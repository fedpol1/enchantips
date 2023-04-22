package com.fedpol1.enchantips.config.data;

import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;

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

        public Option.Builder<Integer> getOptionBuilder() {
            return Option.createBuilder(Integer.class)
                    .binding(this.getDefaultValue(), this::getValue, this::setValue)
                    .controller(o -> this.step == 0 ? new IntegerFieldController(o, this.min, this.max) : new IntegerSliderController(o, this.min, this.max, this.step));
        }
    }
}
