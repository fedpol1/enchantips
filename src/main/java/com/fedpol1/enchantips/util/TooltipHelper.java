package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.PlainTextContents;

public abstract class TooltipHelper {

    public static final String FORCED_GLINT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.glint_override";
    public static final String REPAIR_COST_TOOLTIP = EnchantipsClient.MODID + ".tooltip.repair_cost";
    public static final String ENCHANTABILITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.enchantability";
    public static final String RARITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.anvil_cost";
    public static final String MODIFIED_LEVEL_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level";
    public static final String MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.for_enchantment";
    public static final String SCROLLABLE_TOOLTIP_END = EnchantipsClient.MODID + ".tooltip.scrollable.end";
    public static final String SCROLLABLE_TOOLTIP_START = EnchantipsClient.MODID + ".tooltip.scrollable.start";

    public static MutableComponent buildAnvilCost(int rarity, int numberColor) {
        MutableComponent anvilCostText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(rarity)));
        Style style = Style.EMPTY.withColor(numberColor);
        if(ModOption.SWATCHES_SWITCH.getValue()) {
            style = Style.EMPTY.withColor(ModOption.SWATCHES_FALLBACK_COLOR.getValue().getRGB());
        }
        anvilCostText.setStyle(style);
        return Component.translatable(RARITY_TOOLTIP, anvilCostText).setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.DECORATION.getValue().getRGB())));
    }

    public static MutableComponent buildModifiedLevel(int lower, int upper) {
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_COLOR.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR.getValue().getRGB()),
                MODIFIED_LEVEL_TOOLTIP);
    }

    // is only responsible for generating the modified level interval prefix
    public static MutableComponent buildModifiedLevelForEnchantment(int lower, int upper) {
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_COLOR.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR.getValue().getRGB()),
                MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP
        );
    }

    private static MutableComponent buildModifiedLevelGeneric(int lower, int upper, TextColor tooltipColor, TextColor valueColor, String translation) {
        MutableComponent lowerText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(lower)));
        MutableComponent upperText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(upper)));
        lowerText.setStyle(Style.EMPTY.withColor(valueColor));
        upperText.setStyle(Style.EMPTY.withColor(valueColor));
        return Component.translatable(translation, lowerText, upperText).setStyle(Style.EMPTY.withColor(tooltipColor));

    }

    public static MutableComponent buildForcedGlint(boolean b) {
        return Component.translatable(FORCED_GLINT_TOOLTIP + "." + b)
                .setStyle(Style.EMPTY.withColor(ModOption.GLINT_OVERRIDE_COLOR.getValue().getRGB()));
    }

    public static MutableComponent buildEnchantability(int ench) {
        MutableComponent enchText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(ench)));
        enchText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.ENCHANTABILITY_VALUE_COLOR.getValue().getRGB())));
        return Component.translatable(ENCHANTABILITY_TOOLTIP, enchText)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.ENCHANTABILITY_COLOR.getValue().getRGB())));
    }
    public static MutableComponent buildRepairCost(int cost) {
        MutableComponent costText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(cost)));
        costText.setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.REPAIR_COST_VALUE_COLOR.getValue().getRGB())));
        return Component.translatable(REPAIR_COST_TOOLTIP, costText)
                .setStyle(Style.EMPTY.withColor(TextColor.fromRgb(ModOption.REPAIR_COST_COLOR.getValue().getRGB())));
    }
}
