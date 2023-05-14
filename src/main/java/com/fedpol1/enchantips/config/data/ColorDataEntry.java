package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.data.visitor.DataVisitor;
import java.awt.Color;
import java.util.Locale;

public class ColorDataEntry extends AbstractDataEntry<Color> implements DataEntry<ColorDataEntry.ColorData, Color> {

    private final ColorData data;

    public ColorDataEntry(String key, int defaultColor, boolean hasTooltip) {
        super(key, hasTooltip);
        this.data = new ColorData(this, defaultColor);
    }

    public ColorData getData() {
        return this.data;
    }

    public Color getValue() {
        return this.data.color;
    }

    public static class ColorData implements Data<Color> {

        private final ColorDataEntry entry;
        private Color color;
        private final Color defaultColor;

        ColorData(ColorDataEntry entry, int defaultColor) {
            this.entry = entry;
            this.color = new Color(defaultColor);
            this.defaultColor = new Color(defaultColor);
        }

        public void setValueToDefault() {
            this.color = this.defaultColor;
        }

        public Color getValue() {
            return this.color == null ? this.getDefaultValue() : this.color;
        }

        public ColorDataEntry getEntry() {
            return this.entry;
        }

        public String getStringValue() {
            return String.format(Locale.ROOT, "#%06X", this.getValue().getRGB() & 0xffffff); // discard alpha
        }

        public Color getDefaultValue() {
            // this should never be null
            return this.defaultColor;
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
}
