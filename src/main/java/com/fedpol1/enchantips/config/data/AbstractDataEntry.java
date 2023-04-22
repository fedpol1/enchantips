package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.EnchantipsClient;

public abstract class AbstractDataEntry<T> {

    protected final String key;
    protected final boolean hasTooltip;

    AbstractDataEntry(String key, boolean hasTooltip) {
        this.key = key;
        this.hasTooltip = hasTooltip;
    }

    public boolean hasTooltip() {
        return this.hasTooltip;
    }

    public abstract Data<T> getData();

    public String getKey() {
        return this.key;
    }
}
