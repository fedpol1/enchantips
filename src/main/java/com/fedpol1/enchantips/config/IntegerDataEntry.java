package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;

public class IntegerDataEntry {

    public final String key;
    public final IntegerData data;

    public IntegerDataEntry(String key, int defaultValue) {
        this(key, defaultValue, false);
    }

    public IntegerDataEntry(String key, int defaultValue, boolean hasTooltip) {
        this.key = key;
        this.data = new IntegerData(defaultValue, defaultValue, key, hasTooltip);
    }

    public int getValue() {
        return this.data.value;
    }

    public static class IntegerData implements Data<Integer> {
        private int value;
        private final int defaultValue;
        private final String title;
        private final String tooltip;

        IntegerData(int value, int defaultValue, String key, boolean hasTooltip) {
            this.value = value;
            this.defaultValue = defaultValue;
            this.title = EnchantipsClient.MODID + ".config.title." + key;
            if(hasTooltip) {
                this.tooltip = EnchantipsClient.MODID + ".config.tooltip." + key;
            }
            else {
                this.tooltip = "";
            }
        }

        public void setValueToDefault() {
            this.value = this.defaultValue;
        }

        public Integer getValue() {
            return this.value;
        }

        public Integer getDefaultValue() {
            return this.defaultValue;
        }

        public String getTitle() {
            // this should never be null
            return this.title;
        }

        public String getTooltip() {
            return this.tooltip;
        }

        public void setValue(Integer v) {
            this.value = v;
        }
    }
}
