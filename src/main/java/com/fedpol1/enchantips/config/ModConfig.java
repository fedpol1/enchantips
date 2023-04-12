package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.config.tree.visitor.*;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static HashMap<Enchantment, GroupNode> enchantmentData = new HashMap<>();

    public static void registerConfig() {
        EnchantipsClient.LOGGER.info("Initializing configs");
        ModCategory.init();
        ModOption.init();
        GroupNode gn;
        for(Enchantment current : Registries.ENCHANTMENT) {
            gn = (GroupNode) ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().addChild(new GroupNode(current.getTranslationKey()));
            ModConfig.enchantmentData.put(current, gn);
            gn.addChild(new OptionNode<>(new ColorDataEntry("min_color", ((EnchantmentAccess)current).enchantipsGetDefaultMinColor().getRGB(), false)));
            gn.addChild(new OptionNode<>(new ColorDataEntry("max_color", ((EnchantmentAccess)current).enchantipsGetDefaultMaxColor().getRGB(), false)));
            gn.addChild(new OptionNode<>(new IntegerDataEntry("order", ((EnchantmentAccess)current).enchantipsGetDefaultOrder(), -2000000000, 2000000000, 0, true)));
            gn.addChild(new OptionNode<>(new BooleanDataEntry("highlight_visibility", ((EnchantmentAccess)current).enchantipsGetDefaultHighlightVisibility(), false)));
        }
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    public static void readConfig() throws NullPointerException {
        // not implemented yet
    }

    public static void writeConfig() {
        StringBuilder acc = (StringBuilder) ConfigTree.root.accept(new WriteVisitor(), 0);

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
