package com.fedpol1.enchantips.util;

import net.minecraft.component.ComponentType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;

public abstract class EnchantmentFilterer {

    public static int getLowerBound(EnchantmentLevel enchLevel, ItemStack itemStack, int tableLevel) throws IllegalStateException {
        double modifiedEnchantmentLevel = (double)(tableLevel + 1) * 0.85;
        int enchantmentBound = (int)MathHelper.clamp(Math.round(modifiedEnchantmentLevel), 1, Integer.MAX_VALUE);
        return Math.max(enchLevel.getLowestModifiedLevel(), enchantmentBound);
    }

    public static int getUpperBound(EnchantmentLevel enchLevel, ItemStack itemStack, int tableLevel) throws IllegalStateException {
        EnchantableComponent enchComponent = itemStack.get(DataComponentTypes.ENCHANTABLE);
        int enchantability = enchComponent == null ? 0 : enchComponent.value();
        double modifiedEnchantmentLevel = (double)(tableLevel + 1 + enchantability / 4 * 2) * 1.15;
        // double negation used to round x.5 down to x since Math.round rounds to +inf
        // this is done because multiplication above is by 1.15 but should be by value slightly less than 1.15
        int enchantmentBound = (int)MathHelper.clamp(-Math.round(-modifiedEnchantmentLevel), 1, Integer.MAX_VALUE);
        return Math.min(enchLevel.getHighestModifiedLevel(), enchantmentBound);
    }
}
