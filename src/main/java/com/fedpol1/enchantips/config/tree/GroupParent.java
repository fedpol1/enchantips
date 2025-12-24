package com.fedpol1.enchantips.config.tree;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public interface GroupParent {

    GroupNode addGroup(String name);
    EnchantmentGroupNode addEnchantmentGroup(ResourceKey<Enchantment> ench);
    EnchantmentGroupNode addEnchantmentGroup(String ench);
}
