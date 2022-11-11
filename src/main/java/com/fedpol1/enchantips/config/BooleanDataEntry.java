package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;

public class BooleanDataEntry {


    public final String key;
    public final BooleanData data;

    public BooleanDataEntry(String key, boolean defaultValue) {
        this.key = key;
        this.data = new BooleanData(defaultValue, defaultValue, EnchantipsClient.MODID + ".config." + key.replace('_', '.'));
    }

    public boolean getValue() {
        return this.data.value;
    }

    public static class BooleanData {
        private boolean value;
        private final boolean defaultValue;
        private final String lang;

        BooleanData(boolean value, boolean defaultValue, String  lang) {
            this.value = value;
            this.defaultValue = defaultValue;
            this.lang = lang;
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

        public String getLang() {
            // this should never be null
            return this.lang;
        }

        public void setValue(boolean v) {
            this.value = v;
        }
    }
}
