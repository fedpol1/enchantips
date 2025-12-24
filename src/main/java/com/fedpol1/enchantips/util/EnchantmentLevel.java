package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;
import java.awt.*;
import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.Level;

public class EnchantmentLevel implements Comparable<EnchantmentLevel> {

    private final ResourceKey<Enchantment> key;
    private final int level;

    private EnchantmentLevel(ResourceKey<Enchantment> e, int l) {
        this.key = e;
        this.level = l;
    }

    public static EnchantmentLevel of(Enchantment e, int l) throws IllegalStateException {
        Level w = Minecraft.getInstance().level;
        if(w == null) { throw new IllegalStateException("Could not construct EnchantmentLevel: world is null."); }
        Optional<ResourceKey<Enchantment>> optional = w.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getResourceKey(e);
        if(optional.isEmpty()) {
            IntegratedServer s = Minecraft.getInstance().getSingleplayerServer();
            if(s == null) { throw new IllegalStateException("Could not construct EnchantmentLevel: client enchantment optional is empty & no integrated server exists."); }
            w = s.getLevel(w.dimension());
            if(w == null) { throw new IllegalStateException("Could not construct EnchantmentLevel: client enchantment optional is empty & server world is null."); }
            optional = w.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getResourceKey(e);
            if(optional.isEmpty()) { throw new IllegalStateException("Could not construct EnchantmentLevel: client & server enchantment optionals are both empty."); }
        }
        return new EnchantmentLevel(optional.get(), l);
    }

    public static ArrayList<EnchantmentLevel> ofList(ItemEnchantments component) {
        ArrayList<EnchantmentLevel> enchantments = new ArrayList<>();
        if(component == null) { return enchantments; }
        for(Holder<Enchantment> entry : component.keySet()) {
            Enchantment enchantment = entry.value();
            if(enchantment == null) { continue; }
            enchantments.add(EnchantmentLevel.of(enchantment, component.getLevel(entry)));
        }
        return enchantments;
    }

    public ResourceKey<Enchantment> getKey() {
        return this.key;
    }

    public Enchantment getEnchantment() {
        Level w = Minecraft.getInstance().level;
        if(w == null) { return null; }
        return w.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(this.key);
    }

    public int getLevel() {
        return this.level;
    }

    public Color getColor() {
        Enchantment ench = this.getEnchantment();
        EnchantmentGroupNode gn = ModConfigData.get(this.getKey());

        if(ench == null) { return (Color) ((OptionNode<?>) (gn.getMinColor())).getValue(); }

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

        r1 += (int) ((r2-r1) * intensity);
        g1 += (int) ((g2-g1) * intensity);
        b1 += (int) ((b2-b1) * intensity);

        return new Color((r1<<16) + (g1<<8) + b1);
    }

    public int getLowestModifiedLevel() {
        return this.getEnchantment().getMinCost(this.getLevel());
    }

    public int getHighestModifiedLevel() {
        if(this.getEnchantment().getMaxLevel() > this.getLevel()) {
            return Math.min(this.getEnchantment().getMaxCost(this.getLevel()), this.getEnchantment().getMinCost(this.getLevel() + 1) - 1);
        }
        return this.getEnchantment().getMaxCost(this.getLevel());
    }

    public boolean isOvermax() {
        return this.level > this.getEnchantment().getMaxLevel();
    }

    public int compareTo(EnchantmentLevel other) {
        // potentially prioritize overlevelled enchantments
        if(ModOption.PRIORITIZE_OVERLEVELLED_ENCHANTMENTS.getValue() && (this.isOvermax() ^ other.isOvermax())) {
            return this.isOvermax() ? -1 : 1;
        }

        // first compare order from enchantmentcolordata
        int comparison = ModConfigData.getEnchantmentOrder(this.getKey()) - ModConfigData.getEnchantmentOrder(other.getKey());
        if(comparison != 0) { return comparison; }
        // then compare translated name
        comparison = this.getEnchantment().description().getString().compareTo(other.getEnchantment().description().getString());
        if(comparison != 0) { return comparison; }
        // and finally level
        return this.getLevel() - other.getLevel();
    }
}
