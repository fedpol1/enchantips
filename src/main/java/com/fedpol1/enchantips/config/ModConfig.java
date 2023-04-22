package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.data.*;
import com.fedpol1.enchantips.config.deserializer.ConfigTreeDeserializer;
import com.fedpol1.enchantips.config.serializer.ConfigTreeSerializer;
import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.config.tree.visitor.*;
import com.google.gson.GsonBuilder;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class ModConfig {

    private static final File CONFIG_FILE = FabricLoader.getInstance().getConfigDir().resolve(EnchantipsClient.MODID + ".properties").toFile();

    public static void registerConfig() {
        EnchantipsClient.LOGGER.info("Initializing configs");
        ModCategory.init();
        ModOption.init();
        GroupNode gn;
        for(Enchantment current : Registries.ENCHANTMENT) {
            gn = (GroupNode) ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().addChild(new GroupNode(current));
            ModConfigData.enchantmentData.put(current, gn);
            gn.addChild(new OptionNode<>(new ColorDataEntry(ModConfigData.MIN_COLOR_KEY, ((EnchantmentAccess)current).enchantipsGetDefaultMinColor().getRGB(), false)));
            gn.addChild(new OptionNode<>(new ColorDataEntry(ModConfigData.MAX_COLOR_KEY, ((EnchantmentAccess)current).enchantipsGetDefaultMaxColor().getRGB(), false)));
            gn.addChild(new OptionNode<>(new IntegerDataEntry(ModConfigData.ORDER_KEY, ((EnchantmentAccess)current).enchantipsGetDefaultOrder(), -2000000000, 2000000000, 0, true)));
            gn.addChild(new OptionNode<>(new BooleanDataEntry(ModConfigData.HIGHLIGHT_KEY, ((EnchantmentAccess)current).enchantipsGetDefaultHighlightVisibility(), false)));
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
