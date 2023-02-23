package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.text.TextColor;

public class ColorDataEntry {


    public final String key;
    public final ColorData data;

    public ColorDataEntry(String key, int defaultColor) {
        this(key, defaultColor, false);
    }

    public ColorDataEntry(String key, int defaultColor, boolean hasTooltip) {
        this.key = key;
        this.data = new ColorData(defaultColor, defaultColor, key, hasTooltip);
    }

    public TextColor getValue() {
        return this.data.color;
    }

    public static class ColorData implements Data<TextColor> {
        private TextColor color;
        private final TextColor defaultColor;
        private final String title;
        private final String tooltip;

        ColorData(int color, int defaultColor, String key, boolean hasTooltip) {
            this.color = TextColor.fromRgb(color);
            this.defaultColor = TextColor.fromRgb(defaultColor);
            this.title = EnchantipsClient.MODID + ".config.title." + key;
            if(hasTooltip) {
                this.tooltip = EnchantipsClient.MODID + ".config.tooltip." + key;
            }
            else {
                this.tooltip = "";
            }
        }

        public void setValueToDefault() {
            this.color = this.defaultColor;
        }

        public TextColor getValue() {
            if(this.color == null) {
                EnchantipsClient.LOGGER.warn(title + " color is null, setting default");
                this.color = this.getDefaultValue();
            }
            return this.color;
        }

        public TextColor getDefaultValue() {
            // this should never be null
            return this.defaultColor;
        }

        public String getTitle() {
            // this should never be null
            return this.title;
        }

        public String getTooltip() {
            return this.tooltip;
        }

        public void setValue(TextColor c) {
            this.color = c;
        }
    }
}
