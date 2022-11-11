package com.fedpol1.enchantips;

import net.minecraft.text.Text;

public interface EnchantmentMixinAccess {
    Text enchantipsGetName(int level, boolean modifyRarity);
}
