package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentAccess;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.TextColor;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static TreeMap<String, BooleanDataEntry.BooleanData> tooltipToggles = new TreeMap<>();
    public static TreeMap<String, BooleanDataEntry.BooleanData> highlightToggles = new TreeMap<>();
    public static TreeMap<String, BooleanDataEntry.BooleanData> miscToggles = new TreeMap<>();
    public static TreeMap<String, IntegerDataEntry.IntegerData> miscValues = new TreeMap<>();
    public static TreeMap<String, ColorDataEntry.ColorData> enchantmentColors = new TreeMap<>();
    public static TreeMap<String, ColorDataEntry.ColorData> tooltipColors = new TreeMap<>();
    public static TreeMap<String, ColorDataEntry.ColorData> highlightColors = new TreeMap<>();
    public static TreeMap<String, EnchantmentColorDataEntry> individualColors = new TreeMap<>();

    public static BooleanDataEntry SHOW_REPAIRCOST = new BooleanDataEntry("show.repair_cost", true, true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY = new BooleanDataEntry("show.enchantability", true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY_WHEN_ENCHANTED = new BooleanDataEntry("show.enchantability.when_enchanted", true);
    public static BooleanDataEntry SHOW_RARITY = new BooleanDataEntry("show.rarity", true);
    public static BooleanDataEntry SHOW_MODIFIED_ENCHANTMENT_LEVEL = new BooleanDataEntry("show.modified_level", true);
    public static BooleanDataEntry SHOW_EXTRA_ENCHANTMENTS = new BooleanDataEntry("show.extra_enchantments", true);
    public static BooleanDataEntry SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT = new BooleanDataEntry("show.modified_level.for_enchantment", false);
    public static BooleanDataEntry SHOW_PROTECTION_BAR = new BooleanDataEntry("show.bar.protection", false);
    public static BooleanDataEntry SHOW_ANVIL_ITEM_SWAP_BUTTON = new BooleanDataEntry("show.button.anvil_item_swap", false, true);
    public static BooleanDataEntry OVERRIDE_INDIVIDUAL_ENCHANTMENTS = new BooleanDataEntry("override.enchantments", true, true);
    public static BooleanDataEntry SHOW_HIGHLIGHTS_ENCHANTMENT_MATCH = new BooleanDataEntry("show.highlights.matching_enchantment", false, true);
    public static BooleanDataEntry SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED = new BooleanDataEntry("show.highlights.special_enchantment", false, true);
    public static BooleanDataEntry HIGHLIGHTS_RESPECT_HIDEFLAGS = new BooleanDataEntry("show.highlights.hideflags", true, true);
    public static IntegerDataEntry HIGHLIGHT_LIMIT = new IntegerDataEntry("highlights.limit", 4, true);
    public static ColorDataEntry ENCHANTMENT_NORMAL_MIN = new ColorDataEntry("color.enchantment.normal.min", 0x7f7f7f);
    public static ColorDataEntry ENCHANTMENT_NORMAL_MAX = new ColorDataEntry("color.enchantment.normal.max", 0xdfdfdf);
    public static ColorDataEntry ENCHANTMENT_TREASURE_MIN = new ColorDataEntry("color.enchantment.treasure.min", 0x009f00);
    public static ColorDataEntry ENCHANTMENT_TREASURE_MAX = new ColorDataEntry("color.enchantment.treasure.max", 0x00df00);
    public static ColorDataEntry ENCHANTMENT_CURSE_MIN = new ColorDataEntry("color.enchantment.curse.min", 0xbf0000);
    public static ColorDataEntry ENCHANTMENT_CURSE_MAX = new ColorDataEntry("color.enchantment.curse.max", 0xff0000);
    public static ColorDataEntry ENCHANTMENT_SPECIAL = new ColorDataEntry("color.enchantment.special", 0x00dfff);
    public static ColorDataEntry REPAIRCOST = new ColorDataEntry("color.repair_cost", 0xffbf00);
    public static ColorDataEntry REPAIRCOST_VALUE = new ColorDataEntry("color.repair_cost.value", 0xff7f00);
    public static ColorDataEntry ENCHANTABILITY = new ColorDataEntry("color.enchantability", 0xffbf00);
    public static ColorDataEntry ENCHANTABILITY_VALUE = new ColorDataEntry("color.enchantability.value", 0xff7f00);
    public static ColorDataEntry RARITY_BRACKET = new ColorDataEntry("color.rarity.bracket", 0x3f3f3f);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL = new ColorDataEntry("color.modified_level", 0xffbf00);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL_VALUE = new ColorDataEntry("color.modified_level.value", 0xff7f00);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT = new ColorDataEntry("color.modified_level.for_enchantment", 0xdf9f3f);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE = new ColorDataEntry("color.modified_level.for_enchantment.value", 0xdf7f3f);
    public static ColorDataEntry SLOT_HIGHLIGHT_FULL_MATCH = new ColorDataEntry("color.slot_highlight.full_match", 0x3fff3f);
    public static ColorDataEntry SLOT_HIGHLIGHT_PARTIAL_MATCH = new ColorDataEntry("color.slot_highlight.partial_match", 0xffbf3f);

    public static void registerConfig() throws NullPointerException {
        EnchantipsClient.LOGGER.info("Initializing configs");
        tooltipToggles.put(SHOW_REPAIRCOST.key, SHOW_REPAIRCOST.data);
        tooltipToggles.put(SHOW_ENCHANTABILITY.key, SHOW_ENCHANTABILITY.data);
        tooltipToggles.put(SHOW_ENCHANTABILITY_WHEN_ENCHANTED.key, SHOW_ENCHANTABILITY_WHEN_ENCHANTED.data);
        tooltipToggles.put(SHOW_RARITY.key, SHOW_RARITY.data);
        tooltipToggles.put(SHOW_MODIFIED_ENCHANTMENT_LEVEL.key, SHOW_MODIFIED_ENCHANTMENT_LEVEL.data);
        tooltipToggles.put(SHOW_EXTRA_ENCHANTMENTS.key, SHOW_EXTRA_ENCHANTMENTS.data);
        tooltipToggles.put(SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT.key, SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT.data);
        miscToggles.put(SHOW_PROTECTION_BAR.key, SHOW_PROTECTION_BAR.data);
        miscToggles.put(SHOW_ANVIL_ITEM_SWAP_BUTTON.key, SHOW_ANVIL_ITEM_SWAP_BUTTON.data);
        miscToggles.put(OVERRIDE_INDIVIDUAL_ENCHANTMENTS.key, OVERRIDE_INDIVIDUAL_ENCHANTMENTS.data);
        miscValues.put(HIGHLIGHT_LIMIT.key, HIGHLIGHT_LIMIT.data);
        highlightToggles.put(SHOW_HIGHLIGHTS_ENCHANTMENT_MATCH.key, SHOW_HIGHLIGHTS_ENCHANTMENT_MATCH.data);
        highlightToggles.put(SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.key, SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.data);
        highlightToggles.put(HIGHLIGHTS_RESPECT_HIDEFLAGS.key, HIGHLIGHTS_RESPECT_HIDEFLAGS.data);
        enchantmentColors.put(ENCHANTMENT_NORMAL_MIN.key, ENCHANTMENT_NORMAL_MIN.data);
        enchantmentColors.put(ENCHANTMENT_NORMAL_MAX.key, ENCHANTMENT_NORMAL_MAX.data);
        enchantmentColors.put(ENCHANTMENT_TREASURE_MIN.key, ENCHANTMENT_TREASURE_MIN.data);
        enchantmentColors.put(ENCHANTMENT_TREASURE_MAX.key, ENCHANTMENT_TREASURE_MAX.data);
        enchantmentColors.put(ENCHANTMENT_CURSE_MIN.key, ENCHANTMENT_CURSE_MIN.data);
        enchantmentColors.put(ENCHANTMENT_CURSE_MAX.key, ENCHANTMENT_CURSE_MAX.data);
        enchantmentColors.put(ENCHANTMENT_SPECIAL.key, ENCHANTMENT_SPECIAL.data);
        tooltipColors.put(REPAIRCOST.key, REPAIRCOST.data);
        tooltipColors.put(REPAIRCOST_VALUE.key, REPAIRCOST_VALUE.data);
        tooltipColors.put(ENCHANTABILITY.key, ENCHANTABILITY.data);
        tooltipColors.put(ENCHANTABILITY_VALUE.key, ENCHANTABILITY_VALUE.data);
        tooltipColors.put(RARITY_BRACKET.key, RARITY_BRACKET.data);
        tooltipColors.put(MODIFIED_ENCHANTMENT_LEVEL.key, MODIFIED_ENCHANTMENT_LEVEL.data);
        tooltipColors.put(MODIFIED_ENCHANTMENT_LEVEL_VALUE.key, MODIFIED_ENCHANTMENT_LEVEL_VALUE.data);
        tooltipColors.put(MODIFIED_LEVEL_FOR_ENCHANTMENT.key, MODIFIED_LEVEL_FOR_ENCHANTMENT.data);
        tooltipColors.put(MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE.key, MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE.data);
        highlightColors.put(SLOT_HIGHLIGHT_FULL_MATCH.key, SLOT_HIGHLIGHT_FULL_MATCH.data);
        highlightColors.put(SLOT_HIGHLIGHT_PARTIAL_MATCH.key, SLOT_HIGHLIGHT_PARTIAL_MATCH.data);
        setPartialDefaultConfigEnchantments();
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        setPartialDefaultConfig(tooltipToggles);
        setPartialDefaultConfig(highlightToggles);
        setPartialDefaultConfig(miscToggles);
        setPartialDefaultConfig(miscValues);
        setPartialDefaultConfig(enchantmentColors);
        setPartialDefaultConfig(tooltipColors);
        setPartialDefaultConfig(highlightColors);
        setPartialDefaultConfigEnchantments();
    }

    private static <T extends Data> void setPartialDefaultConfig(TreeMap<String, T> map) {
        for(Map.Entry<String, T> item : map.entrySet()) {
            map.get(item.getKey()).setValueToDefault();
        }
    }

    private static void setPartialDefaultConfigEnchantments() {
        for(Enchantment current : Registry.ENCHANTMENT) {
            EnchantmentColorDataEntry dataEntry = new EnchantmentColorDataEntry(current);
            dataEntry.order = ((EnchantmentAccess)current).enchantipsGetPriority().ordinal();
            dataEntry.active = true;
            individualColors.put(Objects.requireNonNull(Registry.ENCHANTMENT.getId(current)).toString(), dataEntry);
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
                    state = line.substring(2, line.length()-1); continue;
                }

                if(line.matches("^ {2}[^ ].*$")) {
                    split = line.substring(2).split(": ");
                    switch (state) {
                        case "tooltips" -> { tooltipToggles.get(split[0]).setValue(Boolean.parseBoolean(split[1])); }
                        case "highlights" -> { highlightToggles.get(split[0]).setValue(Boolean.parseBoolean(split[1])); }
                        case "miscellaneous_toggles" -> { miscToggles.get(split[0]).setValue(Boolean.parseBoolean(split[1])); }
                        case "miscellaneous_values" -> { miscValues.get(split[0]).setValue(Integer.parseInt(split[1])); }
                        case "grouped_enchantment_colors" -> { enchantmentColors.get(split[0]).setValue(TextColor.parse(split[1])); }
                        case "tooltip_colors" -> { tooltipColors.get(split[0]).setValue(TextColor.parse(split[1])); }
                        case "enchanted_book_highlight_colors" -> { highlightColors.get(split[0]).setValue(TextColor.parse(split[1])); }
                        case "individual_enchantment_settings" -> {
                            EnchantmentColorDataEntry entry = Objects.requireNonNull(individualColors.get(line.substring(4, line.length()-1)));
                            for(int i = 0; i < 5; i++) {
                                line = sc.nextLine();
                                split = line.substring(4).split(": ");
                                switch (split[0]) {
                                    case "min_color" -> { entry.minColor = TextColor.parse(split[1]); }
                                    case "max_color" -> { entry.maxColor = TextColor.parse(split[1]); }
                                    case "order" -> { entry.order = Integer.parseInt(split[1]); }
                                    case "show_highlight" -> { entry.showHighlight = Boolean.parseBoolean(split[1]); }
                                    case "active" -> { entry.active = Boolean.parseBoolean(split[1]); }
                                }
                            }
                            entry.active = true;
                            individualColors.put(entry.enchantmentKey, entry);
                        }
                    }
                }
            }
        }
        catch (IOException | IllegalArgumentException e) {
            EnchantipsClient.LOGGER.error("Could not read configuration file.\n" + e.getMessage());
            EnchantipsClient.LOGGER.info("Setting default configuration.");
            ModConfig.setDefaultConfig();
        }
    }

    public static void writeConfig() {
        if(ModConfig.OVERRIDE_INDIVIDUAL_ENCHANTMENTS.getValue()) {
            setPartialDefaultConfigEnchantments();
        }
        try {
            FileWriter fw = new FileWriter(ModConfig.CONFIG_FILE);
            StringBuilder acc = new StringBuilder();
            acc.append("- tooltips:\n");
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : tooltipToggles.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue()).append("\n");
            }
            acc.append("- highlights:\n");
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : highlightToggles.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue()).append("\n");
            }
            acc.append("- miscellaneous_toggles:\n");
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : miscToggles.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue()).append("\n");
            }
            acc.append("- miscellaneous_values:\n");
            for(Map.Entry<String, IntegerDataEntry.IntegerData> item : miscValues.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue()).append("\n");
            }
            acc.append("- grouped_enchantment_colors:\n");
            for(Map.Entry<String, ColorDataEntry.ColorData> item : enchantmentColors.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue().getHexCode()).append("\n");
            }
            acc.append("- tooltip_colors:\n");
            for(Map.Entry<String, ColorDataEntry.ColorData> item : tooltipColors.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue().getHexCode()).append("\n");
            }
            acc.append("- enchanted_book_highlight_colors:\n");
            for(Map.Entry<String, ColorDataEntry.ColorData> item : highlightColors.entrySet()) {
                acc.append("  ").append(item.getKey()).append(": ").append(item.getValue().getValue().getHexCode()).append("\n");
            }
            acc.append("- individual_enchantment_settings:\n");
            for(Map.Entry<String, EnchantmentColorDataEntry> item : individualColors.entrySet()) {
                acc.append("  - ").append(item.getKey()).append(":\n");
                acc.append("    min_color: ").append(item.getValue().minColor.getHexCode()).append("\n");
                acc.append("    max_color: ").append(item.getValue().maxColor.getHexCode()).append("\n");
                acc.append("    order: ").append(item.getValue().order).append("\n");
                acc.append("    show_highlight: ").append(item.getValue().showHighlight).append("\n");
                acc.append("    active: ").append(item.getValue().active).append("\n");
            }

            fw.write(acc.toString());
            fw.close();
        }
        catch (IOException e) {
            EnchantipsClient.LOGGER.error("Could not write configuration file.\n" + e.getMessage());
        }
    }
}
