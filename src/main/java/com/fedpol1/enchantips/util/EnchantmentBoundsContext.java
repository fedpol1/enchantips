package com.fedpol1.enchantips.util;

import java.util.HashMap;
import java.util.Map;

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

    protected static HashMap<EnchantmentBoundsContext.FilterContextKeySimple, IntBounds> copy() {
        HashMap<EnchantmentBoundsContext.FilterContextKeySimple, IntBounds> newContext = new HashMap<>();
        for(Map.Entry<EnchantmentBoundsContext.FilterContextKeySimple, IntBounds> entry : EnchantmentBoundsContext.context.entrySet()) {
            EnchantmentBoundsContext.FilterContextKeySimple key = entry.getKey();
            IntBounds value = entry.getValue();
            newContext.put(
                    new EnchantmentBoundsContext.FilterContextKeySimple(key.offerLevel, key.enchantability, key.slot),
                    new IntBounds(value.lower(), value.upper())
            );
        }
        return newContext;
    }

    public static void reset() {
        EnchantmentBoundsContext.context = new HashMap<>();
    }

    public record FilterContextKeySimple(int offerLevel, int enchantability, int slot) {
    }
}
