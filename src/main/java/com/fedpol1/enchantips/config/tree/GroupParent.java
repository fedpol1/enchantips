package com.fedpol1.enchantips.config.tree;

import net.minecraft.enchantment.Enchantment;

public interface GroupParent {

    GroupNode addGroup(String name);
    EnchantmentGroupNode addGroup(Enchantment ench);
}
