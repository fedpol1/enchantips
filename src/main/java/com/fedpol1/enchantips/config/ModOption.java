package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.tree.*;

public enum ModOption {

    SHOW_REPAIRCOST(new BooleanDataEntry("show.repair_cost", true, true), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_ENCHANTABILITY(new BooleanDataEntry("show.enchantability",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_ENCHANTABILITY_WHEN_ENCHANTED(new BooleanDataEntry("show.enchantability.when_enchanted",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_RARITY(new BooleanDataEntry("show.rarity",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_MODIFIED_ENCHANTMENT_LEVEL(new BooleanDataEntry("show.modified_level",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_EXTRA_ENCHANTMENTS(new BooleanDataEntry("show.extra_enchantments",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT(new BooleanDataEntry("show.modified_level.for_enchantment",false, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    REPAIRCOST(new ColorDataEntry("color.repair_cost", 0xffbf00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    REPAIRCOST_VALUE(new ColorDataEntry("color.repair_cost.value", 0xff7f00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    ENCHANTABILITY(new ColorDataEntry("color.enchantability", 0xffbf00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    ENCHANTABILITY_VALUE(new ColorDataEntry("color.enchantability.value", 0xff7f00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    RARITY_BRACKET(new ColorDataEntry("color.rarity.bracket", 0x3f3f3f, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_ENCHANTMENT_LEVEL(new ColorDataEntry("color.modified_level", 0xffbf00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_ENCHANTMENT_LEVEL_VALUE(new ColorDataEntry("color.modified_level.value", 0xff7f00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_LEVEL_FOR_ENCHANTMENT(new ColorDataEntry("color.modified_level.for_enchantment", 0xdf9f3f, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE(new ColorDataEntry("color.modified_level.for_enchantment.value", 0xdf7f3f, false), ModCategory.TOOLTIP_COLORS.getNode()),
    SHOW_HIGHLIGHTS(new BooleanDataEntry("show.highlights",false, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHTS_RESPECT_HIDEFLAGS(new BooleanDataEntry("show.highlights.hideflags",true, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHT_LIMIT(new IntegerDataEntry("highlights.limit", 4, 0, 16, 1, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHT_HOTBAR_ALPHA(new IntegerDataEntry("highlights.hotbar_alpha", 127, 0, 255, 0, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHT_TRADING_ALPHA(new IntegerDataEntry("highlights.trading_alpha", 127, 0, 255, 0, false), ModCategory.HIGHLIGHTS.getNode()),
    SHOW_PROTECTION_BAR(new BooleanDataEntry("show.bar.protection",false, false), ModCategory.MISCELLANEOUS.getNode()),
    SHOW_ANVIL_ITEM_SWAP_BUTTON(new BooleanDataEntry("show.button.anvil_item_swap",false, true), ModCategory.MISCELLANEOUS.getNode()),
    UNBREAKABLE_COLOR(new ColorDataEntry("color.unbreakable", 0x00dfff, false), ModCategory.MISCELLANEOUS.getNode());

    private final AbstractDataEntry<?> entry;

    ModOption(AbstractDataEntry<?> entry, Node parent) {
        this.entry = entry;
        parent.addChild(new OptionNode<>(entry));
    }

    public Data<?> getData() {
        return this.entry.getData();
    }

    public static void init() {}
}
