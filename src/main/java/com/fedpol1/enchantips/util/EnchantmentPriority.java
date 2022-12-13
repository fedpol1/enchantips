package com.fedpol1.enchantips.util;

public enum EnchantmentPriority {
    OVERLEVELLED(3),
    CURSED(2),
    TREASURE(1),
    NORMAL(0);

    private final int priority;

    EnchantmentPriority(int priority) {
        this.priority = priority;
    }

    public boolean gt(EnchantmentPriority o) { return this.priority > o.priority; }
    public boolean lt(EnchantmentPriority o) { return this.priority < o.priority; }
    public boolean ge(EnchantmentPriority o) { return this.priority >= o.priority; }
    public boolean le(EnchantmentPriority o) { return this.priority <= o.priority; }
}
