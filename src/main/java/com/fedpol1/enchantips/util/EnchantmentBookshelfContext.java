package com.fedpol1.enchantips.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantable;

import java.util.HashMap;

public abstract class EnchantmentBookshelfContext {

    private static HashMap<Integer, int[]> context = new HashMap<>();

    public static void put(int books, ItemStack stack, int[] offers) {
        Enchantable component = stack.get(DataComponents.ENCHANTABLE);
        if(component == null || stack.isEmpty() || !stack.isEnchantable()) {
            return;
        }
        EnchantmentBookshelfContext.context.put(books, offers);
    }

    public static int[] get(int books) {
        return EnchantmentBookshelfContext.context.get(books);
    }

    public static void reset() {
        EnchantmentBookshelfContext.context = new HashMap<>();
    }
}
