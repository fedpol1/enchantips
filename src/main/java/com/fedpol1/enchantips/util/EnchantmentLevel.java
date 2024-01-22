package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class EnchantmentLevel implements Comparable<EnchantmentLevel> {

    private final Enchantment enchantment;
    private final int level;

    private EnchantmentLevel(Enchantment e, int l) {
        this.enchantment = e;
        this.level = l;
    }

    public static EnchantmentLevel of(Enchantment e, int l) {
        return new EnchantmentLevel(e, l);
    }

    public static EnchantmentLevel of(NbtElement e) {
        if(!(e instanceof NbtCompound c)) { return null; }
        return of(c);
    }

    public static EnchantmentLevel of(NbtCompound c) {
        if(!c.contains("id", NbtElement.STRING_TYPE)) { return null; }
        if(!c.contains("lvl", NbtElement.NUMBER_TYPE)) { return null; }
        Enchantment ench = Registries.ENCHANTMENT.get(new Identifier(c.getString("id")));
        if(ench == null) { return null; }
        return new EnchantmentLevel(ench, c.getInt("lvl"));
    }

    public static ArrayList<EnchantmentLevel> ofList(NbtList l) {
        ArrayList<EnchantmentLevel> enchantments = new ArrayList<>();
        EnchantmentLevel enchantment;
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
        Enchantment ench = this.getEnchantment();
        EnchantmentGroupNode gn = ModConfigData.get(Objects.requireNonNull(ench));

        if(this.getLevel() > ench.getMaxLevel()) {
            return ((Color) ((OptionNode<?>) (gn.getOvermaxColor())).getValue());
        }

        // intensity 0-1; 0 indicates min level, 1 indicates max level; single-level enchantments are max
        float intensity = 1.0f - (float)(ench.getMaxLevel() - this.getLevel()) / Math.max(1.0f, ench.getMaxLevel() - ench.getMinLevel());
        intensity = Math.min(1.0f, Math.max(0.0f, intensity));

        int rgbMin = ((Color) ((OptionNode<?>) (gn.getMinColor())).getValue()).getRGB();
        int rgbMax = ((Color) ((OptionNode<?>) (gn.getMaxColor())).getValue()).getRGB();

        int r1 = (rgbMin & 0xff0000) >> 16;
        int r2 = (rgbMax & 0xff0000) >> 16;
        int g1 = (rgbMin & 0xff00) >> 8;
        int g2 = (rgbMax & 0xff00) >> 8;
        int b1 = (rgbMin & 0xff);
        int b2 = (rgbMax & 0xff);

        r1 += (r2-r1) * intensity;
        g1 += (g2-g1) * intensity;
        b1 += (b2-b1) * intensity;

        return new Color((r1<<16) + (g1<<8) + b1);
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

    public boolean isOvermax() {
        return this.level > enchantment.getMaxLevel();
    }

    public int compareTo(EnchantmentLevel other) {
        // potentially prioritize overlevelled enchantments
        if(ModOption.PRIORITIZE_OVERMAX_ENCHANTMENTS.getValue() && (this.isOvermax() ^ other.isOvermax())) {
            return this.isOvermax() ? -1 : 1;
        }

        // first compare order from enchantmentcolordata
        int comparison = ModConfigData.getEnchantmentOrder(this.getEnchantment()) - ModConfigData.getEnchantmentOrder(other.getEnchantment());
        if(comparison != 0) { return comparison; }
        // then compare translated name
        comparison = Text.translatable(this.getEnchantment().getTranslationKey()).getString().compareTo(Text.translatable(other.getEnchantment().getTranslationKey()).getString());
        if(comparison != 0) { return comparison; }
        // and finally level
        return this.getLevel() - other.getLevel();
    }
}
