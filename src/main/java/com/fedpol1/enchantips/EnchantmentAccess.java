package com.fedpol1.enchantips;

import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;

public interface EnchantmentAccess {
    Text enchantipsGetName(int level, boolean modifyRarity);
    TextColor enchantipsGetColor(int level);
    float enchantipsGetIntensity(int level);
    Color enchantipsGetDefaultMinColor();
    Color enchantipsGetDefaultMaxColor();
    int enchantipsGetDefaultOrder();
    boolean enchantipsGetDefaultHighlightVisibility();
}
