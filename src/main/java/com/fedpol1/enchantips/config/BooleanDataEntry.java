package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;

public class BooleanDataEntry {


    public final String key;
    public final BooleanData data;

    public BooleanDataEntry(String key, boolean defaultValue) {
        this(key, defaultValue, false);
    }

    public BooleanDataEntry(String key, boolean defaultValue, boolean hasTooltip) {
        this.key = key;
        this.data = new BooleanData(defaultValue, defaultValue, key, hasTooltip);
    }

    public boolean getValue() {
        return this.data.value;
    }

    public static class BooleanData {
        private boolean value;
        private final boolean defaultValue;
        private final String title;
        private final String tooltip;

        BooleanData(boolean value, boolean defaultValue, String key, boolean hasTooltip) {
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

        public boolean getValue() {
            return this.value;
        }

        public boolean getDefaultValue() {
            return this.defaultValue;
        }

        public String getTitle() {
            // this should never be null
            return this.title;
        }

        public String getTooltip() {
            return this.tooltip;
        }

        public void setValue(boolean v) {
            this.value = v;
        }
    }
}
