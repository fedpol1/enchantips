package com.fedpol1.enchantips.util;

import java.util.HashMap;

public abstract class EnchantmentFilterContext {

    private static HashMap<IntPair, IntPair> context = new HashMap<>();

    public static void put(int offerLevel, int enchantability, int lowerBound, int upperBound) {
        IntPair key = new IntPair(offerLevel, enchantability / 4);
        if(!EnchantmentFilterContext.context.containsKey(key)) {
            EnchantmentFilterContext.context.put(key, new IntPair(lowerBound, upperBound));
            return;
        }
        IntPair bounds = EnchantmentFilterContext.getBounds(offerLevel, enchantability);
        EnchantmentFilterContext.context.put(key, new IntPair(
                Math.max(bounds.first(), lowerBound),
                Math.min(bounds.second(), upperBound)
        ));
    }

    public static IntPair getBounds(int offerLevel, int enchantability) {
        return EnchantmentFilterContext.context.get(new IntPair(offerLevel, enchantability / 4));
    }

    public static void reset() {
        EnchantmentFilterContext.context = new HashMap<>();
    }
}
