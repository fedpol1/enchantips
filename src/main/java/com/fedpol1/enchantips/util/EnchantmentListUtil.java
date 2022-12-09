package com.fedpol1.enchantips.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class EnchantmentListUtil {

    public static int compareEnchantments(NbtElement a, NbtElement b, boolean compareLevels) {
        int score = 0;
        int pass = 0;

        // if either a or b are not a compound, stop early
        if(a instanceof NbtCompound) { score++; pass++; }
        if(b instanceof NbtCompound) { score--; pass++; }
        if(pass < 2) { return score; }
        score = 0; pass = 0;

        NbtCompound compoundA = (NbtCompound)a;
        NbtCompound compoundB = (NbtCompound)b;

        // if either A or B do not contain a properly formatted enchantment, stop early
        if(compoundA.contains("id", NbtElement.STRING_TYPE) && compoundA.contains("lvl", NbtElement.NUMBER_TYPE)) { score++; pass++; }
        if(compoundB.contains("id", NbtElement.STRING_TYPE) && compoundB.contains("lvl", NbtElement.NUMBER_TYPE)) { score--; pass++; }
        if(pass < 2) { return score; }

        // compare enchantment IDs
        String enchA = compoundA.getString("id");
        String enchB = compoundB.getString("id");
        if(!enchA.contains(":")) { enchA = "minecraft:" + enchA; }
        if(!enchB.contains(":")) { enchB = "minecraft:" + enchB; }
        score = enchA.compareTo(enchB);
        if(score != 0 || !compareLevels) { return score; }

        // compare levels
        return compoundA.getInt("lvl") - compoundA.getInt("lvl");
    }

    public static int countMatches(NbtList a, NbtList b, boolean compareLevels) {
        int matches = 0;
        for(int j = 0; j < a.size(); j++) {
            for(int k = 0; k < b.size(); k++) {
                if(EnchantmentListUtil.compareEnchantments(a.get(j), b.get(k), compareLevels) == 0) {
                    matches++;
                    if(matches == a.size() || matches == b.size()) { return matches; }
                }
            }
        }
        return matches;
    }

    public static boolean sameEnchantments(NbtList a, NbtList b, boolean compareLevels) {
        if (a.size() != b.size()) { return false; }
        return countMatches(a, b, compareLevels) == a.size();
    }
}
