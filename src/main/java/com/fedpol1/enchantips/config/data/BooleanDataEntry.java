package com.fedpol1.enchantips.config.data;

import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.TickBoxController;

public class BooleanDataEntry extends AbstractDataEntry<Boolean> implements DataEntry<BooleanDataEntry.BooleanData, Boolean> {

    private final BooleanData data;

    public BooleanDataEntry(String key, boolean defaultValue, boolean hasTooltip) {
        super(key, hasTooltip);
        this.data = new BooleanData(this, defaultValue);
    }

    public BooleanData getData() {
        return this.data;
    }

    public Boolean getValue() {
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

        public Option.Builder<Boolean> getOptionBuilder() {
            return Option.createBuilder(Boolean.class)
                    .binding(this.getDefaultValue(), this::getValue, this::setValue)
                    .controller(TickBoxController::new);
        }
    }
}
