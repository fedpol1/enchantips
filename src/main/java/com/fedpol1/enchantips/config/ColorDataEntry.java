package com.fedpol1.enchantips.config;

import net.minecraft.text.TextColor;

public class ColorDataEntry extends AbstractDataEntry implements DataEntry {

    public final ColorData data;

    public ColorDataEntry(String key, ModConfigCategory category, int defaultColor) {
        this(key, category, defaultColor, false);
    }

    public ColorDataEntry(String key, ModConfigCategory category, int defaultColor, boolean hasTooltip) {
        super(key, category, hasTooltip);
        this.data = new ColorData(this, defaultColor);
    }

    public TextColor getValue() {
        return this.data.color;
    }

    public static class ColorData implements Data<TextColor> {

        private final ColorDataEntry entry;
        private TextColor color;
        private final TextColor defaultColor;

        ColorData(ColorDataEntry entry, int defaultColor) {
            this.entry = entry;
            this.color = TextColor.fromRgb(defaultColor);
            this.defaultColor = TextColor.fromRgb(defaultColor);
        }

        public void setValueToDefault() {
            this.color = this.defaultColor;
        }

        public TextColor getValue() {
            return this.color == null ? this.getDefaultValue() : this.color;
        }

        public ColorDataEntry getEntry() {
            return this.entry;
        }

        public String getStringValue() {
            return this.getValue().getHexCode();
        }

        public TextColor getDefaultValue() {
            // this should never be null
            return this.defaultColor;
        }

        public void setValue(TextColor c) {
            this.color = c;
        }

        public void readStringValue(String s) {
            this.color = TextColor.parse(s);
        }
    }
}
