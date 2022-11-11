package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentMixinAccess;
import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.*;
import net.minecraft.util.registry.Registry;

import java.util.List;

public class TooltipBuilder {

    public static final String REPAIR_COST_TOOLTIP = EnchantipsClient.MODID + ".tooltip.repair_cost";
    public static final String ENCHANTABILITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.enchantability";
    public static final String RARITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.rarity";

    public static MutableText buildRarity(int rarity, TextColor numberColor) {
        MutableText rarityText = MutableText.of(new LiteralTextContent(Integer.toString(rarity)));
        rarityText.setStyle(Style.EMPTY.withColor(numberColor));
        return Text.translatable(RARITY_TOOLTIP, rarityText).setStyle(Style.EMPTY.withColor(ModConfig.RARITY_BRACKET.getColor()));
    }

    public static Text buildEnchantability(int ench) {
        MutableText enchText = MutableText.of(new LiteralTextContent(Integer.toString(ench)));
        enchText.setStyle(Style.EMPTY.withColor(ModConfig.ENCHANTABILITY_VALUE.getColor()));
        return Text.translatable(ENCHANTABILITY_TOOLTIP, enchText).setStyle(Style.EMPTY.withColor(ModConfig.ENCHANTABILITY.getColor()));
    }
    public static Text buildRepairCost(int cost) {
        MutableText costText = MutableText.of(new LiteralTextContent(Integer.toString(cost)));
        costText.setStyle(Style.EMPTY.withColor(ModConfig.REPAIRCOST_VALUE.getColor()));
        return Text.translatable(REPAIR_COST_TOOLTIP, costText).setStyle(Style.EMPTY.withColor(ModConfig.REPAIRCOST.getColor()));
    }

    // aaaaaaaa
    public static void appendEnchantmentsEbook(List<Text> tooltip, NbtList enchantments, boolean modifyRarity) {
        for(int i = 0; i < enchantments.size(); ++i) {
            NbtCompound nbtCompound = enchantments.getCompound(i);
            Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(nbtCompound)).ifPresent((e) -> {
                tooltip.add(((EnchantmentMixinAccess)e).enchantipsGetName(EnchantmentHelper.getLevelFromNbt(nbtCompound), modifyRarity));
            });
        }
    }
}
