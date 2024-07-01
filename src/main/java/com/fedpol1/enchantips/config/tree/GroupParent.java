package com.fedpol1.enchantips.config.tree;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

public interface GroupParent {

    GroupNode addGroup(String name);
    EnchantmentGroupNode addEnchantmentGroup(RegistryKey<Enchantment> ench);
    EnchantmentGroupNode addEnchantmentGroup(String ench);
}
