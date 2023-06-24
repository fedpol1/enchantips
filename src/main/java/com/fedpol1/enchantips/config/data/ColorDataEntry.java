package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.data.visitor.DataVisitor;
import java.awt.Color;
import java.util.Locale;

public class ColorDataEntry extends AbstractDataEntry<Color> implements DataEntry<ColorDataEntry.ColorData, Color> {

    private final ColorData data;

    public ColorDataEntry(String key, int defaultValue, int tooltipLines) {
        super(key, tooltipLines);
        this.data = new ColorData(this, defaultValue);
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
        private final Color defaultValue;

        ColorData(ColorDataEntry entry, int defaultValue) {
            this.entry = entry;
            this.color = new Color(defaultValue);
            this.defaultValue = new Color(defaultValue);
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
}
