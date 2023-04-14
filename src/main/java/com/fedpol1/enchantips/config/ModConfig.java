package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.data.*;
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
            gn = (GroupNode) ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().addChild(new GroupNode(current));
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
