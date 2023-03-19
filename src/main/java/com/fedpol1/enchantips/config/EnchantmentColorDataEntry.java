package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantmentAccess;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.awt.Color;
import java.util.Objects;

public class EnchantmentColorDataEntry implements Comparable<EnchantmentColorDataEntry> {

    public Enchantment enchantment;
    public String enchantmentKey;
    public Color minColor;
    public Color maxColor;
    public int order; // lower order = higher priority
    public boolean showHighlight;

    public EnchantmentColorDataEntry(Enchantment e) {
        this.enchantment = e;
        this.enchantmentKey = Objects.requireNonNull(Registries.ENCHANTMENT.getId(e)).toString();
        this.minColor = this.getDefaultMinColor();
        this.maxColor = this.getDefaultMaxColor();
        this.order = this.getDefaultOrder();
        this.showHighlight = this.getDefaultHighlightVisibility();
    }

    public Color getDefaultMinColor() {
        switch (((EnchantmentAccess)this.enchantment).enchantipsGetPriority()) {
            case SPECIAL -> { return ModConfig.ENCHANTMENT_SPECIAL.getValue(); }
            case CURSED -> { return ModConfig.ENCHANTMENT_CURSE_MIN.getValue(); }
            case TREASURE -> { return ModConfig.ENCHANTMENT_TREASURE_MIN.getValue(); }
            case NORMAL -> { return ModConfig.ENCHANTMENT_NORMAL_MIN.getValue(); }
        }
        return ModConfig.ENCHANTMENT_NORMAL_MIN.getValue();
    }

    public Color getDefaultMaxColor() {
        switch (((EnchantmentAccess)this.enchantment).enchantipsGetPriority()) {
            case SPECIAL -> { return ModConfig.ENCHANTMENT_SPECIAL.getValue(); }
            case CURSED -> { return ModConfig.ENCHANTMENT_CURSE_MAX.getValue(); }
            case TREASURE -> { return ModConfig.ENCHANTMENT_TREASURE_MAX.getValue(); }
            case NORMAL -> { return ModConfig.ENCHANTMENT_NORMAL_MAX.getValue(); }
        }
        return ModConfig.ENCHANTMENT_NORMAL_MAX.getValue();
    }

    public int getDefaultOrder() {
        return ((EnchantmentAccess)this.enchantment).enchantipsGetPriority().ordinal();
    }

    public boolean getDefaultHighlightVisibility() {
        return true;
    }

    @Override
    public int compareTo(EnchantmentColorDataEntry o) {
        return this.order - o.order;
    }
}
