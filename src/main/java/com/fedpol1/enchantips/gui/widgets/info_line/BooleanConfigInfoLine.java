package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.BooleanButton;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

import java.util.List;

public class BooleanConfigInfoLine extends ConfigInfoLine<Boolean> implements Drawable, Element {

    public BooleanConfigInfoLine(Text text, List<Text> tooltip, Data<Boolean> data, boolean value) {
        super(text, tooltip, data);
        this.height = 0;
        this.setter = new BooleanButton(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, value);
    }
}
