package com.fedpol1.enchantips.accessor;

import net.minecraft.core.HolderSet;
import net.minecraft.world.item.Item;

public interface EnchantmentAccess {
    HolderSet<Item> enchantips$getPrimaryItems();
    HolderSet<Item> enchantips$getSecondaryItems();
}
