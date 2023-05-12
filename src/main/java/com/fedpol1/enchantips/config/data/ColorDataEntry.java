package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.data.visitor.DataVisitor;
import com.fedpol1.enchantips.util.ColorManager;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.ColorController;
import java.awt.Color;

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
            return ColorManager.colorToString(this.getValue());
        }

        public Color getDefaultValue() {
            // this should never be null
            return this.defaultColor;
        }

        public void setValue(Color c) {
            this.color = c;
        }

        public void readStringValue(String s) {
            this.color = ColorManager.stringToColor(s);
        }

        public Object accept(DataVisitor v) {
            return v.visit(this);
        }
    }
}
