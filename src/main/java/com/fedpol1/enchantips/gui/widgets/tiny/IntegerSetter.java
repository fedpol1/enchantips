package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class IntegerSetter extends TextSetter<Integer>{

    public IntegerSetter(int x, int y, Integer value) {
        super(x, y, value);
        this.textField = new TextField(
                this.x,
                this.y,
                9,
                "0123456789",
                true
        );
        this.textField.setText(this.getStringValue());
    }

    public void readStringValue(String s) {
        this.value = Integer.parseInt(s.isEmpty() ? "0" : s);
    }

    public String getStringValue() {
        return Integer.toString(this.value);
    }

    public int getWidth() {
        return this.textField.getWidth() + 2;
    }

    public int getHeight() {
        return 9;
    }

    public void setValue(int i) {
        this.value = i;
        this.textField.setText(this.getStringValue());
    }

    public void setPosition(int x, int y) {
        super.setPosition(x, y);
        this.textField.setPosition(x + 1, y);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/integer_setter"));
        this.textField.render(context, mouseX, mouseY, delta);
    }
}
