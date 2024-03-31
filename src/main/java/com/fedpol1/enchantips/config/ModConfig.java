package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.deserializer.ConfigTreeDeserializer;
import com.fedpol1.enchantips.config.deserializer.OldConfigTreeDeserializer;
import com.fedpol1.enchantips.config.serializer.ConfigTreeSerializer;
import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.config.tree.visitor.*;
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
        EnchantipsClient.LOGGER.info("Initializing Enchantips configs");
        ModCategory.init();
        ModOption.init();

        for(Enchantment current : Registries.ENCHANTMENT) {
            ModCategory.INDIVIDUAL_ENCHANTMENTS.addGroup(current);
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
            try {
                new GsonBuilder()
                        .registerTypeAdapter(ConfigTree.class, new ConfigTreeDeserializer())
                        .create()
                        .fromJson(fileContents.toString(), ConfigTree.class);
            }
            catch (Exception e) {
                EnchantipsClient.LOGGER.info("Falling back on old config format.");
                new GsonBuilder()
                        .registerTypeAdapter(ConfigTree.class, new OldConfigTreeDeserializer())
                        .create()
                        .fromJson(fileContents.toString(), ConfigTree.class);
            }
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
