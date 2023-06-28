package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.visitor.DataVisitor;

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
