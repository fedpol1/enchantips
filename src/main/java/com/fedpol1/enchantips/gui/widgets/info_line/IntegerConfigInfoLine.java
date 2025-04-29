package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.IntegerSetter;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class IntegerConfigInfoLine extends ConfigInfoLine<Integer> implements Drawable, Element {

    public IntegerConfigInfoLine(Text text, Data<Integer> data, Integer value) {
        super(text, data);
        this.height = 0;
        this.setter = new IntegerSetter(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, value);
    }
}
