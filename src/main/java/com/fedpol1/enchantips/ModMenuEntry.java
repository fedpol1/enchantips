package com.fedpol1.enchantips;

import com.fedpol1.enchantips.config.*;
import com.fedpol1.enchantips.config.BooleanDataEntry.*;
import com.fedpol1.enchantips.config.ColorDataEntry.*;
import com.fedpol1.enchantips.config.IntegerDataEntry.*;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.impl.builders.AbstractFieldBuilder;
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
            ConfigCategory individualEnchantmentColors = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantment.colors"));
            ConfigCategory individualEnchantmentMeta = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.individual_enchantment.meta"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            for(Map.Entry<String, Data<?>> item : ModConfig.configData.entrySet()) {
                Data<?> value = item.getValue();

                ConfigCategory cat;
                AbstractFieldBuilder fieldBuilder = null;
                switch (item.getValue().getEntry().getCategory()) {
                    case TOOLTIP_TOGGLE -> { cat = tooltipToggles; }
                    case ENCHANTMENT_GROUP_COLOR -> { cat = enchantmentColors; }
                    case TOOLTIP_COLOR -> { cat = tooltipColors; }
                    case SLOT_HIGHLIGHT -> { cat = highlights; }
                    case MISCELLANEOUS -> { cat = miscellaneous; }
                    default -> { cat = miscellaneous; }
                }

                if(value instanceof ColorData colorValue) {
                    fieldBuilder = entryBuilder
                            .startColorField(Text.translatable(colorValue.getEntry().getTitle()), colorValue.getValue())
                            .setDefaultValue(colorValue.getDefaultValue())
                            .setSaveConsumer(newValue -> colorValue.setValue(TextColor.fromRgb(newValue)))
                            .setTooltip(Text.translatable(colorValue.getEntry().getTooltip()));
                }
                if(value instanceof BooleanData booleanValue) {
                    fieldBuilder = entryBuilder
                            .startBooleanToggle(Text.translatable(booleanValue.getEntry().getTitle()), booleanValue.getValue())
                            .setDefaultValue(booleanValue.getDefaultValue())
                            .setSaveConsumer(newValue -> booleanValue.setValue(newValue))
                            .setTooltip(Text.translatable(booleanValue.getEntry().getTooltip()));
                }
                if(value instanceof IntegerData integerValue) {
                    fieldBuilder = entryBuilder
                            .startIntSlider(Text.translatable(integerValue.getEntry().getTitle()), integerValue.getValue(), 1, 16)
                            .setDefaultValue(integerValue.getDefaultValue())
                            .setSaveConsumer(newValue -> integerValue.setValue(newValue))
                            .setTooltip(Text.translatable(integerValue.getEntry().getTooltip()));
                }
                assert fieldBuilder != null;
                cat.addEntry(fieldBuilder.build());
            }

            for(Map.Entry<String, EnchantmentColorDataEntry> item : ModConfig.individualColors.entrySet()) {
                individualEnchantmentColors.addEntry(
                        entryBuilder
                                .startColorField(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantment.min_color", item.getValue().enchantmentKey), item.getValue().minColor)
                                .setDefaultValue(item.getValue().getDefaultMinColor())
                                .setSaveConsumer(newValue -> item.getValue().minColor = TextColor.fromRgb(newValue))
                                .build()
                );
                individualEnchantmentColors.addEntry(
                        entryBuilder
                                .startColorField(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantment.max_color", item.getValue().enchantmentKey), item.getValue().maxColor)
                                .setDefaultValue(item.getValue().getDefaultMaxColor())
                                .setSaveConsumer(newValue -> item.getValue().maxColor = TextColor.fromRgb(newValue))
                                .build()
                );
                individualEnchantmentMeta.addEntry(
                        entryBuilder
                                .startIntField(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantment.order", item.getValue().enchantmentKey), item.getValue().order)
                                .setDefaultValue(item.getValue().getDefaultOrder())
                                .setSaveConsumer(newValue -> item.getValue().order = newValue)
                                .build()
                );
                individualEnchantmentMeta.addEntry(
                        entryBuilder
                                .startBooleanToggle(Text.translatable(EnchantipsClient.MODID + ".config.title.individual_enchantment.highlight_visibility", item.getValue().enchantmentKey), item.getValue().showHighlight)
                                .setDefaultValue(item.getValue().getDefaultHighlightVisibility())
                                .setSaveConsumer(newValue -> item.getValue().showHighlight = newValue)
                                .build()
                );
            }

            // save config
            builder.setSavingRunnable(ModConfig::writeConfig);
            return builder.build();
        };
    }

}
