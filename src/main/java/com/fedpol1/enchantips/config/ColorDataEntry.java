package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public class ColorDataEntry {


    public final String key;
    public final ColorData data;

    public ColorDataEntry(String key, int defaultColor) {
        this.key = key;
        this.data = new ColorData(defaultColor, defaultColor, EnchantipsClient.MODID + ".config." + key.replace('_', '.'));
    }

    public TextColor getColor() {
        return this.data.color;
    }

    public static class ColorData {
        private TextColor color;
        private final TextColor defaultColor;
        private final String lang;

        ColorData(int color, int defaultColor, String  lang) {
            this.color = TextColor.fromRgb(color);
            this.defaultColor = TextColor.fromRgb(defaultColor);
            this.lang = lang;
        }

        public void setValueToDefault() {
            this.color = this.defaultColor;
        }

        public TextColor getColor() {
            if(this.color == null) {
                EnchantipsClient.LOGGER.warn(lang + " color is null, setting default");
                this.color = this.getDefaultColor();
            }
            return this.color;
        }

        public TextColor getDefaultColor() {
            // this should never be null
            return this.defaultColor;
        }

        public String getLang() {
            // this should never be null
            return this.lang;
        }

        public void setColor(TextColor c) {
            this.color = c;
        }

        public void setColor(int c) {
            this.setColor(TextColor.fromRgb(c));
        }
    }
}
