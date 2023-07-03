package com.fedpol1.enchantips;

import com.fedpol1.enchantips.event.EnchantmentScreenEvents;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class EnchantipsClient implements ClientModInitializer {

    public static final String MODID = "enchantips";
    public static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitializeClient() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if(screen instanceof EnchantmentScreen) {
                ScreenMouseEvents.afterMouseScroll(screen).register(EnchantmentScreenEvents::onScroll);
            }
        });
    }
}
