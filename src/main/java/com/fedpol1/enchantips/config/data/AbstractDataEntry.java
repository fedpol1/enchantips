package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.EnchantipsClient;

public abstract class AbstractDataEntry<T> {

    protected final String key;
    protected final String title;
    protected final String tooltip;

    AbstractDataEntry(String key, boolean hasTooltip) {
        this.key = key;
        this.title = EnchantipsClient.MODID + ".config.title." + key;
        this.tooltip = hasTooltip ? EnchantipsClient.MODID + ".config.tooltip." + key : "";
    }

    public abstract Data<T> getData();

    public String getKey() {
        return this.key;
    }
}
