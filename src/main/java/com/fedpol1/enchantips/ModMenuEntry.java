package com.fedpol1.enchantips;

import com.fedpol1.enchantips.config.BooleanDataEntry;
import com.fedpol1.enchantips.config.BooleanDataEntry.*;
import com.fedpol1.enchantips.config.ColorDataEntry;
import com.fedpol1.enchantips.config.ColorDataEntry.*;
import com.fedpol1.enchantips.config.IntegerDataEntry;
import com.fedpol1.enchantips.config.ModConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.util.Map;

public class ModMenuEntry implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(MinecraftClient.getInstance().currentScreen)
                    .setTitle(Text.translatable(EnchantipsClient.MODID + ".config"));

            ConfigCategory tooltipToggles = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.toggles_tooltips"));
            ConfigCategory enchantmentColors = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors_enchantments"));
            ConfigCategory tooltipColors = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors_tooltips"));
            ConfigCategory highlights = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.highlights"));
            ConfigCategory miscellaneous = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.miscellaneous"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            // tooltip toggles
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : ModConfig.tooltipToggles.entrySet()) {
                BooleanData value = item.getValue();
                tooltipToggles.addEntry(
                        entryBuilder.startBooleanToggle(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(newValue))
                                .build()
                );
            }

            // enchantment colors
            for(Map.Entry<String, ColorDataEntry.ColorData> item : ModConfig.enchantmentColors.entrySet()) {
                ColorData value = item.getValue();
                enchantmentColors.addEntry(
                        entryBuilder.startColorField(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(TextColor.fromRgb(newValue)))
                                .build()
                );
            }

            // tooltip colors
            for(Map.Entry<String, ColorDataEntry.ColorData> item : ModConfig.tooltipColors.entrySet()) {
                ColorData value = item.getValue();
                tooltipColors.addEntry(
                        entryBuilder.startColorField(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(TextColor.fromRgb(newValue)))
                                .build()
                );
            }

            // highlights
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : ModConfig.highlightToggles.entrySet()) {
                BooleanData value = item.getValue();
                highlights.addEntry(
                        entryBuilder.startBooleanToggle(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(newValue))
                                .build()
                );
            }

            for(Map.Entry<String, ColorDataEntry.ColorData> item : ModConfig.highlightColors.entrySet()) {
                ColorData value = item.getValue();
                highlights.addEntry(
                        entryBuilder.startColorField(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(TextColor.fromRgb(newValue)))
                                .build()
                );
            }

            // misc
            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : ModConfig.miscToggles.entrySet()) {
                BooleanData value = item.getValue();
                miscellaneous.addEntry(
                        entryBuilder.startBooleanToggle(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(newValue))
                                .build()
                );
            }

            for(Map.Entry<String, IntegerDataEntry.IntegerData> item : ModConfig.miscValues.entrySet()) {
                IntegerDataEntry.IntegerData value = item.getValue();
                miscellaneous.addEntry(
                        entryBuilder.startIntSlider(Text.translatable(value.getTitle()), value.getValue(), 1, 16)
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(newValue))
                                .build()
                );
            }

            // save config
            builder.setSavingRunnable(ModConfig::writeConfig);
            return builder.build();
        };
    }

}
