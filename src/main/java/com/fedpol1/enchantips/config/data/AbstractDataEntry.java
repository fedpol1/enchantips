package com.fedpol1.enchantips.config.data;

public abstract class AbstractDataEntry<T> {

    protected final String key;
    protected final int tooltipLines;

    AbstractDataEntry(String key, int tooltipLines) {
        this.key = key;
        this.tooltipLines = tooltipLines;
    }

    public int getNumTooltipLines() {
        return this.tooltipLines;
    }

    public abstract Data<T> getData();

    public String getKey() {
        return this.key;
    }
}
