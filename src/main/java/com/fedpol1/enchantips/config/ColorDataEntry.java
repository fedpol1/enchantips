package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.text.Text;
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

    public TextColor getColor() {
        return this.data.color;
    }

    public static class ColorData {
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

        public TextColor getColor() {
            if(this.color == null) {
                EnchantipsClient.LOGGER.warn(title + " color is null, setting default");
                this.color = this.getDefaultColor();
            }
            return this.color;
        }

        public TextColor getDefaultColor() {
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

        public void setColor(TextColor c) {
            this.color = c;
        }

        public void setColor(int c) {
            this.setColor(TextColor.fromRgb(c));
        }
    }
}
