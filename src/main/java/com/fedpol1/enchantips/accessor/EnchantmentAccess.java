package com.fedpol1.enchantips.accessor;

import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;

public interface EnchantmentAccess {
    TagKey<Item> enchantipsGetPrimaryItems();
    TagKey<Item> enchantipsGetSecondaryItems();
}
