package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.IntegerSetter;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

import java.util.List;

public class IntegerConfigInfoLine extends ConfigInfoLine<Integer> implements Drawable, Element {

    public IntegerConfigInfoLine(Text text, List<Text> tooltip, Data<Integer> data, Integer value) {
        super(text, tooltip, data);
        this.height = 0;
        this.setter = new IntegerSetter(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, this, value);
        this.setters.add(this.setter);
    }
}
