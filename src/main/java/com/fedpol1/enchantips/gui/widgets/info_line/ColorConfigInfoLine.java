package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.ColorSetter;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

import java.awt.*;

public class ColorConfigInfoLine extends ConfigInfoLine<Color> implements Drawable, Element {

    public ColorConfigInfoLine(Text text, Data<Color> data, Color value) {
        super(text, data);
        this.height = 0;
        this.setter = new ColorSetter(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, value);
    }
}
