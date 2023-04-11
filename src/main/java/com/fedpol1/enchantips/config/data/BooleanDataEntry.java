package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.ModConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import net.minecraft.text.Text;

public class BooleanDataEntry extends AbstractDataEntry<Boolean> implements DataEntry<BooleanDataEntry.BooleanData, Boolean> {

    private final BooleanData data;

    public BooleanDataEntry(String key, ModConfigCategory category, boolean defaultValue, boolean hasTooltip) {
        super(key, category, hasTooltip);
        this.data = new BooleanData(this, defaultValue);
        ModConfig.data.put(key, this.data);
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

        public Option<Boolean> getOption() {
            return Option.createBuilder(Boolean.class)
                    .name(Text.translatable(this.getEntry().getTitle()))
                    .tooltip(Text.translatable(this.getEntry().getTooltip()))
                    .binding(this.getDefaultValue(), this::getValue, this::setValue)
                    .controller(TickBoxController::new)
                    .build();
        }
    }
}
