package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.data.*;
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
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static LinkedHashMap<String, Data<?>> data = new LinkedHashMap<>();
    public static HashMap<String, EnchantmentColorDataEntry> enchantmentData = new LinkedHashMap<>();

    public static BooleanDataEntry SHOW_REPAIRCOST = new BooleanDataEntry("show.repair_cost", ModConfigCategory.TOOLTIP_TOGGLE, true, true);
    public static BooleanDataEntry SHOW_ENCHANTABILITY = new BooleanDataEntry("show.enchantability", ModConfigCategory.TOOLTIP_TOGGLE, true, false);
    public static BooleanDataEntry SHOW_ENCHANTABILITY_WHEN_ENCHANTED = new BooleanDataEntry("show.enchantability.when_enchanted", ModConfigCategory.TOOLTIP_TOGGLE, true, false);
    public static BooleanDataEntry SHOW_RARITY = new BooleanDataEntry("show.rarity", ModConfigCategory.TOOLTIP_TOGGLE, true, false);
    public static BooleanDataEntry SHOW_MODIFIED_ENCHANTMENT_LEVEL = new BooleanDataEntry("show.modified_level", ModConfigCategory.TOOLTIP_TOGGLE, true, false);
    public static BooleanDataEntry SHOW_EXTRA_ENCHANTMENTS = new BooleanDataEntry("show.extra_enchantments", ModConfigCategory.TOOLTIP_TOGGLE, true, false);
    public static BooleanDataEntry SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT = new BooleanDataEntry("show.modified_level.for_enchantment", ModConfigCategory.TOOLTIP_TOGGLE, false, false);
    public static BooleanDataEntry SHOW_PROTECTION_BAR = new BooleanDataEntry("show.bar.protection", ModConfigCategory.MISCELLANEOUS, false, false);
    public static BooleanDataEntry SHOW_ANVIL_ITEM_SWAP_BUTTON = new BooleanDataEntry("show.button.anvil_item_swap", ModConfigCategory.MISCELLANEOUS, false, true);
    public static BooleanDataEntry SHOW_HIGHLIGHTS = new BooleanDataEntry("show.highlights", ModConfigCategory.SLOT_HIGHLIGHT, false, false);
    public static BooleanDataEntry HIGHLIGHTS_RESPECT_HIDEFLAGS = new BooleanDataEntry("show.highlights.hideflags", ModConfigCategory.SLOT_HIGHLIGHT, true, false);
    public static IntegerDataEntry HIGHLIGHT_HOTBAR_ALPHA = new IntegerDataEntry("highlights.hotbar_alpha", ModConfigCategory.SLOT_HIGHLIGHT, 127, 0, 255, 0, false);
    public static IntegerDataEntry HIGHLIGHT_TRADING_ALPHA = new IntegerDataEntry("highlights.trading_alpha", ModConfigCategory.SLOT_HIGHLIGHT, 127, 0, 255, 0, false);
    public static IntegerDataEntry HIGHLIGHT_LIMIT = new IntegerDataEntry("highlights.limit", ModConfigCategory.SLOT_HIGHLIGHT, 4, 0, 16, 1, true);
    public static ColorDataEntry UNBREAKABLE_COLOR = new ColorDataEntry("color.unbreakable", ModConfigCategory.MISCELLANEOUS, 0x00dfff, false);
    public static ColorDataEntry REPAIRCOST = new ColorDataEntry("color.repair_cost", ModConfigCategory.TOOLTIP_COLOR, 0xffbf00, false);
    public static ColorDataEntry REPAIRCOST_VALUE = new ColorDataEntry("color.repair_cost.value", ModConfigCategory.TOOLTIP_COLOR, 0xff7f00, false);
    public static ColorDataEntry ENCHANTABILITY = new ColorDataEntry("color.enchantability", ModConfigCategory.TOOLTIP_COLOR, 0xffbf00, false);
    public static ColorDataEntry ENCHANTABILITY_VALUE = new ColorDataEntry("color.enchantability.value", ModConfigCategory.TOOLTIP_COLOR, 0xff7f00, false);
    public static ColorDataEntry RARITY_BRACKET = new ColorDataEntry("color.rarity.bracket", ModConfigCategory.TOOLTIP_COLOR, 0x3f3f3f, false);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL = new ColorDataEntry("color.modified_level", ModConfigCategory.TOOLTIP_COLOR, 0xffbf00, false);
    public static ColorDataEntry MODIFIED_ENCHANTMENT_LEVEL_VALUE = new ColorDataEntry("color.modified_level.value", ModConfigCategory.TOOLTIP_COLOR, 0xff7f00, false);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT = new ColorDataEntry("color.modified_level.for_enchantment", ModConfigCategory.TOOLTIP_COLOR, 0xdf9f3f, false);
    public static ColorDataEntry MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE = new ColorDataEntry("color.modified_level.for_enchantment.value", ModConfigCategory.TOOLTIP_COLOR, 0xdf7f3f, false);

    public static void registerConfig() throws NullPointerException {
        EnchantipsClient.LOGGER.info("Initializing configs");
        setPartialDefaultConfigEnchantments();
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        for(Map.Entry<String, Data<?>> item : data.entrySet()) {
            data.get(item.getKey()).setValueToDefault();
        }
        setPartialDefaultConfigEnchantments();
    }

    // fills individualColors map with enchantment color data, sorted
    public static void setPartialDefaultConfigEnchantments() {
        for(Enchantment current : Registries.ENCHANTMENT) {
            ModConfig.enchantmentData.put(Objects.requireNonNull(Registries.ENCHANTMENT.getId(current)).toString(), new EnchantmentColorDataEntry(current));
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
                        EnchantmentColorDataEntry entry = Objects.requireNonNull(enchantmentData.get(line.substring(4, line.length() - 1)));
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
                        enchantmentData.put(entry.enchantmentKey, entry);
                    }
                    else {
                        try {
                            data.get(split[0]).readStringValue(split[1]);
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
        StringBuilder acc = new StringBuilder();

        TreeMap<ModConfigCategory, ArrayList<Data<?>>> data = new TreeMap<>();
        for(ModConfigCategory cat : ModConfigCategory.values()) { // populate map
            data.put(cat, new ArrayList<>());
        }
        for(Map.Entry<String, Data<?>> item : ModConfig.data.entrySet()) { // populate arraylists in map
            data.get(item.getValue().getEntry().getCategory()).add(item.getValue());
        }

        for(Map.Entry<ModConfigCategory, ArrayList<Data<?>>> item : data.entrySet()) { // write general settings to file
            acc.append("- ").append(item.getKey().toString()).append(":\n");
            for(Data<?> d : item.getValue()) {
                acc.append("  ").append(d.getEntry().getKey()).append(": ").append(d.getStringValue()).append("\n");
            }
        }

        acc.append("- individual_enchantment_setting:\n");
        for(Map.Entry<String, EnchantmentColorDataEntry> item : enchantmentData.entrySet()) { // write specific enchantment settings to file
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
        ConfigCategory.Builder tooltipColors =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors_tooltips"));
        ConfigCategory.Builder highlights =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.highlights"));
        ConfigCategory.Builder miscellaneous =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.miscellaneous"));
        ConfigCategory.Builder individualEnchantments =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantments"));

        for(Map.Entry<String, Data<?>> item : ModConfig.data.entrySet()) {
            Option<?> opt = item.getValue().getOption();
            switch (item.getValue().getEntry().getCategory()) {
                case TOOLTIP_TOGGLE -> { tooltipToggles = tooltipToggles.option(opt); }
                case TOOLTIP_COLOR -> { tooltipColors = tooltipColors.option(opt); }
                case SLOT_HIGHLIGHT -> { highlights = highlights.option(opt); }
                case MISCELLANEOUS -> { miscellaneous = miscellaneous.option(opt); }
                default -> { miscellaneous = miscellaneous.option(opt); }
            }
        }

        // sort enchantments in the screen alphabetically
        LinkedHashMap<String, EnchantmentColorDataEntry> sortedEnchantments = new LinkedHashMap<>();
        ModConfig.enchantmentData.entrySet().stream().sorted(new Comparator<Map.Entry<String, EnchantmentColorDataEntry>>() {
            @Override
            public int compare(Map.Entry<String, EnchantmentColorDataEntry> o1, Map.Entry<String, EnchantmentColorDataEntry> o2) {
                String s1 = Text.translatable(Registries.ENCHANTMENT.get(new Identifier(o1.getKey())).getTranslationKey()).getString();
                String s2 = Text.translatable(Registries.ENCHANTMENT.get(new Identifier(o2.getKey())).getTranslationKey()).getString();
                return s1.compareTo(s2);
            }
        }).forEach(e -> sortedEnchantments.put(e.getKey(), e.getValue()));

        for(Map.Entry<String, EnchantmentColorDataEntry> item : sortedEnchantments.entrySet()) {
            Text enchText = Text.translatable(item.getValue().enchantment.getTranslationKey());
            Text enchTooltip = Text.of(item.getValue().enchantmentKey);
            individualEnchantments = individualEnchantments.group(
                    OptionGroup.createBuilder()
                            .name(enchText)
                            .collapsed(true)
                            .option(Option.createBuilder(Color.class)
                                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.min_color", enchText))
                                    .tooltip(enchTooltip)
                                    .binding(item.getValue().getDefaultMinColor(), () -> item.getValue().minColor, v -> item.getValue().minColor = v)
                                    .controller(ColorController::new)
                                    .build()
                            ).option(Option.createBuilder(Color.class)
                                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.max_color", enchText))
                                    .tooltip(enchTooltip)
                                    .binding(item.getValue().getDefaultMaxColor(), () -> item.getValue().maxColor, v -> item.getValue().maxColor = v)
                                    .controller(ColorController::new)
                                    .build()
                            ).option(Option.createBuilder(Integer.class)
                                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.order", enchText))
                                    .tooltip(enchTooltip)
                                    .binding(item.getValue().getDefaultOrder(), () -> item.getValue().order, v -> item.getValue().order = v)
                                    .controller(IntegerFieldController::new)
                                    .build()
                            ).option(Option.createBuilder(Boolean.class)
                                    .name(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantments.highlight_visibility", enchText))
                                    .tooltip(enchTooltip)
                                    .binding(item.getValue().getDefaultHighlightVisibility(), () -> item.getValue().showHighlight, v -> item.getValue().showHighlight = v)
                                    .controller(TickBoxController::new)
                                    .build()
                            ).build()
                    );
        }

        return YetAnotherConfigLib.createBuilder()
                .title(Text.translatable(EnchantipsClient.MODID + ".config"))
                .save(ModConfig::writeConfig)
                .category(tooltipToggles.build())
                .category(tooltipColors.build())
                .category(highlights.build())
                .category(miscellaneous.build())
                .category(individualEnchantments.build())
                .build().generateScreen(parent);
    }
}
