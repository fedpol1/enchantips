package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.widgets.tiny.BooleanButton;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class BooleanConfigInfoLine extends ConfigInfoLine<Boolean> implements Drawable, Element {

    public BooleanConfigInfoLine(Text text, ModOption<Boolean> option, boolean value) {
        super(text, option);
        this.height = 0;
        this.setter = new BooleanButton(this.x + this.resetButton.getWidth() + 1, this.y, value);
    }
}
