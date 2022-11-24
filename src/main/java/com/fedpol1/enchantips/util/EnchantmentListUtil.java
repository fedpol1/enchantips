package com.fedpol1.enchantips.util;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;

public class EnchantmentListUtil {

    public static boolean sameEnchantments(NbtList a, NbtList b) {
        // error checking
        if(a.size() != b.size()) {
            return false;
        }
        for(int i = 0; i < a.size(); i++) {
            if(!(a.get(i) instanceof NbtCompound && b.get(i) instanceof NbtCompound)) {
                return false;
            }
            boolean aProper = ((NbtCompound)a.get(i)).contains("id") && ((NbtCompound)a.get(i)).contains("lvl");
            boolean bProper = ((NbtCompound)b.get(i)).contains("id") && ((NbtCompound)b.get(i)).contains("lvl");
            if(!(aProper && bProper)) {
                return false;
            }
        }
        // for every enchantment in a, try to find a matching enchantment in b
        boolean found;
        try {
            for (NbtElement ae : a) {
                found = false;
                String aEnchantment = ((NbtCompound) ae).getString("id");
                int aLevel = ((NbtCompound) ae).getInt("lvl");
                for (NbtElement be : b) {
                    String bEnchantment = ((NbtCompound) be).getString("id");
                    int bLevel = ((NbtCompound) be).getInt("lvl");
                    if(aEnchantment.equals(bEnchantment) && aLevel == bLevel) {
                        found = true;
                    }
                }
                if (!found) {
                    return false;
                }
            }
        }
        catch (ClassCastException e) {
            return false;
        }
        return true;
    }
}
