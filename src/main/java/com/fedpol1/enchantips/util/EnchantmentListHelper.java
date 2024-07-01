package com.fedpol1.enchantips.util;

import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class EnchantmentListHelper {

    public static int compareEnchantments(RegistryEntry<Enchantment> a, int aLevel, RegistryEntry<Enchantment> b, int bLevel, boolean compareLevels) {
        // compare enchantment IDs
        String enchA = a.getIdAsString();
        String enchB = b.getIdAsString();
        int score = enchA.compareTo(enchB);
        if(score != 0 || !compareLevels) { return score; }

        // compare levels
        return aLevel - bLevel;
    }

    public static int countMatches(ItemEnchantmentsComponent a, ItemEnchantmentsComponent b, boolean compareLevels) {
        int matches = 0;
        for(RegistryEntry<Enchantment> aEntry : a.getEnchantments()) {
            for(RegistryEntry<Enchantment> bEntry : b.getEnchantments()) {
                if(EnchantmentListHelper.compareEnchantments(aEntry, a.getLevel(aEntry), bEntry, b.getLevel(bEntry), compareLevels) == 0) {
                    matches++;
                    if(matches == a.getEnchantments().size() || matches == b.getEnchantments().size()) { return matches; }
                }
            }
        }
        return matches;
    }

    public static boolean sameEnchantments(ItemEnchantmentsComponent a, ItemEnchantmentsComponent b, boolean compareLevels) {
        if (a.getEnchantments().size() != b.getEnchantments().size()) { return false; }
        return countMatches(a, b, compareLevels) == a.getEnchantments().size();
    }
}
