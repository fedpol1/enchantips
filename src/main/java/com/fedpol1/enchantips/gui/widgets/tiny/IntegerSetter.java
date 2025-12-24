package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.IntegerConfigInfoLine;
import net.minecraft.client.gui.GuiGraphics;

public class IntegerSetter extends TextSetter<Integer> {

    public IntegerSetter(int x, int y, IntegerConfigInfoLine line, Integer value) {
        super(x, y, line, value);
        this.textField = new TextField(this.x, this.y, line, line.getMaximumDigits(), "-0123456789");
        this.textField.setText(this.getStringValue());
    }

    public void readStringValue(String s) {
        try {
            this.value = Integer.parseInt(s);
        } catch (NumberFormatException e) {
            this.value = null;
        }

    }

    public String getStringValue() {
        if(this.value == null) { return ""; }
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

    @Override
    public boolean canTrigger() {
        return true;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        this.render(context, mouseX, mouseY, delta, EnchantipsClient.id("config/integer_setter"));
        this.textField.render(context, mouseX, mouseY, delta);
    }
}
