package com.fedpol1.enchantips.config;

import java.util.Locale;

public enum ModConfigCategory {
    TOOLTIP_TOGGLE(0),
    ENCHANTMENT_GROUP_COLOR(1),
    TOOLTIP_COLOR(2),
    SLOT_HIGHLIGHT(3),
    MISCELLANEOUS(4);

    private final int ordinal;

    ModConfigCategory(int o) {
        this.ordinal = o;
    }

    public int getOrdinal() {
        return this.ordinal;
    }

    public static int getSize() {
        return ModConfigCategory.values().length;
    }

    public String toString() {
        return super.toString().toLowerCase(Locale.ROOT);
    }
}
