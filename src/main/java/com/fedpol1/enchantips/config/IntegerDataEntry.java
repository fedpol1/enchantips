package com.fedpol1.enchantips.config;

import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import net.minecraft.text.Text;

public class IntegerDataEntry extends AbstractDataEntry implements DataEntry {

    public final IntegerData data;

    public IntegerDataEntry(String key, ModConfigCategory category, int defaultValue) {
        this(key, category, defaultValue, false);
    }

    public IntegerDataEntry(String key, ModConfigCategory category, int defaultValue, boolean hasTooltip) {
        super(key, category, hasTooltip);
        this.data = new IntegerData(this, defaultValue);
    }

    public int getValue() {
        return this.data.value;
    }

    public static class IntegerData implements Data<Integer> {

        private final IntegerDataEntry entry;
        private int value;
        private final int defaultValue;

        IntegerData(IntegerDataEntry entry, int defaultValue) {
            this.entry = entry;
            this.value = defaultValue;
            this.defaultValue = defaultValue;
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

        public Option<Integer> getOption() {
            return Option.createBuilder(Integer.class)
                    .name(Text.translatable(this.getEntry().getTitle()))
                    .tooltip(Text.translatable(this.getEntry().getTooltip()))
                    .binding(this.getDefaultValue(), this::getValue, this::setValue)
                    .controller(o -> new IntegerSliderController(o, 0, 16, 1))
                    .build();
        }
    }
}
