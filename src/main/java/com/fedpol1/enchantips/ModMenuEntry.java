package com.fedpol1.enchantips;

import com.fedpol1.enchantips.config.BooleanDataEntry;
import com.fedpol1.enchantips.config.BooleanDataEntry.*;
import com.fedpol1.enchantips.config.ColorDataEntry;
import com.fedpol1.enchantips.config.ColorDataEntry.*;
import com.fedpol1.enchantips.config.ModConfig;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import java.util.Map;

public class ModMenuEntry implements ModMenuApi{

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return screen -> {

            ConfigBuilder builder = ConfigBuilder.create()
                    .setParentScreen(MinecraftClient.getInstance().currentScreen)
                    .setTitle(Text.translatable(EnchantipsClient.MODID + ".config"));

            ConfigCategory miscellaneous = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.miscellaneous"));
            ConfigCategory colors = builder.getOrCreateCategory(Text.translatable(EnchantipsClient.MODID + ".config.title.category.colors"));

            ConfigEntryBuilder entryBuilder = builder.entryBuilder();

            for(Map.Entry<String, BooleanDataEntry.BooleanData> item : ModConfig.bools.entrySet()) {
                BooleanData value = item.getValue();
                miscellaneous.addEntry(
                        entryBuilder.startBooleanToggle(Text.translatable(value.getTitle()), value.getValue())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultValue())
                                .setSaveConsumer(newValue -> value.setValue(newValue))
                                .build()
                );
            }

            for(Map.Entry<String, ColorDataEntry.ColorData> item : ModConfig.colors.entrySet()) {
                ColorData value = item.getValue();
                colors.addEntry(
                        entryBuilder.startColorField(Text.translatable(value.getTitle()), value.getColor())
                                .setTooltip(Text.translatable(value.getTooltip()))
                                .setDefaultValue(value.getDefaultColor())
                                .setSaveConsumer(newValue -> value.setColor(newValue))
                                .build()
                );
            }

            // save config
            builder.setSavingRunnable(ModConfig::writeConfig);
            return builder.build();
        };
    }

}
