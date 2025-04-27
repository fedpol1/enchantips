package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextField extends BaseSetter {

    protected String text;
    protected SelectionManager selectionManager;
    protected int maximumLength;
    protected String allowedCharacters;
    protected boolean rightAligned;
    protected boolean focused;

    public TextField(int x, int y, int maximumLength, String allowedCharacters, boolean rightAligned) {
        super(x, y);
        this.text = "";
        this.selectionManager = new SelectionManager(
                this::getText,
                this::setText,
                SelectionManager.makeClipboardGetter(MinecraftClient.getInstance()),
                SelectionManager.makeClipboardSetter(MinecraftClient.getInstance()),
                s -> this.text.chars().sequential().allMatch(c1 -> this.allowedCharacters.chars().anyMatch(c2 -> c1 == c2))

        );
        this.maximumLength = maximumLength;
        this.allowedCharacters = allowedCharacters;
        this.rightAligned = rightAligned;
        this.focused = false;
    }

    public int getWidth() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxWidth = 0;
        for(int c : this.allowedCharacters.toCharArray()) {
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal(Character.toString(c))));
        }
        return maxWidth * this.maximumLength + 1;
    }

    public int getHeight() {
        return 9;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String s) {
        this.text = s;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int textWidth = textRenderer.getWidth(Text.literal(this.text));
        // positions measured from right edge
        int sectionStartPos = textRenderer.getWidth(Text.literal(this.text.substring(this.selectionManager.getSelectionStart())));
        int sectionEndPos = textRenderer.getWidth(Text.literal(this.text.substring(this.selectionManager.getSelectionEnd())));
        String selectionPath = sectionStartPos < sectionEndPos ? "selection" : "selection_alt";
        int width = this.getWidth();

        if(this.focused) {
            context.drawGuiTexture(
                    RenderLayer::getGuiTextured,
                    Identifier.of(EnchantipsClient.MODID, selectionPath),
                    this.x - 1 + width - Math.max(sectionStartPos, sectionEndPos), this.y,
                    1 + Math.abs(sectionEndPos - sectionStartPos), 9
            );
        }
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(this.text),
                this.x + width - textWidth, this.y + 1,
                0xffffff, false
        );
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;/*
        boolean isWithinBounds = super.mouseClicked(mouseX, mouseY, button, () -> {});
        if(isWithinBounds) {
            this.selectionManager.selectAll();
        }
        this.focused = isWithinBounds;
        return isWithinBounds;*/
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
            return true;
        }
        return false;
    }

    private void shiftText() {
        int deletePosition = 0;
        int deleteLength = Math.max(0, this.text.length() - this.maximumLength);
        if(this.selectionManager.getSelectionStart() < this.text.length()) {
            deletePosition = this.selectionManager.getSelectionStart();
        }
        int deleteLengthAtStart = Math.max(0, deletePosition + deleteLength - this.text.length());
        deleteLength -= deleteLengthAtStart;

        if(this.text.length() > this.maximumLength) {
            this.text = this.text.substring(deleteLengthAtStart, deletePosition) +
                    this.text.substring(deletePosition + deleteLength);
            this.selectionManager.setSelection(
                    Math.min(this.text.length(), this.selectionManager.getSelectionStart()),
                    Math.min(this.text.length(), this.selectionManager.getSelectionEnd())
            );
        }
    }
}
