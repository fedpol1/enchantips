package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public abstract class EnchantmentListHelper {

    public static int compareEnchantments(Object2IntMap.Entry<RegistryEntry<Enchantment>> a, Object2IntMap.Entry<RegistryEntry<Enchantment>> b, boolean compareLevels) {
        // compare enchantment IDs
        String enchA = a.getKey().getIdAsString();
        String enchB = b.getKey().getIdAsString();
        int score = enchA.compareTo(enchB);
        if(score != 0 || !compareLevels) { return score; }

        // compare levels
        return a.getIntValue() - b.getIntValue();
    }

    public static int countMatches(ItemEnchantmentsComponent a, ItemEnchantmentsComponent b, boolean compareLevels) {
        int matches = 0;
        for(Object2IntMap.Entry<RegistryEntry<Enchantment>> aEntry : a.getEnchantmentsMap()) {
            for(Object2IntMap.Entry<RegistryEntry<Enchantment>> bEntry : b.getEnchantmentsMap()) {
                if(EnchantmentListHelper.compareEnchantments(aEntry, bEntry, compareLevels) == 0) {
                    matches++;
                    if(matches == a.getEnchantmentsMap().size() || matches == b.getEnchantmentsMap().size()) { return matches; }
                }
            }
        }
        return matches;
    }

    public static boolean sameEnchantments(ItemEnchantmentsComponent a, ItemEnchantmentsComponent b, boolean compareLevels) {
        if (a.getEnchantmentsMap().size() != b.getEnchantmentsMap().size()) { return false; }
        return countMatches(a, b, compareLevels) == a.getEnchantmentsMap().size();
    }
}
