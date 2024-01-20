package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.gui.YACLScreen;

import java.awt.Color;
import java.util.function.BiConsumer;

public class ModOption<T> {

    public static final ModOption<Boolean> SHOW_REPAIRCOST = ModCategory.TOOLTIP_TOGGLES.addOption(new BooleanOption(true), "repair_cost", 1);
    public static final ModOption<Boolean> SHOW_ENCHANTABILITY = ModCategory.TOOLTIP_TOGGLES.addOption(new BooleanOption(true), "enchantability", 0);
    public static final ModOption<Boolean> SHOW_ENCHANTABILITY_WHEN_ENCHANTED = ModCategory.TOOLTIP_TOGGLES.addOption(new BooleanOption(true), "enchantability.when_enchanted", 0);
    public static final ModOption<Boolean> SHOW_RARITY = ModCategory.TOOLTIP_TOGGLES.addOption(new BooleanOption(false), "rarity", 2);
    public static final ModOption<Boolean> SHOW_MODIFIED_ENCHANTMENT_LEVEL = ModCategory.TOOLTIP_TOGGLES.addOption(new BooleanOption(true), "modified_level", 2);
    public static final ModOption<Boolean> SHOW_EXTRA_ENCHANTMENTS = ModCategory.TOOLTIP_TOGGLES.addOption(new BooleanOption(true), "extra_enchantments", 1);
    public static final ModOption<Color> REPAIRCOST = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0xbf3f00), "repair_cost", 0);
    public static final ModOption<Color> REPAIRCOST_VALUE = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0xdf1f1f), "repair_cost.value", 0);
    public static final ModOption<Color> ENCHANTABILITY = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0xbf007f), "enchantability", 0);
    public static final ModOption<Color> ENCHANTABILITY_VALUE = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0xff1f9f), "enchantability.value", 0);
    public static final ModOption<Color> DECORATION = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0x4f4f47), "decoration", 1);
    public static final ModOption<Color> MODIFIED_ENCHANTMENT_LEVEL = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0xdf9f3f), "modified_level", 0);
    public static final ModOption<Color> MODIFIED_ENCHANTMENT_LEVEL_VALUE = ModCategory.TOOLTIP_COLORS.addOption(new ColorOption(0xdf7f3f), "modified_level.value", 0);
    public static final ModOption<Boolean> SHOW_HIGHLIGHTS = ModCategory.HIGHLIGHTS.addOption(new BooleanOption(false), "show", 0);
    public static final ModOption<Boolean> HIGHLIGHTS_RESPECT_HIDEFLAGS = ModCategory.HIGHLIGHTS.addOption(new BooleanOption(true), "respect_hideflags", 0);
    public static final ModOption<Integer> HIGHLIGHT_LIMIT = ModCategory.HIGHLIGHTS.addOption(new IntegerOption(4, 0, 16, 1), "limit", 0);
    public static final ModOption<Integer> HIGHLIGHT_HOTBAR_ALPHA = ModCategory.HIGHLIGHTS.addOption(new IntegerOption(127, 0, 255, 0), "hotbar_alpha", 0);
    public static final ModOption<Integer> HIGHLIGHT_TRADING_ALPHA = ModCategory.HIGHLIGHTS.addOption(new IntegerOption(127, 0, 255, 0), "trading_alpha", 0);
    public static final ModOption<Boolean> SHOW_PROTECTION_BAR = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(false), "show_protection_bar", 0);
    public static final ModOption<Boolean> SHOW_ANVIL_ITEM_SWAP_BUTTON = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(false), "show_anvil_swap_button", 3);
    public static final ModOption<Boolean> SHOW_ANVIL_WARNING = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(true), "show_anvil_warning", 1);
    public static final ModOption<Color> UNBREAKABLE_COLOR = ModCategory.MISCELLANEOUS.addOption(new ColorOption(0x00dfff), "unbreakable_color", 0);
    public static final ModOption<Boolean> HIGHLIGHT_UNBREAKABLE = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(true), "highlight_unbreakable", 0);
    public static final ModOption<Boolean> PRIORITIZE_OVERMAX_ENCHANTMENTS = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(true), "prioritize_overmax_enchantments", 0);

    private final Data<T> data;
    private final String key;
    private final int tooltipLines;

    public ModOption(Data<T> data, String key, int tooltipLines) {
        this.data = data;
        this.key = key;
        this.tooltipLines = tooltipLines;
    }

    public Data<T> getData() {
        return this.data;
    }

    public int getNumTooltipLines() {
        return this.tooltipLines;
    }

    public String getKey() {
        return this.key;
    }

    public T getValue() {
        return this.data.getValue();
    }

    public static void init() {}
}
