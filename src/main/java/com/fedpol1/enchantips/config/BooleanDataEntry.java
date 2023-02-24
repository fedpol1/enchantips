package com.fedpol1.enchantips.config;

public class BooleanDataEntry extends AbstractDataEntry implements DataEntry {

    public final BooleanData data;

    public BooleanDataEntry(String key, ModConfigCategory category, boolean defaultValue) {
        this(key, category, defaultValue, false);
    }

    public BooleanDataEntry(String key, ModConfigCategory category, boolean defaultValue, boolean hasTooltip) {
        super(key, category, hasTooltip);
        this.data = new BooleanData(this, defaultValue);
    }

    public boolean getValue() {
        return this.data.value;
    }

    public static class BooleanData implements Data<Boolean> {

        private final BooleanDataEntry entry;
        private boolean value;
        private final boolean defaultValue;

        BooleanData(BooleanDataEntry entry, boolean defaultValue) {
            this.entry = entry;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
        }

        public BooleanDataEntry getEntry() {
            return this.entry;
        }

        public void setValueToDefault() {
            this.value = this.defaultValue;
        }

        public Boolean getValue() {
            return this.value;
        }

        public String getStringValue() {
            return Boolean.toString(this.getValue());
        }

        public Boolean getDefaultValue() {
            return this.defaultValue;
        }

        public void setValue(Boolean v) {
            this.value = v;
        }

        public void readStringValue(String s) {
            this.value = Boolean.parseBoolean(s);
        }
    }
}
