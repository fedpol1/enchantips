package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantmentAccess;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.awt.Color;
import java.util.Objects;

public class EnchantmentColorDataEntry {

    public Enchantment enchantment;
    public String enchantmentKey;
    public Color minColor;
    public Color maxColor;
    public int order; // lower order = higher priority
    public boolean showHighlight;

    public EnchantmentColorDataEntry(Enchantment e) {
        this.enchantment = e;
        this.enchantmentKey = Objects.requireNonNull(Registries.ENCHANTMENT.getId(e)).toString();
        this.minColor = ((EnchantmentAccess)e).enchantipsGetDefaultMinColor();
        this.maxColor = ((EnchantmentAccess)e).enchantipsGetDefaultMaxColor();
        this.order = ((EnchantmentAccess)e).enchantipsGetDefaultOrder();
        this.showHighlight = ((EnchantmentAccess)e).enchantipsGetDefaultHighlightVisibility();
    }
}
