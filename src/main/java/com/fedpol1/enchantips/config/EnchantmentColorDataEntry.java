package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantmentAccess;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.TextColor;
import net.minecraft.util.registry.Registry;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class EnchantmentColorDataEntry implements Comparable<EnchantmentColorDataEntry> {

    public Enchantment enchantment;
    public String enchantmentKey;
    public TextColor minColor;
    public TextColor maxColor;
    public int order; // lower order = higher priority
    public boolean showHighlight;
    public boolean active;

    public EnchantmentColorDataEntry(Enchantment e) {
        this.enchantment = e;
        this.enchantmentKey = Objects.requireNonNull(Registry.ENCHANTMENT.getId(e)).toString();
        this.minColor = ((EnchantmentAccess)e).enchantipsGetColor(e.getMinLevel());
        this.maxColor = ((EnchantmentAccess)e).enchantipsGetColor(e.getMaxLevel());
        this.order = 0;
        this.showHighlight = true;
        this.active = false;
    }

    @Override
    public int compareTo(@NotNull EnchantmentColorDataEntry o) {
        return this.order - o.order;
    }
}
