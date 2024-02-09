package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class TooltipHelper {

    public static final String REPAIR_COST_TOOLTIP = EnchantipsClient.MODID + ".tooltip.repair_cost";
    public static final String ENCHANTABILITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.enchantability";
    public static final String RARITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.rarity";
    public static final String MODIFIED_LEVEL_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level";
    public static final String MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.for_enchantment";
    public static final String SCROLLABLE_TOOLTIP_END = EnchantipsClient.MODID + ".tooltip.scrollable.end";
    public static final String SCROLLABLE_TOOLTIP_START = EnchantipsClient.MODID + ".tooltip.scrollable.start";

    public static MutableText buildSwatch(int rgb) {
        return Text.translatable("enchantips.tooltip.swatch")
                .setStyle(Style.EMPTY
                        .withColor(rgb)
                        .withFont(EnchantipsClient.SYMBOL_FONT)
                );
    }

    public static MutableText buildRarity(int rarity, int numberColor) {
        MutableText rarityText = MutableText.of(new PlainTextContent.Literal(Integer.toString(rarity)));
        Style style = Style.EMPTY.withColor(numberColor);
        if(ModOption.SHOW_SWATCHES.getValue()) {
            style = Style.EMPTY.withColor(ModOption.DECORATION.getValue().getRGB());
        }
        rarityText.setStyle(style);
        return Text.translatable(RARITY_TOOLTIP, rarityText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.DECORATION.getValue().getRGB())));
    }

    public static MutableText buildModifiedLevel(int lower, int upper) {
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTMENT_LEVEL.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTMENT_LEVEL_VALUE.getValue().getRGB()),
                MODIFIED_LEVEL_TOOLTIP);
    }

    // is only responsible for generating the modified level interval prefix
    public static MutableText buildModifiedLevelForEnchantment(int lower, int upper) {
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTMENT_LEVEL.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTMENT_LEVEL_VALUE.getValue().getRGB()),
                MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP
        );
    }

    private static MutableText buildModifiedLevelGeneric(int lower, int upper, TextColor tooltipColor, TextColor valueColor, String translation) {
        MutableText lowerText = MutableText.of(new PlainTextContent.Literal(Integer.toString(lower)));
        MutableText upperText = MutableText.of(new PlainTextContent.Literal(Integer.toString(upper)));
        lowerText.setStyle(Style.EMPTY.withColor(valueColor));
        upperText.setStyle(Style.EMPTY.withColor(valueColor));
        return Text.translatable(translation, lowerText, upperText).setStyle(Style.EMPTY.withColor(tooltipColor));

    }

    public static MutableText buildEnchantability(int ench) {
        MutableText enchText = MutableText.of(new PlainTextContent.Literal(Integer.toString(ench)));
        enchText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.ENCHANTABILITY_VALUE.getValue().getRGB())));
        return Text.translatable(ENCHANTABILITY_TOOLTIP, enchText)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.ENCHANTABILITY.getValue().getRGB())));
    }
    public static MutableText buildRepairCost(int cost) {
        MutableText costText = MutableText.of(new PlainTextContent.Literal(Integer.toString(cost)));
        costText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.REPAIRCOST_VALUE.getValue().getRGB())));
        return Text.translatable(REPAIR_COST_TOOLTIP, costText)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.REPAIRCOST.getValue().getRGB())));
    }

    public static MutableText buildUnbreakable() {
        return Text.translatable("item.unbreakable")
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.UNBREAKABLE_COLOR.getValue().getRGB())));
    }

    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments, boolean modifyRarity) {
        ArrayList<EnchantmentLevel> enchantmentLevelData = EnchantmentLevel.ofList(enchantments);
        Collections.sort(enchantmentLevelData);
        for(EnchantmentLevel ench : enchantmentLevelData) {
            tooltip.add(EnchantmentAppearanceHelper.getName(ench, modifyRarity));
        }
    }
}
