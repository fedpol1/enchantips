package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.deserializer.ConfigTreeDeserializer;
import com.fedpol1.enchantips.config.deserializer.OldConfigTreeDeserializer;
import com.fedpol1.enchantips.config.serializer.ConfigTreeSerializer;
import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.config.tree.visitor.*;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".json").toFile();

    public static void registerConfig() {
        EnchantipsClient.LOGGER.info("Initializing Enchantips configs");
        ModCategory.init();
        ModOption.init();

        ModConfig.readConfig();
        ModConfig.writeConfig();
    }

    public static void registerPerEnchantmentConfig(DynamicRegistryManager registryManager) {
        Optional<RegistryWrapper.Impl<Enchantment>> optionalWrapper = registryManager.getOptionalWrapper(RegistryKeys.ENCHANTMENT);
        if(optionalWrapper.isEmpty()) { return; }
        RegistryWrapper.Impl<Enchantment> wrapper = optionalWrapper.get();
        int existingEnchantments = 0;
        int newEnchantments = 0;

        for(RegistryKey<Enchantment> key : wrapper.streamKeys().toList()) {
            EnchantmentGroupNode group = (EnchantmentGroupNode) ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChild(key.getValue().toString());
            if(group == null) {
                group = ModCategory.INDIVIDUAL_ENCHANTMENTS.addEnchantmentGroup(key);
                newEnchantments++;
            } else {
                existingEnchantments++;
            }
            Enchantment enchantment = registryManager.get(RegistryKeys.ENCHANTMENT).get(key);
            if(enchantment == null) { continue; }
            group.setDescription(enchantment.description());
        }

        if(newEnchantments > 0) {
            ModConfig.writeConfig();
        }

        EnchantipsClient.LOGGER.info("Found {} pre-existing enchantments and {} new enchantments.", existingEnchantments, newEnchantments);
    }

    public static void deregisterUnusedEnchantmentConfig() {
        TreeSet<String> toRemove = new TreeSet<>();

        for(Map.Entry<String, Node> entry : ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren()) {
            if(entry.getValue() instanceof EnchantmentGroupNode ench && !ench.isKnown()) {
                toRemove.add(entry.getKey());
            }
        }
        for(String element : toRemove) {
            ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().removeChild(element);
        }
        EnchantipsClient.LOGGER.info("Pruned {} enchantments.", toRemove.size());
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
        catch (IOException | JsonSyntaxException e) {
            EnchantipsClient.LOGGER.error("Could not read configuration file.\n{}", e.getMessage());
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
            EnchantipsClient.LOGGER.error("Could not write configuration file.\n{}", e.getMessage());
        }
    }

    public static Screen createGui(Screen parent) {
        return ((YetAnotherConfigLib) ConfigTree.root.accept(new ScreenVisitor(), null)).generateScreen(parent);
    }
}
