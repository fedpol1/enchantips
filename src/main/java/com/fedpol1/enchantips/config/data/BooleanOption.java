package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.visitor.DataVisitor;

public class BooleanOption implements Data<Boolean> {

    private boolean value;
    private final boolean defaultValue;

    public BooleanOption(boolean defaultValue) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
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

    public Object accept(DataVisitor v) {
        return v.visit(this);
    }
}
