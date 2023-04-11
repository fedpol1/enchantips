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
        this.minColor = this.getDefaultMinColor();
        this.maxColor = this.getDefaultMaxColor();
        this.order = this.getDefaultOrder();
        this.showHighlight = this.getDefaultHighlightVisibility();
    }

    public Color getDefaultMinColor() {
        switch (((EnchantmentAccess)this.enchantment).enchantipsGetPriority()) {
            case CURSED -> { return new Color(0xbf0000); }
            case TREASURE -> { return new Color(0x009f00); }
            case NORMAL -> { return new Color(0x9f7f7f); }
        }
        return new Color(0x9f7f7f);
    }

    public Color getDefaultMaxColor() {
        switch (((EnchantmentAccess)this.enchantment).enchantipsGetPriority()) {
            case CURSED -> { return new Color(0xff0000); }
            case TREASURE -> { return new Color(0x00df00); }
            case NORMAL -> { return new Color(0xffdfdf); }
        }
        return new Color(0xffdfdf);
    }

    public int getDefaultOrder() {
        return ((EnchantmentAccess)this.enchantment).enchantipsGetPriority().ordinal();
    }

    public boolean getDefaultHighlightVisibility() {
        return true;
    }
}
