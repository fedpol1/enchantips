package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Locale;

public class ColorSetter extends BaseSetter{

    private static final String[] ALLOWED_CHARACTERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "A", "B", "C", "D", "E", "F"};
    private static final int MAXIMUM_LENGTH = 6;

    protected boolean focused;
    protected Color color;
    protected String colorText;
    protected SelectionManager selectionManager;

    public ColorSetter(int x, int y, Color color) {
        super(x, y);
        this.color = color;
        this.colorText = this.colorToString(this.color); // discard alpha
        this.selectionManager = new SelectionManager(
                () -> this.colorText,
                this::setColorText,
                SelectionManager.makeClipboardGetter(MinecraftClient.getInstance()),
                SelectionManager.makeClipboardSetter(MinecraftClient.getInstance()),
                s -> s.matches("[0-9A-Fa-f]*")
        );
        this.focused = false;
    }

    public Color stringToColor(String s) {
        if(s.isEmpty()) {
            return new Color(0);
        }
        return new Color(Integer.parseInt(s, 16));
    }

    public String colorToString(Color c) {
        return String.format(Locale.ROOT, "%06X", c.getRGB() & 0xffffff);
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxWidth = 0;
        for(String c : ColorSetter.ALLOWED_CHARACTERS) {
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal(c)));
        }
        return maxWidth * 6 + 4 + 7;
    }

    public int getHeight() {
        return 9;
    }

    public void setColor(Color c) {
        this.color = c;
        this.colorText = this.colorToString(c);
    }

    public void setColorText(String s) {
        this.colorText = s;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/color_setter"));

        int sectionWidth = this.selectionManager.getSelectionEnd() - this.selectionManager.getSelectionStart();
        int sectionStart = Math.min(this.selectionManager.getSelectionEnd(), this.selectionManager.getSelectionStart());
        String path = sectionWidth < 0 ? "selection" : "selection_alt";

        context.fill(this.x + 1, this.y + 1, this.x + 8, this.y + 8, this.color.getRGB() & 0xffffff | 0xff000000 );
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                Identifier.of(EnchantipsClient.MODID, path),
                this.x +  9 + sectionStart * 6, this.y,
                1 + Math.abs(sectionWidth) * 6, 9
        );
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                this.colorText,
                this.x + 10, this.y + 1,
                0xffffff, false
        );
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isWithinBounds = super.mouseClicked(mouseX, mouseY, button, () -> {});
        this.focused = isWithinBounds;
        return isWithinBounds;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(!this.focused) { return false; }
        if(this.selectionManager.handleSpecialKey(keyCode)) {
            this.shiftText();
            return true;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if(!this.focused) { return false; }
        if(this.selectionManager.insert(Character.toUpperCase(chr))) {
            this.shiftText();
        }
        return true;
    }

    private void shiftText() {
        int deletePosition = 0;
        int deleteLength = Math.max(0, this.colorText.length() - MAXIMUM_LENGTH);
        if(this.selectionManager.getSelectionStart() < this.colorText.length()) {
            deletePosition = this.selectionManager.getSelectionStart();
        }
        int deleteLengthAtStart = Math.max(0, deletePosition + deleteLength - this.colorText.length());
        deleteLength -= deleteLengthAtStart;

        if(this.colorText.length() > 6) {
            this.colorText = this.colorText.substring(deleteLengthAtStart, deletePosition) +
                    this.colorText.substring(deletePosition + deleteLength);
            this.color = this.stringToColor(this.colorText);
            this.selectionManager.setSelection(
                    Math.min(this.colorText.length(), this.selectionManager.getSelectionStart()),
                    Math.min(this.colorText.length(), this.selectionManager.getSelectionEnd())
            );
        }
    }
}
