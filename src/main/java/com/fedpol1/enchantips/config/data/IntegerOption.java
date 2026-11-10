package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.IntegerConfigInfoLine;
import java.util.List;
import net.minecraft.network.chat.Component;

public class IntegerOption implements Data<Integer> {

    private int value;
    private final int defaultValue;
    private final int min;
    private final int max;

    public IntegerOption(int defaultValue, int min, int max) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
    }

    public int getMin() {
        return this.min;
    }

    public int getMax() {
        return this.max;
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

    public boolean canSet(Integer v) {
        return v != null && v >= this.min && v <= this.max;
    }

    public void readStringValue(String s) {
        this.value = Integer.parseInt(s);
    }

    public List<Component> getSaveTooltip(Integer v) {
        if(this.canSet(v)) {
            return null;
        }
        if(v == null) {
            return List.of(Component.translatable("enchantips.gui.setter.integer.error.invalid"));
        }
        return List.of(Component.translatable("enchantips.gui.setter.integer.error.out_of_bounds", v, this.min, this.max));
    }

    public ConfigInfoLine<Integer> getConfigLine(OptionNode<Integer> optionNode) {
        return new IntegerConfigInfoLine(
                this.getOptionTitle(optionNode),
                this.getOptionTooltip(optionNode),
                this,
                this.value
        );
    }
}
