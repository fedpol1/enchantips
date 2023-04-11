package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import com.fedpol1.enchantips.config.tree.visitor.WriteVisitor;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static LinkedHashMap<ModOption, OptionNode<?>> data = new LinkedHashMap<>();
    public static HashMap<Enchantment, GroupNode> enchantmentData = new HashMap<>();

    public static void registerConfig() throws NullPointerException {
        EnchantipsClient.LOGGER.info("Initializing configs");
        Node t;
        t = ConfigTree.root.addChild(new CategoryNode("toggles_tooltips"));
        ModConfig.data.put(ModOption.SHOW_REPAIRCOST, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.repair_cost", true, true))));
        ModConfig.data.put(ModOption.SHOW_ENCHANTABILITY, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.enchantability",true, false))));
        ModConfig.data.put(ModOption.SHOW_ENCHANTABILITY_WHEN_ENCHANTED, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.enchantability.when_enchanted",true, false))));
        ModConfig.data.put(ModOption.SHOW_RARITY, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.rarity",true, false))));
        ModConfig.data.put(ModOption.SHOW_MODIFIED_ENCHANTMENT_LEVEL, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.modified_level",true, false))));
        ModConfig.data.put(ModOption.SHOW_EXTRA_ENCHANTMENTS, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.extra_enchantments",true, false))));
        ModConfig.data.put(ModOption.SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.modified_level.for_enchantment",false, false))));
        t = ConfigTree.root.addChild(new CategoryNode("colors_tooltips"));
        ModConfig.data.put(ModOption.REPAIRCOST, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.repair_cost", 0xffbf00, false))));
        ModConfig.data.put(ModOption.REPAIRCOST_VALUE, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.repair_cost.value", 0xff7f00, false))));
        ModConfig.data.put(ModOption.ENCHANTABILITY, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.enchantability", 0xffbf00, false))));
        ModConfig.data.put(ModOption.ENCHANTABILITY_VALUE, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.enchantability.value", 0xff7f00, false))));
        ModConfig.data.put(ModOption.RARITY_BRACKET, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.rarity.bracket", 0x3f3f3f, false))));
        ModConfig.data.put(ModOption.MODIFIED_ENCHANTMENT_LEVEL, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.modified_level", 0xffbf00, false))));
        ModConfig.data.put(ModOption.MODIFIED_ENCHANTMENT_LEVEL_VALUE, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.modified_level.value", 0xff7f00, false))));
        ModConfig.data.put(ModOption.MODIFIED_LEVEL_FOR_ENCHANTMENT, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.modified_level.for_enchantment", 0xdf9f3f, false))));
        ModConfig.data.put(ModOption.MODIFIED_LEVEL_FOR_ENCHANTMENT_VALUE, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.modified_level.for_enchantment.value", 0xdf7f3f, false))));
        t = ConfigTree.root.addChild(new CategoryNode("highlights"));
        ModConfig.data.put(ModOption.SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.highlights.special_enchantment",false, false))));
        ModConfig.data.put(ModOption.HIGHLIGHTS_RESPECT_HIDEFLAGS, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.highlights.hideflags",true, false))));
        ModConfig.data.put(ModOption.HIGHLIGHT_HOTBAR_ALPHA, (OptionNode<?>) t.addChild(new OptionNode<>(new IntegerDataEntry("highlights.hotbar_alpha", 127, 0, 255, 0, false))));
        ModConfig.data.put(ModOption.HIGHLIGHT_TRADING_ALPHA, (OptionNode<?>) t.addChild(new OptionNode<>(new IntegerDataEntry("highlights.trading_alpha", 127, 0, 255, 0, false))));
        ModConfig.data.put(ModOption.HIGHLIGHT_LIMIT, (OptionNode<?>) t.addChild(new OptionNode<>(new IntegerDataEntry("highlights.limit", 4, 0, 16, 1, false))));
        t = ConfigTree.root.addChild(new CategoryNode("miscellaneous"));
        ModConfig.data.put(ModOption.SHOW_PROTECTION_BAR, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.bar.protection",false, false))));
        ModConfig.data.put(ModOption.SHOW_ANVIL_ITEM_SWAP_BUTTON, (OptionNode<?>) t.addChild(new OptionNode<>(new BooleanDataEntry("show.button.anvil_item_swap",false, true))));
        ModConfig.data.put(ModOption.UNBREAKABLE_COLOR, (OptionNode<?>) t.addChild(new OptionNode<>(new ColorDataEntry("color.unbreakable", 0x00dfff, false))));
        t = ConfigTree.root.addChild(new CategoryNode("individual_enchantments"));
        GroupNode gn;
        for(Enchantment current : Registries.ENCHANTMENT) {
            gn = new GroupNode(current.getTranslationKey());
            ModConfig.enchantmentData.put(current, gn);
            gn.addChild(new OptionNode<>(new ColorDataEntry("min_color", ((EnchantmentAccess)current).enchantipsGetColor(current.getMinLevel()).getRgb(), false)));
            gn.addChild(new OptionNode<>(new ColorDataEntry("max_color", ((EnchantmentAccess)current).enchantipsGetColor(current.getMinLevel()).getRgb(), false)));
            gn.addChild(new OptionNode<>(new IntegerDataEntry("order", ((EnchantmentAccess)current).enchantipsGetPriority().ordinal(), -2000000000, 2000000000, 0, false)));
            gn.addChild(new OptionNode<>(new BooleanDataEntry("highlight_visibility", true, false)));
        }
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    private static void setDefaultConfig() {
        for(Map.Entry<ModOption, OptionNode<?>> item : data.entrySet()) {
            data.get(item.getKey()).getEntry().getData().setValueToDefault();
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
                        GroupNode gn = enchantmentData.get(Registries.ENCHANTMENT.get(new Identifier(line.substring(4, line.length() - 1))));
                        for (int i = 0; i < 4; i++) {
                            line = sc.nextLine();
                            split = line.substring(4).split(": ");
                            switch (split[0]) {
                                case "min_color" -> { ((OptionNode<?>) gn.getChild(0)).getEntry().getData().readStringValue(split[1]); }
                                case "max_color" -> { ((OptionNode<?>) gn.getChild(1)).getEntry().getData().readStringValue(split[1]); }
                                case "order" -> { ((OptionNode<?>) gn.getChild(2)).getEntry().getData().readStringValue(split[1]); }
                                case "show_highlight" -> { ((OptionNode<?>) gn.getChild(3)).getEntry().getData().readStringValue(split[1]); }
                            }
                        }
                    }
                    else {
                        try {
                            //configData.get(split[0]).readStringValue(split[1]);
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
        StringBuilder acc = (StringBuilder) ConfigTree.root.accept(new WriteVisitor(), 0);/*
        StringBuilder acc = new StringBuilder();

        TreeMap<ModOption, ArrayList<Data<?>>> data = new TreeMap<>();
        for(ModOption cat : ModOption.values()) { // populate map
            data.put(cat, new ArrayList<>());
        }
        for(Map.Entry<ModOption, OptionNode<?>> item : configData.entrySet()) { // populate arraylists in map
            data.get(item.getValue().getEntry().getCategory()).add(item.getValue());
        }

        for(Map.Entry<ModOption, ArrayList<Data<?>>> item : data.entrySet()) { // write general settings to file
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
        }*/

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
        return ((YetAnotherConfigLib) ConfigTree.root.accept(new ScreenVisitor(), null)).generateScreen(parent);
        /*
        ConfigCategory.Builder tooltipToggles =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.toggles_tooltips"));
        ConfigCategory.Builder tooltipColors =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors_tooltips"));
        ConfigCategory.Builder highlights =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.highlights"));
        ConfigCategory.Builder miscellaneous =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.miscellaneous"));
        ConfigCategory.Builder individualEnchantments =  ConfigCategory.createBuilder().name(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantments"));

        for(Map.Entry<String, Data<?>> item : ModConfig.configDataOld.entrySet()) {
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
                .build().generateScreen(parent);*/
    }
}
