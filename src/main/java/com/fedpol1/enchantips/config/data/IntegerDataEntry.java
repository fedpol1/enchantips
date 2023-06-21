package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.data.visitor.DataVisitor;

public class IntegerDataEntry extends AbstractDataEntry<Integer> implements DataEntry<IntegerDataEntry.IntegerData, Integer> {

    private final IntegerData data;

    public IntegerDataEntry(String key, int defaultValue, int min, int max, int step, boolean hasTooltip) {
        super(key, hasTooltip);
        this.data = new IntegerData(this, defaultValue, min, max, step);
    }

    public IntegerData getData() {
        return this.data;
    }

    public Integer getValue() {
        return this.data.value;
    }

    public static class IntegerData implements Data<Integer> {

        private final IntegerDataEntry entry;
        private int value;
        private final int defaultValue;
        private final int min;
        private final int max;
        private final int step;

        IntegerData(IntegerDataEntry entry, int defaultValue, int min, int max, int step) {
            this.entry = entry;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
            this.min = min;
            this.max = max;
            this.step = step;
        }

        public IntegerDataEntry getEntry() {
            return this.entry;
        }

        public void setValueToDefault() {
            this.value = this.defaultValue;
        }

        public Integer getValue() {
            return this.value;
        }

        public String getStringValue() {
            return Integer.toString(this.getValue());
        }

        public Integer getDefaultValue() {
            return this.defaultValue;
        }

        public void setValue(Integer v) {
            this.value = v;
        }

        public void readStringValue(String s) {
            this.value = Integer.parseInt(s);
        }

        public int getMin() {
            return this.min;
        }

        public int getMax() {
            return this.max;
        }

        public int getStep() {
            return this.step;
        }

        public Object accept(DataVisitor v) {
            return v.visit(this);
        }
    }
}
