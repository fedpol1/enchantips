package com.fedpol1.enchantips.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public abstract class EnchantmentFilterer {

    public static int getLowerBound(Enchantment enchantment, int enchantmentLevel, ItemStack itemStack, int tableLevel) {
        double modifiedEnchantmentLevel = (double)(tableLevel + 1) * 0.85;
        int enchantmentBound = (int)MathHelper.clamp(Math.round(modifiedEnchantmentLevel), 1, Integer.MAX_VALUE);
        return Math.max(EnchantmentLevelData.of(enchantment, enchantmentLevel).getLowestModifiedLevel(), enchantmentBound);
    }

    public static int getUpperBound(Enchantment enchantment, int enchantmentLevel, ItemStack itemStack, int tableLevel) {
        double modifiedEnchantmentLevel = (double)(tableLevel + 1 + itemStack.getItem().getEnchantability() / 4 * 2) * 1.15;
        // double negation used to round x.5 down to x since Math.round rounds to +inf
        // this is done because multiplication above is by 1.15 but should be by value slightly less than 1.15
        int enchantmentBound = (int)MathHelper.clamp(-Math.round(-modifiedEnchantmentLevel), 1, Integer.MAX_VALUE);
        return Math.min(EnchantmentLevelData.of(enchantment, enchantmentLevel).getHighestModifiedLevel(), enchantmentBound);
    }
}
