package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ColorConfigInfoLine;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Locale;

public class ColorSetter extends TextSetter<Color> {

    public ColorSetter(int x, int y, ColorConfigInfoLine line, Color color) {
        super(x, y, line, color);
        this.textField = new TextField(this.x + 9, this.y, line, 6, "0123456789abcdefABCDEF");
        this.value = color;
        this.textField.setText(this.getStringValue());
    }

    public void readStringValue(String s) {
        try {
            this.value = new Color(Integer.parseInt(s, 16));
        } catch (NumberFormatException e) {
            this.value = null;
        }

    }

    public String getStringValue() {
        if(this.value == null) { return ""; }
        return String.format(Locale.ROOT, "%06X", this.value.getRGB() & 0xffffff);
    }

    public int getWidth() {
        return this.textField.getWidth() + 10;
    }

    public int getHeight() {
        return 9;
    }

    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.textField.setPosition(x + 9, y);
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/color_setter"));
        if(this.value != null) {
            context.fill(this.x + 1, this.y + 1, this.x + 8, this.y + 8, this.value.getRGB() & 0xffffff | 0xff000000);
        }
        this.textField.render(context, mouseX, mouseY, delta);
    }
}
