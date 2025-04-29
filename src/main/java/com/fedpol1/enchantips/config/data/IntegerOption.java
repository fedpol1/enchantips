package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.IntegerConfigInfoLine;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.IntegerFieldControllerBuilder;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;

public class IntegerOption implements Data<Integer> {

    private int value;
    private final int defaultValue;
    private final int min;
    private final int max;
    private final int step;

    public IntegerOption(int defaultValue, int min, int max, int step) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
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

    public ConfigInfoLine<Integer> getConfigLine(OptionNode<Integer> optionNode) {
        return new IntegerConfigInfoLine(
                this.getOptionTitle(optionNode),
                this,
                this.value
                );
    }

    public Option<Integer> getYaclOption(OptionNode<Integer> optionNode) {
        return Option.<Integer>createBuilder()
                .binding(this.getDefaultValue(), this::getValue, this::setValue)
                .controller(o -> this.step == 0 ?
                        IntegerFieldControllerBuilder.create(o)
                                .min(this.min)
                                .max(this.max) :
                        IntegerSliderControllerBuilder.create(o)
                                .range(this.min, this.max)
                                .step(this.step)
                )
                .name(this.getOptionTitle(optionNode))
                .description(this.getOptionDescription(optionNode))
                .build();
    }
}
