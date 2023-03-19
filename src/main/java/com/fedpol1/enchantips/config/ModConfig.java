package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.util.ColorManager;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import dev.isxander.yacl.gui.controllers.ColorController;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static TreeMap<String, Data<?>> configData = new TreeMap<>();
    public static TreeMap<String, EnchantmentColorDataEntry> individualColors = new TreeMap<>();

    public static BooleanDataEntry SHOW_REPAIRCOST = new BooleanDataEntry("show.repair_cost", ModConfigCategory.TOOLTIP_TOGGLE, true, true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY = new BooleanDataEntry("show.enchantability", ModConfigCategory.TOOLTIP_TOGGLE, true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY_WHEN_ENCHANTED = new BooleanDataEntry("show.enchantability.when_enchanted", ModConfigCategory.TOOLTIP_TOGGLE, true);
    public static BooleanDataEntry SHOW_RARITY = new BooleanDataEntry("show.rarity", ModConfigCategory.TOOLTIP_TOGGLE, true);
    public static BooleanDataEntry SHOW_MODIFIED_ENCHANTMENT_LEVEL = new BooleanDataEntry("show.modified_level", ModConfigCategory.TOOLTIP_TOGGLE, true);
    public static BooleanDataEntry SHOW_EXTRA_ENCHANTMENTS = new BooleanDataEntry("show.extra_enchantments", ModConfigCategory.TOOLTIP_TOGGLE, true);
    public static BooleanDataEntry SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT = new BooleanDataEntry("show.modified_level.for_enchantment", ModConfigCategory.TOOLTIP_TOGGLE, false);
    public static BooleanDataEntry SHOW_PROTECTION_BAR = new BooleanDataEntry("show.bar.protection", ModConfigCategory.MISCELLANEOUS, false);
    public static BooleanDataEntry SHOW_ANVIL_ITEM_SWAP_BUTTON = new BooleanDataEntry("show.button.anvil_item_swap", ModConfigCategory.MISCELLANEOUS, false, true);
    public static BooleanDataEntry OVERRIDE_INDIVIDUAL_ENCHANTMENTS = new BooleanDataEntry("override.enchantments", ModConfigCategory.MISCELLANEOUS, true, true);
    public static BooleanDataEntry SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED = new BooleanDataEntry("show.highlights.special_enchantment", ModConfigCategory.SLOT_HIGHLIGHT, false, true);
    public static BooleanDataEntry HIGHLIGHTS_RESPECT_HIDEFLAGS = new BooleanDataEntry("show.highlights.hideflags", ModConfigCategory.SLOT_HIGHLIGHT, true, true);
    public static IntegerDataEntry HIGHLIGHT_LIMIT = new IntegerDataEntry("highlights.limit", ModConfigCategory.SLOT_HIGHLIGHT, 4, true);
    public static ColorDataEntry ENCHANTMENT_NORMAL_MIN = new ColorDataEntry("color.enchantment.normal.min", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0x7f7f7f);
    public static ColorDataEntry ENCHANTMENT_NORMAL_MAX = new ColorDataEntry("color.enchantment.normal.max", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0xdfdfdf);
    public static ColorDataEntry ENCHANTMENT_TREASURE_MIN = new ColorDataEntry("color.enchantment.treasure.min", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0x009f00);
    public static ColorDataEntry ENCHANTMENT_TREASURE_MAX = new ColorDataEntry("color.enchantment.treasure.max", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0x00df00);
    public static ColorDataEntry ENCHANTMENT_CURSE_MIN = new ColorDataEntry("color.enchantment.curse.min", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0xbf0000);
    public static ColorDataEntry ENCHANTMENT_CURSE_MAX = new ColorDataEntry("color.enchantment.curse.max", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0xff0000);
    public static ColorDataEntry ENCHANTMENT_SPECIAL = new ColorDataEntry("color.enchantment.special", ModConfigCategory.ENCHANTMENT_GROUP_COLOR, 0x00dfff);
    public static ColorDataEntry REPAIRCOST = new ColorDataEntry("color.repair_cost", ModConfigCategory.TOOLTIP_COLOR, 0xffbf00);
    public static ColorDataEntry REPAIRCOST_VALUE = new ColorDataEntry("color.repair_cost.value", ModConfigCategory.TOOLTIP_COLOR, 0xff7f00);
    public static ColorDataEntry ENCHANTABILITY = new ColorDataEntry("color.enchantability", ModConfigCategory.TOOLTIP_COLOR, 0xffbf00);
    public static ColorDataEntry ENCHANTABILITY_VALUE = new ColorDataEntry("color.enchantability.value", ModConfigCategory.TOOLTIP_COLOR, 0xff7f00);
    public static ColorDataEntry RARITY_BRACKET = new ColorDataEntry("color.rarity.bracket", ModConfigCategory.TOOLTIP_COLOR, 0x3f3f3f);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL = new ColorDataEntry("color.modified_level", ModConfigCategory.TOOLTIP_COLOR, 0xffbf00);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL_VALUE = new ColorDataEntry("color.modified_level.value", ModConfigCategory.TOOLTIP_COLOR, 0xff7f00);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT = new ColorDataEntry("color.modified_level.for_enchantment", ModConfigCategory.TOOLTIP_COLOR, 0xdf9f3f);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE = new ColorDataEntry("color.modified_level.for_enchantment.value", ModConfigCategory.TOOLTIP_COLOR, 0xdf7f3f);
    public static ColorDataEntry SLOT_HIGHLIGHT_FULL_MATCH = new ColorDataEntry("color.slot_highlight.full_match", ModConfigCategory.SLOT_HIGHLIGHT, 0x3fff3f);
    public static ColorDataEntry SLOT_HIGHLIGHT_PARTIAL_MATCH = new ColorDataEntry("color.slot_highlight.partial_match", ModConfigCategory.SLOT_HIGHLIGHT, 0xffbf3f);

    public static void registerConfig() throws NullPointerException {
        EnchantipsClient.LOGGER.info("Initializing configs");
        configData.put(SHOW_REPAIRCOST.getKey(), SHOW_REPAIRCOST.data);
        configData.put(SHOW_ENCHANTABILITY.getKey(), SHOW_ENCHANTABILITY.data);
        configData.put(SHOW_ENCHANTABILITY_WHEN_ENCHANTED.getKey(), SHOW_ENCHANTABILITY_WHEN_ENCHANTED.data);
        configData.put(SHOW_RARITY.getKey(), SHOW_RARITY.data);
        configData.put(SHOW_MODIFIED_ENCHANTMENT_LEVEL.getKey(), SHOW_MODIFIED_ENCHANTMENT_LEVEL.data);
        configData.put(SHOW_EXTRA_ENCHANTMENTS.getKey(), SHOW_EXTRA_ENCHANTMENTS.data);
        configData.put(SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT.getKey(), SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT.data);
        configData.put(SHOW_PROTECTION_BAR.getKey(), SHOW_PROTECTION_BAR.data);
        configData.put(SHOW_ANVIL_ITEM_SWAP_BUTTON.getKey(), SHOW_ANVIL_ITEM_SWAP_BUTTON.data);
        configData.put(OVERRIDE_INDIVIDUAL_ENCHANTMENTS.getKey(), OVERRIDE_INDIVIDUAL_ENCHANTMENTS.data);
        configData.put(HIGHLIGHT_LIMIT.getKey(), HIGHLIGHT_LIMIT.data);
        configData.put(SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.getKey(), SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.data);
        configData.put(HIGHLIGHTS_RESPECT_HIDEFLAGS.getKey(), HIGHLIGHTS_RESPECT_HIDEFLAGS.data);
        configData.put(ENCHANTMENT_NORMAL_MIN.getKey(), ENCHANTMENT_NORMAL_MIN.data);
        configData.put(ENCHANTMENT_NORMAL_MAX.getKey(), ENCHANTMENT_NORMAL_MAX.data);
        configData.put(ENCHANTMENT_TREASURE_MIN.getKey(), ENCHANTMENT_TREASURE_MIN.data);
        configData.put(ENCHANTMENT_TREASURE_MAX.getKey(), ENCHANTMENT_TREASURE_MAX.data);
        configData.put(ENCHANTMENT_CURSE_MIN.getKey(), ENCHANTMENT_CURSE_MIN.data);
        configData.put(ENCHANTMENT_CURSE_MAX.getKey(), ENCHANTMENT_CURSE_MAX.data);
        configData.put(ENCHANTMENT_SPECIAL.getKey(), ENCHANTMENT_SPECIAL.data);
        configData.put(REPAIRCOST.getKey(), REPAIRCOST.data);
        configData.put(REPAIRCOST_VALUE.getKey(), REPAIRCOST_VALUE.data);
        configData.put(ENCHANTABILITY.getKey(), ENCHANTABILITY.data);
        configData.put(ENCHANTABILITY_VALUE.getKey(), ENCHANTABILITY_VALUE.data);
        configData.put(RARITY_BRACKET.getKey(), RARITY_BRACKET.data);
        configData.put(MODIFIED_ENCHANTMENT_LEVEL.getKey(), MODIFIED_ENCHANTMENT_LEVEL.data);
        configData.put(MODIFIED_ENCHANTMENT_LEVEL_VALUE.getKey(), MODIFIED_ENCHANTMENT_LEVEL_VALUE.data);
        configData.put(MODIFIED_LEVEL_FOR_ENCHANTMENT.getKey(), MODIFIED_LEVEL_FOR_ENCHANTMENT.data);
        configData.put(MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE.getKey(), MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE.data);
        configData.put(SLOT_HIGHLIGHT_FULL_MATCH.getKey(), SLOT_HIGHLIGHT_FULL_MATCH.data);
        configData.put(SLOT_HIGHLIGHT_PARTIAL_MATCH.getKey(), SLOT_HIGHLIGHT_PARTIAL_MATCH.data);
        setPartialDefaultConfigEnchantments();
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        for(Map.Entry<String, Data<?>> item : configData.entrySet()) {
            configData.get(item.getKey()).setValueToDefault();
        }
        setPartialDefaultConfigEnchantments();
    }

    private static void setPartialDefaultConfigEnchantments() {
        for(Enchantment current : Registries.ENCHANTMENT) {
            individualColors.put(Objects.requireNonNull(Registries.ENCHANTMENT.getId(current)).toString(), new EnchantmentColorDataEntry(current));
        }
    }

    public static void readConfig() throws NullPointerException {
        try {
            ModConfig.CONFIG_FILE.createNewFile();
            Scanner sc = new Scanner(ModConfig.CONFIG_FILE);
            String line;
            String[] split;
            String state = "";
            while(sc.hasNextLine()) {
                line = sc.nextLine();
                if(line.matches("^- .*$")) {
                    state = line.substring(2, line.length()-1);
                    continue;
                }

                if(line.matches("^ {2}[^ ].*$")) {
                    split = line.substring(2).split(": ");
                    if (state.equals("individual_enchantment_setting")) {
                        EnchantmentColorDataEntry entry = Objects.requireNonNull(individualColors.get(line.substring(4, line.length() - 1)));
                        for (int i = 0; i < 4; i++) {
                            line = sc.nextLine();
                            split = line.substring(4).split(": ");
                            switch (split[0]) {
                                case "min_color" -> { entry.minColor = ColorManager.stringToColor(split[1]); }
                                case "max_color" -> { entry.maxColor = ColorManager.stringToColor(split[1]); }
                                case "order" -> { entry.order = Integer.parseInt(split[1]); }
                                case "show_highlight" -> { entry.showHighlight = Boolean.parseBoolean(split[1]); }
                            }
                        }
                        individualColors.put(entry.enchantmentKey, entry);
                    }
                    else {
                        try {
                            configData.get(split[0]).readStringValue(split[1]);
                        }
                        catch (NullPointerException e) {
                            EnchantipsClient.LOGGER.warn("Could not read config option " + split[0] + " with value " + split[1]);
                        }
                    }
                }
            }
        }
        catch (IOException | IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            EnchantipsClient.LOGGER.error("Could not read configuration file.\n" + e.getMessage());
            EnchantipsClient.LOGGER.info("Setting default configuration.");
            ModConfig.setDefaultConfig();
        }
    }

    public static void writeConfig() {
        if(ModConfig.OVERRIDE_INDIVIDUAL_ENCHANTMENTS.getValue()) {
            setPartialDefaultConfigEnchantments();
        }

        StringBuilder acc = new StringBuilder();

        TreeMap<ModConfigCategory, ArrayList<Data<?>>> data = new TreeMap<>();
        for(ModConfigCategory cat : ModConfigCategory.values()) { // populate map
            data.put(cat, new ArrayList<>());
        }
        for(Map.Entry<String, Data<?>> item : configData.entrySet()) { // populate arraylists in map
            data.get(item.getValue().getEntry().getCategory()).add(item.getValue());
        }

        for(Map.Entry<ModConfigCategory, ArrayList<Data<?>>> item : data.entrySet()) { // write general settings to file
            acc.append("- ").append(item.getKey().toString()).append(":\n");
            for(Data<?> d : item.getValue()) {
                acc.append("  ").append(d.getEntry().getKey()).append(": ").append(d.getStringValue()).append("\n");
            }
        }

        acc.append("- individual_enchantment_setting:\n");
        for(Map.Entry<String, EnchantmentColorDataEntry> item : individualColors.entrySet()) { // write specific enchantment settings to file
            acc.append("  - ").append(item.getKey()).append(":\n");
            acc.append("    min_color: ").append(ColorManager.colorToString(item.getValue().minColor)).append("\n");
            acc.append("    max_color: ").append(ColorManager.colorToString(item.getValue().maxColor)).append("\n");
            acc.append("    order: ").append(item.getValue().order).append("\n");
            acc.append("    show_highlight: ").append(item.getValue().showHighlight).append("\n");
        }

        try {
            FileWriter fw = new FileWriter(ModConfig.CONFIG_FILE);
            fw.write(acc.toString());
            fw.close();
        }
        catch (IOException e) {
            EnchantipsClient.LOGGER.error("Could not write configuration file.\n" + e.getMessage());
        }
    }

    public static Screen createGui(Screen parent) {
        ConfigCategory.Builder tooltipToggles =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.toggles_tooltips"));
        ConfigCategory.Builder enchantmentColors =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors_enchantments"));
        ConfigCategory.Builder tooltipColors =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors_tooltips"));
        ConfigCategory.Builder highlights =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.highlights"));
        ConfigCategory.Builder miscellaneous =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.miscellaneous"));
        ConfigCategory.Builder individualEnchantments =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantments"));

        OptionGroup.Builder individualEnchantmentColors = OptionGroup.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantments.colors"));
        OptionGroup.Builder individualEnchantmentMeta = OptionGroup.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantments.meta"));

        for(Map.Entry<String, Data<?>> item : ModConfig.configData.entrySet()) {
            Option<?> opt = item.getValue().getOption();
            switch (item.getValue().getEntry().getCategory()) {
                case TOOLTIP_TOGGLE -> { tooltipToggles = tooltipToggles.option(opt); }
                case ENCHANTMENT_GROUP_COLOR -> { enchantmentColors = enchantmentColors.option(opt); }
                case TOOLTIP_COLOR -> { tooltipColors = tooltipColors.option(opt); }
                case SLOT_HIGHLIGHT -> { highlights = highlights.option(opt); }
                case MISCELLANEOUS -> { miscellaneous = miscellaneous.option(opt); }
                default -> { miscellaneous = miscellaneous.option(opt); }
            }
        }

        for(Map.Entry<String, EnchantmentColorDataEntry> item : ModConfig.individualColors.entrySet()) {
            individualEnchantmentColors = individualEnchantmentColors.option(Option.createBuilder(Color.class)
                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.min_color", Text.translatable(item.getValue().enchantment.getTranslationKey())))
                    .tooltip(Text.of(item.getValue().enchantmentKey))
                    .binding(item.getValue().getDefaultMinColor(), () -> item.getValue().minColor, v -> item.getValue().minColor = v)
                    .controller(ColorController::new)
                    .build()
            ).option(Option.createBuilder(Color.class)
                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.max_color", Text.translatable(item.getValue().enchantment.getTranslationKey())))
                    .tooltip(Text.of(item.getValue().enchantmentKey))
                    .binding(item.getValue().getDefaultMaxColor(), () -> item.getValue().maxColor, v -> item.getValue().maxColor = v)
                    .controller(ColorController::new)
                    .build()
            );
            individualEnchantmentMeta = individualEnchantmentMeta.option(Option.createBuilder(Integer.class)
                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.order", Text.translatable(item.getValue().enchantment.getTranslationKey())))
                    .tooltip(Text.of(item.getValue().enchantmentKey))
                    .binding(item.getValue().getDefaultOrder(), () -> item.getValue().order, v -> item.getValue().order = v)
                    .controller(IntegerFieldController::new)
                    .build()
            ).option(Option.createBuilder(Boolean.class)
                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.highlight_visibility", Text.translatable(item.getValue().enchantment.getTranslationKey())))
                    .tooltip(Text.of(item.getValue().enchantmentKey))
                    .binding(item.getValue().getDefaultHighlightVisibility(), () -> item.getValue().showHighlight, v -> item.getValue().showHighlight = v)
                    .controller(TickBoxController::new)
                    .build()
            );
        }

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable(EnchantipsClient.MODID + ".config"))
                .save(ModConfig::writeConfig)
                .category(tooltipToggles.build())
                .category(enchantmentColors.build())
                .category(tooltipColors.build())
                .category(highlights.build())
                .category(miscellaneous.build())
                .category(individualEnchantments
                        .group(individualEnchantmentColors.collapsed(true).build())
                        .group(individualEnchantmentMeta.collapsed(true).build())
                        .build())
                .build().generateScreen(parent);
    }
}
