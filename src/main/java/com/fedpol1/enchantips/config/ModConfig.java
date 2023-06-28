package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.deserializer.ConfigTreeDeserializer;
import com.fedpol1.enchantips.config.serializer.ConfigTreeSerializer;
import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.config.tree.visitor.*;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".json").toFile();

    public static void registerConfig() {
        EnchantipsClient.LOGGER.info("Initializing configs");
        ModCategory.init();
        ModOption.init();
        GroupNode gn;
        for(Enchantment current : Registries.ENCHANTMENT) {
            gn = (GroupNode) ModCategory.INDIVIDUAL_ENCHANTMENTS.addChild(new GroupNode(current));
            ModConfigData.enchantmentData.put(current, gn);
            gn.addChild(new OptionNode<>(new ModOption<>(new ColorOption(EnchantmentAppearanceHelper.getDefaultMinColor(current).getRGB()),  ModConfigData.MIN_COLOR_KEY, 0)));
            gn.addChild(new OptionNode<>(new ModOption<>(new ColorOption(EnchantmentAppearanceHelper.getDefaultMaxColor(current).getRGB()),  ModConfigData.MAX_COLOR_KEY, 0)));
            gn.addChild(new OptionNode<>(new ModOption<>(new IntegerOption(EnchantmentAppearanceHelper.getDefaultOrder(current), -2000000000, 2000000000, 0),  ModConfigData.ORDER_KEY, 1)));
            gn.addChild(new OptionNode<>(new ModOption<>(new BooleanOption(EnchantmentAppearanceHelper.getDefaultHighlightVisibility(current)),  ModConfigData.HIGHLIGHT_KEY, 0)));
        }
        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    public static void readConfig() throws NullPointerException {
        try {
            ModConfig.CONFIG_FILE.createNewFile();
            Scanner sc = new Scanner(ModConfig.CONFIG_FILE);
            StringBuilder fileContents = new StringBuilder();
            while (sc.hasNextLine()) {
                fileContents.append(sc.nextLine());
            }
            new GsonBuilder()
                    .registerTypeAdapter(ConfigTree.class, new ConfigTreeDeserializer())
                    .create()
                    .fromJson(fileContents.toString(), ConfigTree.class);
        }
        catch (IOException e) {
            EnchantipsClient.LOGGER.error("Could not read configuration file.\n" + e.getMessage());
        }
    }

    public static void writeConfig() {
        String json = new GsonBuilder()
                .registerTypeAdapter(ConfigTree.class, new ConfigTreeSerializer())
                .setPrettyPrinting()
                .create()
                .toJson(ConfigTree.root);

        try {
            FileWriter fw = new FileWriter(ModConfig.CONFIG_FILE);
            fw.write(json);
            fw.close();
        }
        catch (IOException e) {
            EnchantipsClient.LOGGER.error("Could not write configuration file.\n" + e.getMessage());
        }
    }

    public static Screen createGui(Screen parent) {
        return ((YetAnotherConfigLib) ConfigTree.root.accept(new ScreenVisitor(), null)).generateScreen(parent);
    }
}
