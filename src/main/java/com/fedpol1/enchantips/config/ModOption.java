package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.tree.*;

public enum ModOption {

    SHOW_REPAIRCOST(new BooleanDataEntry("repair_cost", true, true), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_ENCHANTABILITY(new BooleanDataEntry("enchantability",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_ENCHANTABILITY_WHEN_ENCHANTED(new BooleanDataEntry("enchantability.when_enchanted",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_RARITY(new BooleanDataEntry("rarity",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_MODIFIED_ENCHANTMENT_LEVEL(new BooleanDataEntry("modified_level",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_EXTRA_ENCHANTMENTS(new BooleanDataEntry("extra_enchantments",true, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT(new BooleanDataEntry("modified_level.for_enchantment",false, false), ModCategory.TOOLTIP_TOGGLES.getNode()),
    REPAIRCOST(new ColorDataEntry("repair_cost", 0xffbf00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    REPAIRCOST_VALUE(new ColorDataEntry("repair_cost.value", 0xff7f00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    ENCHANTABILITY(new ColorDataEntry("enchantability", 0xffbf00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    ENCHANTABILITY_VALUE(new ColorDataEntry("enchantability.value", 0xff7f00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    RARITY_BRACKET(new ColorDataEntry("rarity.bracket", 0x3f3f3f, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_ENCHANTMENT_LEVEL(new ColorDataEntry("modified_level", 0xffbf00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_ENCHANTMENT_LEVEL_VALUE(new ColorDataEntry("modified_level.value", 0xff7f00, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_LEVEL_FOR_ENCHANTMENT(new ColorDataEntry("modified_level.for_enchantment", 0xdf9f3f, false), ModCategory.TOOLTIP_COLORS.getNode()),
    MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE(new ColorDataEntry("modified_level.for_enchantment.value", 0xdf7f3f, false), ModCategory.TOOLTIP_COLORS.getNode()),
    SHOW_HIGHLIGHTS(new BooleanDataEntry("show",false, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHTS_RESPECT_HIDEFLAGS(new BooleanDataEntry("respect_hideflags",true, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHT_LIMIT(new IntegerDataEntry("limit", 4, 0, 16, 1, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHT_HOTBAR_ALPHA(new IntegerDataEntry("hotbar_alpha", 127, 0, 255, 0, false), ModCategory.HIGHLIGHTS.getNode()),
    HIGHLIGHT_TRADING_ALPHA(new IntegerDataEntry("trading_alpha", 127, 0, 255, 0, false), ModCategory.HIGHLIGHTS.getNode()),
    SHOW_PROTECTION_BAR(new BooleanDataEntry("show_protection_bar",false, false), ModCategory.MISCELLANEOUS.getNode()),
    SHOW_ANVIL_ITEM_SWAP_BUTTON(new BooleanDataEntry("show_anvil_swap_button",false, true), ModCategory.MISCELLANEOUS.getNode()),
    UNBREAKABLE_COLOR(new ColorDataEntry("unbreakable_color", 0x00dfff, false), ModCategory.MISCELLANEOUS.getNode());

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
