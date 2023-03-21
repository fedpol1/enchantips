package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TooltipBuilder {

    public static final String REPAIR_COST_TOOLTIP = EnchantipsClient.MODID + ".tooltip.repair_cost";
    public static final String ENCHANTABILITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.enchantability";
    public static final String RARITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.rarity";
    public static final String MODIFIED_LEVEL_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level";
    public static final String MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.for_enchantment";

    public static MutableText buildRarity(int rarity, TextColor numberColor) {
        MutableText rarityText = MutableText.of(new LiteralTextContent(Integer.toString(rarity)));
        rarityText.setStyle(Style.EMPTY.withColor(numberColor));
        return Text.translatable(RARITY_TOOLTIP, rarityText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.RARITY_BRACKET.getValue().getRGB())));
    }

    public static MutableText buildModifiedLevel(int lower, int upper) {
        return buildModifiedLevelGeneric(lower, upper, TextColor.fromRgb(ModConfig.MODIFIED_ENCHANTMENT_LEVEL.getValue().getRGB()), TextColor.fromRgb(ModConfig.MODIFIED_ENCHANTMENT_LEVEL_VALUE.getValue().getRGB()), MODIFIED_LEVEL_TOOLTIP);
    }

    // is only responsible for generating the modified level interval prefix
    public static MutableText buildModifiedLevelForEnchantment(int lower, int upper) {
        return buildModifiedLevelGeneric(lower, upper, TextColor.fromRgb(ModConfig.MODIFIED_LEVEL_FOR_ENCHANTMENT.getValue().getRGB()), TextColor.fromRgb(ModConfig.MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE.getValue().getRGB()), MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP);
    }

    private static MutableText buildModifiedLevelGeneric(int lower, int upper, TextColor tooltipColor, TextColor valueColor, String translation) {
        MutableText lowerText = MutableText.of(new LiteralTextContent(Integer.toString(lower)));
        MutableText upperText = MutableText.of(new LiteralTextContent(Integer.toString(upper)));
        lowerText.setStyle(Style.EMPTY.withColor(valueColor));
        upperText.setStyle(Style.EMPTY.withColor(valueColor));
        return Text.translatable(translation, lowerText, upperText).setStyle(Style.EMPTY.withColor(tooltipColor));

    }

    public static MutableText buildEnchantability(int ench) {
        MutableText enchText = MutableText.of(new LiteralTextContent(Integer.toString(ench)));
        enchText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.ENCHANTABILITY_VALUE.getValue().getRGB())));
        return Text.translatable(ENCHANTABILITY_TOOLTIP, enchText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.ENCHANTABILITY.getValue().getRGB())));
    }
    public static MutableText buildRepairCost(int cost) {
        MutableText costText = MutableText.of(new LiteralTextContent(Integer.toString(cost)));
        costText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.REPAIRCOST_VALUE.getValue().getRGB())));
        return Text.translatable(REPAIR_COST_TOOLTIP, costText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.REPAIRCOST.getValue().getRGB())));
    }

    public static MutableText buildUnbreakable() {
        return Text.translatable("item.unbreakable").setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModConfig.UNBREAKABLE_COLOR.getValue().getRGB())));
    }

    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments, boolean modifyRarity) {
        ArrayList<EnchantmentLevelData> enchantmentLevelData = EnchantmentLevelData.ofList(enchantments);
        Collections.sort(enchantmentLevelData);
        for(EnchantmentLevelData ench : enchantmentLevelData) {
            tooltip.add(((EnchantmentAccess)ench.getEnchantment()).enchantipsGetName(ench.getLevel(), modifyRarity));
        }
    }
}
