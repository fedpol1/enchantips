package com.fedpol1.enchantips;

import com.fedpol1.enchantips.config.ModConfig;

import com.fedpol1.enchantips.event.KeyInputHandler;
import net.fabricmc.api.ClientModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnchantipsClient implements ClientModInitializer {

    public static final String MODID = "enchantips";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        ModConfig.registerConfig();
        KeyInputHandler.registerKeybind();
    }
}
