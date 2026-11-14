package com.fedpol1.enchantips.util;

import java.util.HashMap;

public abstract class EnchantmentBoundsContext {

    private static HashMap<FilterContextKeySimple, IntBounds> context = new HashMap<>();

    public static void put(int offerLevel, int enchantability, int slot, int lowerBound, int upperBound) {
        FilterContextKeySimple key = new FilterContextKeySimple(offerLevel, enchantability / 4, slot);
        if(!EnchantmentBoundsContext.context.containsKey(key)) {
            EnchantmentBoundsContext.context.put(key, new IntBounds(lowerBound, upperBound));
            return;
        }
        IntBounds bounds = EnchantmentBoundsContext.getBounds(offerLevel, enchantability, slot);
        EnchantmentBoundsContext.context.put(key, new IntBounds(
                Math.max(bounds.lower(), lowerBound),
                Math.min(bounds.upper(), upperBound)
        ));
    }

    public static IntBounds getBounds(int offerLevel, int enchantability, int slot) {
        return EnchantmentBoundsContext.context.get(new FilterContextKeySimple(offerLevel, enchantability / 4, slot));
    }

    public static void reset() {
        EnchantmentBoundsContext.context = new HashMap<>();
    }

    public record FilterContextKeySimple(int offerLevel, int enchantability, int slot) {
    }
}
