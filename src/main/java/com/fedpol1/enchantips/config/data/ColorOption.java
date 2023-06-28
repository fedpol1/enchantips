package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.visitor.DataVisitor;

import java.awt.Color;
import java.util.Locale;

public class ColorOption implements Data<Color> {

    private Color color;
    private final Color defaultValue;

    public ColorOption(int defaultValue) {
        this.color = new Color(defaultValue);
        this.defaultValue = new Color(defaultValue);
    }

    public Color getValue() {
        return this.color == null ? this.getDefaultValue() : this.color;
    }

    public String getStringValue() {
        return String.format(Locale.ROOT, "#%06X", this.getValue().getRGB() & 0xffffff); // discard alpha
    }

    public Color getDefaultValue() {
        // this should never be null
        return this.defaultValue;
    }

    public void setValue(Color c) {
        this.color = c;
    }

    public void readStringValue(String s) {
        this.color = new Color(Integer.parseInt(s.substring(1), 16));
    }

    public Object accept(DataVisitor v) {
        return v.visit(this);
    }
}
