package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Locale;

public class ColorSetter extends BaseSetter{

    private static final String[] ALLOWED_CHARACTERS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
            "a", "b", "c", "d", "e", "f", "A", "B", "C", "D", "E", "F"};

    protected boolean focused;
    protected Color color;
    protected String colorText;

    public ColorSetter(int x, int y, Color color) {
        super(x, y);
        this.color = color;
        this.colorText = this.colorToString(this.color); // discard alpha
        this.focused = false;
    }

    public Color stringToColor(String s) {
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

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/color_setter"));
        context.fill(this.x + 1, this.y + 1, this.x + 8, this.y + 8, this.color.getRGB() & 0xffffff | 0xff000000 );
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                this.colorToString(this.color),
                this.x + 10, this.y + 1,
                0xffffff, false
        );
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isWithinBounds = super.mouseClicked(mouseX, mouseY, button, () -> {});
        this.focused = isWithinBounds;
        return isWithinBounds;
    }
}
