package com.fedpol1.enchantips.accessor;

import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntryList;

public interface EnchantmentAccess {
    RegistryEntryList<Item> enchantips$getPrimaryItems();
    RegistryEntryList<Item> enchantips$getSecondaryItems();
}
