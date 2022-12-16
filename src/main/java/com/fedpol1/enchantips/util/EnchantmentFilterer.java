package com.fedpol1.enchantips.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public abstract class EnchantmentFilterer {

    public static int getLowerBound(Enchantment enchantment, int enchantmentLevel, ItemStack itemStack, int tableLevel) {
        double modifiedEnchantmentLevel = (double)(tableLevel + 1) * 0.85;
        int enchantmentBound = (int)MathHelper.clamp(Math.round(modifiedEnchantmentLevel), 1, Integer.MAX_VALUE);
        return Math.max(getLowerBoundForEnchantment(enchantment, enchantmentLevel), enchantmentBound);
    }

    public static int getUpperBound(Enchantment enchantment, int enchantmentLevel, ItemStack itemStack, int tableLevel) {
        double modifiedEnchantmentLevel = (double)(tableLevel + 1 + itemStack.getItem().getEnchantability() / 4 * 2) * 1.15;
        // double negation used to round x.5 down to x since Math.round rounds to +inf
        // this is done because multiplication above is by 1.15 but should be by value slightly less than 1.15
        int enchantmentBound = (int)MathHelper.clamp(-Math.round(-modifiedEnchantmentLevel), 1, Integer.MAX_VALUE);
        return Math.min(getUpperBoundForEnchantment(enchantment, enchantmentLevel), enchantmentBound);
    }

    public static int getLowerBoundForEnchantment(Enchantment enchantment, int enchantmentLevel) {
        return enchantment.getMinPower(enchantmentLevel);
    }

    public static int getUpperBoundForEnchantment(Enchantment enchantment, int enchantmentLevel) {
        if(enchantment.getMaxLevel() > enchantmentLevel) {
            return Math.min(enchantment.getMaxPower(enchantmentLevel), enchantment.getMinPower(enchantmentLevel + 1) - 1);
        }
        return enchantment.getMaxPower(enchantmentLevel);
    }
}
