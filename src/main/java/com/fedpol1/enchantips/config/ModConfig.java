package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.text.TextColor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import java.util.TreeMap;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static TreeMap<String, BooleanDataEntry.BooleanData> bools = new TreeMap<>();
    public static TreeMap<String, ColorDataEntry.ColorData> colors = new TreeMap<>();

    public static BooleanDataEntry SHOW_REPAIRCOST = new BooleanDataEntry("show_repaircost", true);
    public static BooleanDataEntry SHOW_REPAIRCOST_WHEN_0 = new BooleanDataEntry("show_repaircost_when_0", true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY = new BooleanDataEntry("show_enchantability", true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY_WHEN_ENCHANTED = new BooleanDataEntry("show_enchantability_when_enchanted", true);
    public static BooleanDataEntry SHOW_RARITY = new BooleanDataEntry("show_rarity", true);
    public static BooleanDataEntry SHOW_MODIFIED_ENCHANTMENT_LEVEL = new BooleanDataEntry("show_modified_level", true);
    public static BooleanDataEntry SHOW_EXTRA_ENCHANTMENTS = new BooleanDataEntry("show_extra_enchantments", true);
    public static BooleanDataEntry SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENTS = new BooleanDataEntry("show_modified_level_for_enchantments", false);
    public static ColorDataEntry ENCHANTMENT_NORMAL_MIN = new ColorDataEntry("color_enchantment_normal_min", 0x7f7f7f);
    public static ColorDataEntry ENCHANTMENT_NORMAL_MAX = new ColorDataEntry("color_enchantment_normal_max", 0xdfdfdf);
    public static ColorDataEntry ENCHANTMENT_TREASURE_MIN = new ColorDataEntry("color_enchantment_treasure_min", 0x009f00);
    public static ColorDataEntry ENCHANTMENT_TREASURE_MAX = new ColorDataEntry("color_enchantment_treasure_max", 0x00df00);
    public static ColorDataEntry ENCHANTMENT_CURSE_MIN = new ColorDataEntry("color_enchantment_curse_min", 0xbf0000);
    public static ColorDataEntry ENCHANTMENT_CURSE_MAX = new ColorDataEntry("color_enchantment_curse_max", 0xff0000);
    public static ColorDataEntry REPAIRCOST = new ColorDataEntry("color_repaircost", 0xffbf00);
    public static ColorDataEntry REPAIRCOST_VALUE = new ColorDataEntry("color_repaircost_value", 0xff7f00);
    public static ColorDataEntry ENCHANTABILITY = new ColorDataEntry("color_enchantability", 0xffbf00);
    public static ColorDataEntry ENCHANTABILITY_VALUE = new ColorDataEntry("color_enchantability_value", 0xff7f00);
    public static ColorDataEntry RARITY_BRACKET = new ColorDataEntry("color_rarity_bracket", 0x3f3f3f);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL = new ColorDataEntry("color_modified_level", 0xffbf00);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL_VALUE = new ColorDataEntry("color_modified_level_value", 0xff7f00);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT = new ColorDataEntry("color_modified_level_for_enchantment", 0xdf9f3f);
    public static ColorDataEntry MODIFIED_LEVEL_VALUE_FOR_ENCHANTMENT = new ColorDataEntry("color_modified_level_value_for_enchantment", 0xdf7f3f);

    public static void registerConfig() {
        EnchantipsClient.LOGGER.info("Initializing configs");
        bools.put(SHOW_REPAIRCOST.key, SHOW_REPAIRCOST.data);
        bools.put(SHOW_REPAIRCOST_WHEN_0.key, SHOW_REPAIRCOST_WHEN_0.data);
        bools.put(SHOW_ENCHANTABILITY.key, SHOW_ENCHANTABILITY.data);
        bools.put(SHOW_ENCHANTABILITY_WHEN_ENCHANTED.key, SHOW_ENCHANTABILITY_WHEN_ENCHANTED.data);
        bools.put(SHOW_RARITY.key, SHOW_RARITY.data);
        bools.put(SHOW_MODIFIED_ENCHANTMENT_LEVEL.key, SHOW_MODIFIED_ENCHANTMENT_LEVEL.data);
        bools.put(SHOW_EXTRA_ENCHANTMENTS.key, SHOW_EXTRA_ENCHANTMENTS.data);
        bools.put(SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENTS.key, SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENTS.data);
        colors.put(ENCHANTMENT_NORMAL_MIN.key, ENCHANTMENT_NORMAL_MIN.data);
        colors.put(ENCHANTMENT_NORMAL_MAX.key, ENCHANTMENT_NORMAL_MAX.data);
        colors.put(ENCHANTMENT_TREASURE_MIN.key, ENCHANTMENT_TREASURE_MIN.data);
        colors.put(ENCHANTMENT_TREASURE_MAX.key, ENCHANTMENT_TREASURE_MAX.data);
        colors.put(ENCHANTMENT_CURSE_MIN.key, ENCHANTMENT_CURSE_MIN.data);
        colors.put(ENCHANTMENT_CURSE_MAX.key, ENCHANTMENT_CURSE_MAX.data);
        colors.put(REPAIRCOST.key, REPAIRCOST.data);
        colors.put(REPAIRCOST_VALUE.key, REPAIRCOST_VALUE.data);
        colors.put(ENCHANTABILITY.key, ENCHANTABILITY.data);
        colors.put(ENCHANTABILITY_VALUE.key, ENCHANTABILITY_VALUE.data);
        colors.put(RARITY_BRACKET.key, RARITY_BRACKET.data);
        colors.put(MODIFIED_ENCHANTMENT_LEVEL.key, MODIFIED_ENCHANTMENT_LEVEL.data);
        colors.put(MODIFIED_ENCHANTMENT_LEVEL_VALUE.key, MODIFIED_ENCHANTMENT_LEVEL_VALUE.data);
        colors.put(MODIFIED_LEVEL_FOR_ENCHANTMENT.key, MODIFIED_LEVEL_FOR_ENCHANTMENT.data);
        colors.put(MODIFIED_LEVEL_VALUE_FOR_ENCHANTMENT.key, MODIFIED_LEVEL_VALUE_FOR_ENCHANTMENT.data);
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        for(Map.Entry<String, BooleanDataEntry.BooleanData> item : bools.entrySet()) {
            bools.get(item.getKey()).setValueToDefault();
        }
        for(Map.Entry<String, ColorDataEntry.ColorData> item : colors.entrySet()) {
            colors.get(item.getKey()).setValueToDefault();
        }
    }

    public static void readConfig() {
        try {
            ModConfig.CONFIG_FILE.createNewFile();
            Scanner sc = new Scanner(ModConfig.CONFIG_FILE);
            String[] split;
            while(sc.hasNextLine()) {
                split = sc.nextLine().split("=");
                try {
                    colors.get(split[0]).setColor(TextColor.parse(split[1]));
                }
                catch (NullPointerException ignored) {}
                try {
                    bools.get(split[0]).setValue(Boolean.parseBoolean(split[1]));
                }
                catch (NullPointerException ignored) {}
            }
        }
        catch (IOException | IllegalArgumentException e) {
            EnchantipsClient.LOGGER.error("Could not read configuration file.\n" + e.getMessage());
            EnchantipsClient.LOGGER.info("Setting default configuration.");
            ModConfig.setDefaultConfig();
        }
    }

    public static void writeConfig() {
        try {
            FileWriter fw = new FileWriter(ModConfig.CONFIG_FILE);
            StringBuilder acc = new StringBuilder();
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : bools.entrySet()) {
                acc.append(item.getKey()).append("=").append(item.getValue().getValue()).append("\n");
            }
            for(Map.Entry<String, ColorDataEntry.ColorData> item : colors.entrySet()) {
                acc.append(item.getKey()).append("=").append(item.getValue().getColor().getHexCode()).append("\n");
            }

            fw.write(acc.toString());
            fw.close();
        }
        catch (IOException e) {
            EnchantipsClient.LOGGER.error("Could not write configuration file.\n" + e.getMessage());
        }
    }
}
