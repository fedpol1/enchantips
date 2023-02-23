package com.fedpol1.enchantips;

import com.fedpol1.enchantips.util.EnchantmentPriority;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

public interface EnchantmentAccess {
    Text enchantipsGetName(int level, boolean modifyRarity);
    TextColor enchantipsGetColor(int level);
    float enchantipsGetIntensity(int level);
    EnchantmentPriority enchantipsGetPriority();
}
