package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.resources.Symbols;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.network.chat.contents.PlainTextContents;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.Identifier;

public abstract class TooltipHelper {

    public static final String FORCED_GLINT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.glint_override";
    public static final String REPAIR_COST_TOOLTIP = EnchantipsClient.MODID + ".tooltip.repair_cost";
    public static final String ENCHANTABILITY_TOOLTIP = EnchantipsClient.MODID + ".tooltip.enchantability";
    public static final String MODIFIED_LEVEL_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level";
    public static final String MODIFIED_LEVEL_SINGLE_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.single";
    public static final String MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.for_enchantment";
    public static final String MODIFIED_LEVEL_FOR_ENCHANTMENT_SINGLE_TOOLTIP = EnchantipsClient.MODID + ".tooltip.modified_level.for_enchantment.single";
    public static final String SCROLLABLE_TOOLTIP_END = EnchantipsClient.MODID + ".tooltip.scrollable.end";
    public static final String SCROLLABLE_TOOLTIP_START = EnchantipsClient.MODID + ".tooltip.scrollable.start";
    public static final Identifier ANVIL_COST_SYMBOL = Identifier.fromNamespaceAndPath(EnchantipsClient.MODID, Symbols.DIRECTORY + "/tooltip/anvil_cost");
    public static final Identifier WEIGHT_SYMBOL = Identifier.fromNamespaceAndPath(EnchantipsClient.MODID, Symbols.DIRECTORY + "/tooltip/weight");

    public static MutableComponent buildSymbolledNumber(Identifier symbol, int value, int numberColor) {
        Style style = Style.EMPTY.withColor(numberColor);
        if(ModOption.SWATCHES_SWITCH.getValue()) {
            style = Style.EMPTY.withColor(ModOption.SWATCHES_FALLBACK_COLOR.getValue().getRGB());
        }
        return Component.object(new AtlasSprite(Identifier.parse("gui"), symbol)).setStyle(style)
                .append(Component.literal(" ").withStyle(Style.EMPTY.withFont(Symbols.FONT)))
                .append(Component.literal(Integer.toString(value)));
    }

    public static MutableComponent buildModifiedLevel(int lower, int upper) {
        String translation = lower == upper ? MODIFIED_LEVEL_SINGLE_TOOLTIP : MODIFIED_LEVEL_TOOLTIP;
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_COLOR.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR.getValue().getRGB()),
                translation);
    }

    // is only responsible for generating the modified level interval suffix
    public static MutableComponent buildModifiedLevelForEnchantment(int lower, int upper) {
        String translation = lower == upper ? MODIFIED_LEVEL_FOR_ENCHANTMENT_SINGLE_TOOLTIP : MODIFIED_LEVEL_FOR_ENCHANTMENT_TOOLTIP;
        return buildModifiedLevelGeneric(
                lower,
                upper,
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_COLOR.getValue().getRGB()),
                TextColor.fromRgb(ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR.getValue().getRGB()),
                translation
        );
    }

    // surprisingly, works even with translations with only one placeholder
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
        MutableComponent enchText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(
                ModOption.ENCHANTABILITY_DIVISION.getValue() ? ench / 4 : ench
        )));
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
