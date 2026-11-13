package com.fedpol1.enchantips.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class EnchantmentSeedContext {

    private static HashMap<FilterContextKey, EnchantmentLevel> context = new HashMap<>();
    protected static boolean searcherUpdate = false;

    public static void put(int offerLevel, ItemStack stack, int slot, Enchantment enchantment, int enchLevel) {
        Enchantable component = stack.get(DataComponents.ENCHANTABLE);
        if(component == null || !stack.isEnchantable()) { return; }
        FilterContextKey key = new FilterContextKey(offerLevel, stack.getItem(), component.value(), slot);
        if(!EnchantmentSeedContext.context.containsKey(key)) {
            EnchantmentSeedContext.context.put(key, EnchantmentLevel.of(enchantment, enchLevel));
            EnchantmentSeedContext.searcherUpdate = true;
        }
    }

    public static EnchantmentLevel get(int offerLevel, ItemStack stack, int slot) {
        Enchantable component = stack.get(DataComponents.ENCHANTABLE);
        if(component == null || !stack.isEnchantable()) { return null; }
        return EnchantmentSeedContext.get(offerLevel, stack.getItem(), component.value(), slot);
    }

    public static EnchantmentLevel get(int offerLevel, Item item, int enchantability, int slot) {
        return EnchantmentSeedContext.context.get(new FilterContextKey(offerLevel, item, enchantability, slot));
    }

    protected static Set<Map.Entry<FilterContextKey, EnchantmentLevel>> entries() {
        return EnchantmentSeedContext.context.entrySet();
    }

    public static void reset() {
        EnchantmentSeedContext.context = new HashMap<>();
        EnchantmentSeedSearcher.reset();
    }

    protected static HashMap<FilterContextKey, EnchantmentLevel> copy() {
        HashMap<FilterContextKey, EnchantmentLevel> newContext = new HashMap<>();
        for(Map.Entry<FilterContextKey, EnchantmentLevel> entry : EnchantmentSeedContext.context.entrySet()) {
            FilterContextKey key = entry.getKey();
            EnchantmentLevel value = entry.getValue();
            newContext.put(
                    new FilterContextKey(key.offerLevel, key.item, key.enchantability, key.slot),
                    EnchantmentLevel.of(value.getEnchantment(), value.getLevel())
            );
        }
        return newContext;
    }

    public record FilterContextKey(int offerLevel, Item item, int enchantability, int slot) {
    }
}
