package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.gui.YACLScreen;

import java.awt.Color;
import java.util.function.BiConsumer;

public class ModOption<T> {

    public static final ModOption<Boolean> SWATCHES_SWITCH = ModCategory.PER_ENCHANTMENT_SWATCHES.addOption(new BooleanOption(false), "switch", 2);
    public static final ModOption<Color> SWATCHES_FALLBACK_COLOR = ModCategory.PER_ENCHANTMENT_SWATCHES.addOption(new ColorOption(0xaaaaaa), "fallback_color", 1);
    public static final ModOption<Boolean> GLINT_OVERRIDE_SWITCH = ModCategory.PER_ITEMSTACK_GLINT_OVERRIDE.addOption(new BooleanOption(false), "switch", 1);
    public static final ModOption<Color> GLINT_OVERRIDE_COLOR = ModCategory.PER_ITEMSTACK_GLINT_OVERRIDE.addOption(new ColorOption(0xbf3f00), "color", 0);
    public static final ModOption<Boolean> REPAIR_COST_SWITCH = ModCategory.PER_ITEMSTACK_REPAIR_COST.addOption(new BooleanOption(true), "switch", 2);
    public static final ModOption<Color> REPAIR_COST_COLOR = ModCategory.PER_ITEMSTACK_REPAIR_COST.addOption(new ColorOption(0xbf3f00), "color", 0);
    public static final ModOption<Color> REPAIR_COST_VALUE_COLOR = ModCategory.PER_ITEMSTACK_REPAIR_COST.addOption(new ColorOption(0xdf1f1f), "color.value", 0);
    public static final ModOption<Boolean> ENCHANTABILITY_SWITCH = ModCategory.PER_ITEMSTACK_ENCHANTABILITY.addOption(new BooleanOption(true), "switch", 1);
    public static final ModOption<Boolean> ENCHANTABILITY_SWITCH_WHEN_ENCHANTED = ModCategory.PER_ITEMSTACK_ENCHANTABILITY.addOption(new BooleanOption(false), "switch.when_enchanted", 1);
    public static final ModOption<Boolean> ENCHANTABILITY_DIVISION = ModCategory.PER_ITEMSTACK_ENCHANTABILITY.addOption(new BooleanOption(false), "division", 1);
    public static final ModOption<Color> ENCHANTABILITY_COLOR = ModCategory.PER_ITEMSTACK_ENCHANTABILITY.addOption(new ColorOption(0xbf007f), "color", 0);
    public static final ModOption<Color> ENCHANTABILITY_VALUE_COLOR = ModCategory.PER_ITEMSTACK_ENCHANTABILITY.addOption(new ColorOption(0xff1f9f), "color.value", 0);
    public static final ModOption<Boolean> HIGHLIGHTS_SWITCH = ModCategory.PER_ITEMSTACK_HIGHLIGHTS.addOption(new BooleanOption(false), "switch", 1);
    public static final ModOption<Boolean> HIGHLIGHTS_SWITCH_OVERRIDE = ModCategory.PER_ITEMSTACK_HIGHLIGHTS.addOption(new BooleanOption(false), "switch.override", 1);
    public static final ModOption<Integer> HIGHLIGHTS_LIMIT = ModCategory.PER_ITEMSTACK_HIGHLIGHTS.addOption(new IntegerOption(4, 0, 16, 1), "limit", 1);
    public static final ModOption<Integer> HIGHLIGHTS_ALPHA_HOTBAR = ModCategory.PER_ITEMSTACK_HIGHLIGHTS.addOption(new IntegerOption(127, 0, 255, 0), "alpha.hotbar", 2);
    public static final ModOption<Integer> HIGHLIGHTS_ALPHA_TRADING = ModCategory.PER_ITEMSTACK_HIGHLIGHTS.addOption(new IntegerOption(127, 0, 255, 0), "alpha.trading", 2);
    public static final ModOption<Boolean> ANVIL_COST_SWITCH = ModCategory.PER_ENCHANTMENT_ANVIL_COST.addOption(new BooleanOption(false), "switch", 3);
    public static final ModOption<Boolean> WEIGHT_SWITCH = ModCategory.PER_ENCHANTMENT_WEIGHT.addOption(new BooleanOption(false), "switch", 1);
    public static final ModOption<Boolean> MAXIMUM_ENCHANTMENT_LEVEL_SWITCH = ModCategory.PER_ENCHANTMENT_MAXIMUM_LEVEL.addOption(new BooleanOption(false), "switch", 1);
    public static final ModOption<Boolean> MAXIMUM_ENCHANTMENT_LEVEL_SWITCH_AT_MAX = ModCategory.PER_ENCHANTMENT_MAXIMUM_LEVEL.addOption(new BooleanOption(true), "switch.at_max", 0);
    public static final ModOption<Boolean> MAXIMUM_ENCHANTMENT_LEVEL_SWITCH_OVER_MAX = ModCategory.PER_ENCHANTMENT_MAXIMUM_LEVEL.addOption(new BooleanOption(true), "switch.over_max", 0);
    public static final ModOption<Boolean> ENCHANTMENT_TARGETS_SWITCH = ModCategory.PER_ENCHANTMENT_TARGETS.addOption(new BooleanOption(false), "switch", 2);
    public static final ModOption<Integer> ENCHANTMENT_TARGETS_LIMIT = ModCategory.PER_ENCHANTMENT_TARGETS.addOption(new IntegerOption(11, 1, 256, 0), "limit", 2);
    public static final ModOption<Boolean> ENCHANTMENT_TAGS_SWITCH = ModCategory.PER_ENCHANTMENT_TAGS.addOption(new BooleanOption(false), "switch", 1);
    public static final ModOption<Integer> ENCHANTMENT_TAGS_LIMIT = ModCategory.PER_ENCHANTMENT_TAGS.addOption(new IntegerOption(5, 1, 256, 0), "limit", 1);
    public static final ModOption<Color> ENCHANTING_HELPER_COLOR = ModCategory.ENCHANTING_HELPERS.addOption(new ColorOption(0xdf9f3f), "color", 0);
    public static final ModOption<Color> ENCHANTING_HELPER_VALUE_COLOR = ModCategory.ENCHANTING_HELPERS.addOption(new ColorOption(0xdf7f3f), "color.value", 0);
    public static final ModOption<Boolean> ENCHANTING_POWER_SWITCH = ModCategory.ENCHANTING_HELPERS.addOption(new BooleanOption(true), "enchanting_power.switch", 2);
    public static final ModOption<Boolean> ENCHANTING_CONTEXT_SWITCH = ModCategory.ENCHANTING_HELPERS.addOption(new BooleanOption(false), "enchanting_context.switch", 2);
    public static final ModOption<Boolean> ENCHANTMENT_SEED_SWITCH = ModCategory.ENCHANTING_HELPERS.addOption(new BooleanOption(false), "enchantment_seed.switch", 3);
    public static final ModOption<Boolean> EXTRA_ENCHANTMENTS_SWITCH = ModCategory.ENCHANTING_HELPERS.addOption(new BooleanOption(true), "extra_enchantments.switch", 3);
    public static final ModOption<Integer> EXTRA_ENCHANTMENTS_LIMIT = ModCategory.ENCHANTING_HELPERS.addOption(new IntegerOption(7, 1, 256, 0), "extra_enchantments.limit", 1);
    public static final ModOption<Color> DECORATION = ModCategory.MISCELLANEOUS.addOption(new ColorOption(0x4f4f47), "decoration", 1);
    public static final ModOption<Boolean> ANVIL_SWAP_BUTTON_SWITCH = ModCategory.MISCELLANEOUS_WIDGETS.addOption(new BooleanOption(false), "anvil_swap_button.switch", 2);
    public static final ModOption<Boolean> ANVIL_SWAP_WARNING_SWITCH = ModCategory.MISCELLANEOUS_WIDGETS.addOption(new BooleanOption(true), "anvil_swap_warning.switch", 1);
    public static final ModOption<Color> UNBREAKABLE_COLOR = ModCategory.MISCELLANEOUS_UNBREAKABLE.addOption(new ColorOption(0x00dfff), "color", 1);
    public static final ModOption<Boolean> UNBREAKABLE_HIGHLIGHT = ModCategory.MISCELLANEOUS_UNBREAKABLE.addOption(new BooleanOption(true), "highlight", 1);
    public static final ModOption<Boolean> PRIORITIZE_OVERLEVELLED_ENCHANTMENTS = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(true), "prioritize_overmax_enchantments", 1);
    public static final ModOption<Boolean> COMPACT_CONFIGURATION = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(false), "compact_configuration", 2);
    public static final ModOption<Boolean> GLOBAL_NAME_MODIFICATION = ModCategory.MISCELLANEOUS.addOption(new BooleanOption(false), "global_name_modification", 2);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> PRUNE_ENCHANTMENTS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                yaclScreen.config.saveFunction();
                ModConfig.deregisterUnusedEnchantmentConfig();
                ModConfig.writeConfig();
            }
    ), "prune_enchantments", 1);
    public static final ModOption<Color> ACTION_COLOR = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ColorOption(0x000000), "action_color", 2);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> SET_MIN_COLORS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                yaclScreen.config.saveFunction();
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setMinColor(ModOption.ACTION_COLOR.getValue());
                });
                ModConfig.writeConfig();
            }
    ), "set_min_colors", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> SET_MAX_COLORS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                yaclScreen.config.saveFunction();
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setMaxColor(ModOption.ACTION_COLOR.getValue());
                });
                ModConfig.writeConfig();
            }
    ), "set_max_colors", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> SET_OVERMAX_COLORS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                yaclScreen.config.saveFunction();
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setOvermaxColor(ModOption.ACTION_COLOR.getValue());
                });
                ModConfig.writeConfig();
            }
    ), "set_overmax_colors", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> COPY_MAX_COLORS_TO_OVERMAX_COLORS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).copyMaxColorToOvermaxColor();
                });
                ModConfig.writeConfig();
            }
    ), "copy_max_colors_to_overmax_colors", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> RESET_MIN_COLORS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setMinColor(null);
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setMaxColor(null);
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setOvermaxColor(null);
                });
                ModConfig.writeConfig();
            }
    ), "reset_colors", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> RESET_ORDERS = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setOrder(null);
                });
                ModConfig.writeConfig();
            }
    ), "reset_orders", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> SET_ORDERS_TO_0 = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                ModOption.PRIORITIZE_OVERLEVELLED_ENCHANTMENTS.getData().setValue(false);
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setOrder(0);
                });
                ModConfig.writeConfig();
            }
    ), "set_orders_to_0", 1);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> SET_HIGHLIGHTS_TRUE = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setHighlight(true);
                });
                ModConfig.writeConfig();
            }
    ), "set_highlights_true", 2);
    public static final ModOption<BiConsumer<YACLScreen, ButtonOption>> SET_HIGHLIGHTS_FALSE = ModCategory.MISCELLANEOUS_BULK_UPDATE.addOption(new ActionOption(
            (yaclScreen, buttonOption) -> {
                ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren().forEach(nodeEntry -> {
                    ((EnchantmentGroupNode)nodeEntry.getValue()).setHighlight(false);
                });
                ModConfig.writeConfig();
            }
    ), "set_highlights_false", 2);

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
