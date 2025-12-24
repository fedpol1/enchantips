package com.fedpol1.enchantips;

import com.fedpol1.enchantips.event.EnchantmentScreenEvents;
import com.fedpol1.enchantips.gui.screen.EnchantmentInfoScreen;
import com.fedpol1.enchantips.resources.SymbolSetReloadListener;
import com.mojang.blaze3d.platform.InputConstants;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class EnchantipsClient implements ClientModInitializer {

    public static final String MODID = "enchantips";
    public static final Logger LOGGER = LogManager.getLogger();
    public static SymbolSetReloadListener symbolSetReloadListener = new SymbolSetReloadListener();

    private static final KeyMapping ENCHANTMENT_INFO_KEY = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.enchantips.enchantment_info",
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN, // unbound by default
            KeyMapping.Category.MISC
    ));

    @Override
    public void onInitializeClient() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if(screen instanceof EnchantmentScreen) {
                ScreenMouseEvents.afterMouseScroll(screen).register(EnchantmentScreenEvents::onScroll);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Screen current = Minecraft.getInstance().screen;
            if(ENCHANTMENT_INFO_KEY.consumeClick() && !(current instanceof EnchantmentInfoScreen)) {
                Minecraft.getInstance().setScreen(
                        new EnchantmentInfoScreen(current)
                );
            }
        });

        Identifier symbolSetReloader = EnchantipsClient.id(SymbolSetReloadListener.DIRECTORY);

        ResourceLoader.get(PackType.CLIENT_RESOURCES).registerReloader(
                symbolSetReloader, symbolSetReloadListener
        );
    }

    public static Identifier id(String s) {
        return Identifier.fromNamespaceAndPath(EnchantipsClient.MODID, s);
    }
}
