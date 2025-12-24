package com.fedpol1.enchantips.util;

import net.minecraft.core.Holder;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public abstract class EnchantmentListHelper {

    public static int compareEnchantments(Holder<Enchantment> a, int aLevel, Holder<Enchantment> b, int bLevel, boolean compareLevels) {
        // compare enchantment IDs
        String enchA = a.getRegisteredName();
        String enchB = b.getRegisteredName();
        int score = enchA.compareTo(enchB);
        if(score != 0 || !compareLevels) { return score; }

        // compare levels
        return aLevel - bLevel;
    }

    public static int countMatches(ItemEnchantments a, ItemEnchantments b, boolean compareLevels) {
        int matches = 0;
        for(Holder<Enchantment> aEntry : a.keySet()) {
            for(Holder<Enchantment> bEntry : b.keySet()) {
                if(EnchantmentListHelper.compareEnchantments(aEntry, a.getLevel(aEntry), bEntry, b.getLevel(bEntry), compareLevels) == 0) {
                    matches++;
                    if(matches == a.keySet().size() || matches == b.keySet().size()) { return matches; }
                }
            }
        }
        return matches;
    }

    public static boolean sameEnchantments(ItemEnchantments a, ItemEnchantments b, boolean compareLevels) {
        if (a.keySet().size() != b.keySet().size()) { return false; }
        return countMatches(a, b, compareLevels) == a.keySet().size();
    }
}
