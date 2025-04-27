package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.Locale;

public class ColorSetter extends TextSetter<Color>{


    public ColorSetter(int x, int y, Color color) {
        super(x, y, color);
        this.textField = new TextField(
                this.x + 9,
                this.y,
                6,
                "0123456789abcdefABCDEF",
                true
        );
        this.value = color;
        this.textField.setText(this.getStringValue());
    }

    public void readStringValue(String s) {
        this.value = new Color(s.isEmpty() ? 0 : Integer.parseInt(s, 16));
    }

    public String getStringValue() {
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

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/color_setter"));
        context.fill(this.x + 1, this.y + 1, this.x + 8, this.y + 8, this.value.getRGB() & 0xffffff | 0xff000000 );
        this.textField.render(context, mouseX, mouseY, delta);
    }
}
