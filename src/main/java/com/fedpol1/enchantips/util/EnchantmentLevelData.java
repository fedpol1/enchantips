package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.EnchantmentColorDataEntry;
import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class EnchantmentLevelData implements Comparable<EnchantmentLevelData> {

    private final Enchantment enchantment;
    private final int level;

    private EnchantmentLevelData(Enchantment e, int l) {
        this.enchantment = e;
        this.level = l;
    }

    public static EnchantmentLevelData of(Enchantment e, int l) {
        return new EnchantmentLevelData(e, l);
    }

    public static EnchantmentLevelData of(NbtElement e) {
        if(!(e instanceof NbtCompound c)) { return null; }
        return of(c);
    }

    public static EnchantmentLevelData of(NbtCompound c) {
        if(!c.contains("id", NbtElement.STRING_TYPE)) { return null; }
        if(!c.contains("lvl", NbtElement.NUMBER_TYPE)) { return null; }
        Enchantment ench = Registries.ENCHANTMENT.get(new Identifier(c.getString("id")));
        if(ench == null) { return null; }
        return new EnchantmentLevelData(ench, c.getInt("lvl"));
    }

    public static ArrayList<EnchantmentLevelData> ofList(NbtList l) {
        ArrayList<EnchantmentLevelData> enchantments = new ArrayList<>();
        EnchantmentLevelData enchantment;
        for(NbtElement e : l) {
            enchantment = of(e);
            if(enchantment == null) { continue; }
            enchantments.add(enchantment);
        }
        return enchantments;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public int getLevel() {
        return this.level;
    }

    public Color getColor() {
        return new Color(((EnchantmentAccess)this.getEnchantment()).enchantipsGetColor(this.getLevel()).getRgb());
    }

    public EnchantmentColorDataEntry getDataEntry() {
        return ModConfig.individualColors.get(Objects.requireNonNull(Registries.ENCHANTMENT.getId(this.getEnchantment())).toString());
    }

    public int getLowestModifiedLevel() {
        return this.getEnchantment().getMinPower(this.getLevel());
    }

    public int getHighestModifiedLevel() {
        if(this.getEnchantment().getMaxLevel() > this.getLevel()) {
            return Math.min(this.getEnchantment().getMaxPower(this.getLevel()), this.getEnchantment().getMinPower(this.getLevel() + 1) - 1);
        }
        return this.getEnchantment().getMaxPower(this.getLevel());
    }

    public int compareTo(EnchantmentLevelData other) {
        // first compare order from enchantmentcolordata
        int comparison = this.getDataEntry().order - other.getDataEntry().order;
        if(comparison != 0) { return comparison; }
        // then compare translated name
        comparison = Text.translatable(this.getEnchantment().getTranslationKey()).getString().compareTo(Text.translatable(other.getEnchantment().getTranslationKey()).getString());
        if(comparison != 0) { return comparison; }
        // and finally level
        return this.getLevel() - other.getLevel();
    }
}
