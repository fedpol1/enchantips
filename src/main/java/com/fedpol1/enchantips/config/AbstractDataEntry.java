package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;

public abstract class AbstractDataEntry {

    protected final String key;
    protected final String title;
    protected final String tooltip;
    protected final ModConfigCategory category;

    AbstractDataEntry(String key, ModConfigCategory category, boolean hasTooltip) {
        this.key = key;
        this.title = EnchantipsClient.MODID + ".config.title." + key;
        this.tooltip = hasTooltip ? EnchantipsClient.MODID + ".config.tooltip." + key : "";
        this.category = category;
    }

    public String getKey() {
        return this.key;
    }

    public String getTitle() {
        return this.title;
    }

    public String getTooltip() {
        return this.tooltip;
    }

    public ModConfigCategory getCategory() {
        return this.category;
    }
}
