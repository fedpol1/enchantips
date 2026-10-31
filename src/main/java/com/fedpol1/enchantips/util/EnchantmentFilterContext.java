package com.fedpol1.enchantips.util;

import java.util.HashMap;

public abstract class EnchantmentFilterContext {

    private static HashMap<FilterContextKey, IntBounds> context = new HashMap<>();

    public static void put(int offerLevel, int enchantability, int slot, int lowerBound, int upperBound) {
        FilterContextKey key = new FilterContextKey(offerLevel, enchantability / 4, slot);
        if(!EnchantmentFilterContext.context.containsKey(key)) {
            EnchantmentFilterContext.context.put(key, new IntBounds(lowerBound, upperBound));
            return;
        }
        IntBounds bounds = EnchantmentFilterContext.getBounds(offerLevel, enchantability, slot);
        EnchantmentFilterContext.context.put(key, new IntBounds(
                Math.max(bounds.lower(), lowerBound),
                Math.min(bounds.upper(), upperBound)
        ));
    }

    public static IntBounds getBounds(int offerLevel, int enchantability, int slot) {
        return EnchantmentFilterContext.context.get(new FilterContextKey(offerLevel, enchantability / 4, slot));
    }

    public static void reset() {
        EnchantmentFilterContext.context = new HashMap<>();
    }

    public record FilterContextKey(int offerLevel, int enchantability, int slot) {
    }
}
