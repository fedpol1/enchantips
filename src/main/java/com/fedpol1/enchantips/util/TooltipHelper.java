package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.text.*;

public abstract class TooltipHelper {

    public static final String FORCED_GLINT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.glint_override";
    public static final String REPAIR_COST_TOOLTIP = EnchantipsClient.MODID + ".tooltip.repair_cost";
    public static final String ENCHANTABILITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.enchantability";
    public static final String RARITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.anvil_cost";
    public static final String MODIFIED_LEVEL_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level";
    public static final String MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.for_enchantment";
    public static final String SCROLLABLE_TOOLTIP_END = EnchantipsClient.MODID + ".tooltip.scrollable.end";
    public static final String SCROLLABLE_TOOLTIP_START = EnchantipsClient.MODID + ".tooltip.scrollable.start";

    public static MutableText buildRarity(int rarity, int numberColor) {
        MutableText rarityText = MutableText.of(new PlainTextContent.Literal(Integer.toString(rarity)));
        Style style = Style.EMPTY.withColor(numberColor);
        if(ModOption.SWATCHES_SWITCH.getValue()) {
            style = Style.EMPTY.withColor(ModOption.DECORATION.getValue().getRGB());
        }
        rarityText.setStyle(style);
        return Text.translatable(RARITY_TOOLTIP, rarityText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.DECORATION.getValue().getRGB())));
    }

    public static MutableText buildModifiedLevel(int lower, int upper) {
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_COLOR.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR.getValue().getRGB()),
                MODIFIED_LEVEL_TOOLTIP);
    }

    // is only responsible for generating the modified level interval prefix
    public static MutableText buildModifiedLevelForEnchantment(int lower, int upper) {
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_COLOR.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR.getValue().getRGB()),
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

    public static MutableText buildForcedGlint(boolean b) {
        return Text.translatable(FORCED_GLINT_TOOLTIP + "." + b)
                .setStyle(Style.EMPTY.withColor(ModOption.GLINT_OVERRIDE_COLOR.getValue().getRGB()));
    }

    public static MutableText buildEnchantability(int ench) {
        MutableText enchText = MutableText.of(new PlainTextContent.Literal(Integer.toString(ench)));
        enchText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.ENCHANTABILITY_VALUE_COLOR.getValue().getRGB())));
        return Text.translatable(ENCHANTABILITY_TOOLTIP, enchText)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.ENCHANTABILITY_COLOR.getValue().getRGB())));
    }
    public static MutableText buildRepairCost(int cost) {
        MutableText costText = MutableText.of(new PlainTextContent.Literal(Integer.toString(cost)));
        costText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.REPAIR_COST_VALUE_COLOR.getValue().getRGB())));
        return Text.translatable(REPAIR_COST_TOOLTIP, costText)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.REPAIR_COST_COLOR.getValue().getRGB())));
    }

    public static MutableText buildUnbreakable() {
        return Text.translatable("item.unbreakable")
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.UNBREAKABLE_COLOR.getValue().getRGB())));
    }
}
