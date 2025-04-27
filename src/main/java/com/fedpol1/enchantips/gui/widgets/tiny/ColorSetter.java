package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Locale;

public class ColorSetter extends BaseSetter{

    protected boolean focused;
    protected Color color;
    protected TextField textField;

    public ColorSetter(int x, int y, Color color) {
        super(x, y);
        this.textField = new TextField(
                this.x + 9,
                this.y,
                6,
                "0123456789abcdefABCDEF",
                true
        );
        this.textField.setText(this.colorToString(color));
        this.color = color;
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
        return this.textField.getWidth() + 10;
    }

    public int getHeight() {
        return 9;
    }

    public void setColor(Color c) {
        this.color = c;
        this.textField.setText(this.colorToString(c));
    }

    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.textField.setPosition(x + 9, y);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/color_setter"));
        context.fill(this.x + 1, this.y + 1, this.x + 8, this.y + 8, this.color.getRGB() & 0xffffff | 0xff000000 );
        this.textField.render(context, mouseX, mouseY, delta);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isWithinBounds = super.mouseClicked(mouseX, mouseY, button, () -> this.textField.selectionManager.selectAll());
        this.focused = isWithinBounds;
        this.textField.focused = isWithinBounds;
        return isWithinBounds;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(!this.focused) { return false; }
        if(this.textField.keyPressed(keyCode, scanCode, modifiers)) {
            this.color = this.stringToColor(this.textField.text);
            return true;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if(!this.focused) { return false; }
        if(this.textField.charTyped(chr, modifiers)) {
            this.color = this.stringToColor(this.textField.text);
            return true;
        }
        return false;
    }
}
