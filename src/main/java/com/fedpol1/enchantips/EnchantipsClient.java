package com.fedpol1.enchantips;

import com.fedpol1.enchantips.event.EnchantmentScreenEvents;
import com.fedpol1.enchantips.gui.screen.ConfigScreen;
import com.fedpol1.enchantips.gui.screen.EnchantmentInfoScreen;
import com.fedpol1.enchantips.resources.SymbolReloadListener;
import com.fedpol1.enchantips.resources.SymbolSetReloadListener;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceType;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class EnchantipsClient implements ClientModInitializer {

    public static final String MODID = "enchantips";
    public static final Logger LOGGER = LogManager.getLogger();

    private static final KeyBinding ENCHANTMENT_INFO_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.enchantips.enchantment_info",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN, // unbound by default
            "key.categories.misc"
    ));
    private static final KeyBinding CONFIG_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.enchantips.config", // temporary lol
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_UNKNOWN, // unbound by default
            "key.categories.misc"
    ));

    @Override
    public void onInitializeClient() {
        ScreenEvents.BEFORE_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if(screen instanceof EnchantmentScreen) {
                ScreenMouseEvents.afterMouseScroll(screen).register(EnchantmentScreenEvents::onScroll);
            }
        });

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            Screen current = MinecraftClient.getInstance().currentScreen;
            if(ENCHANTMENT_INFO_KEY.wasPressed() && !(current instanceof EnchantmentInfoScreen)) {
                MinecraftClient.getInstance().setScreen(
                        new EnchantmentInfoScreen(Text.translatable(EnchantipsClient.MODID + ".gui.enchantment_info"), current)
                );
            }
            if (CONFIG_KEY.wasPressed() && !(current instanceof ConfigScreen)) {
                MinecraftClient.getInstance().setScreen(
                        new ConfigScreen(Text.translatable(EnchantipsClient.MODID + ".gui.config"), current)
                );
            }
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SymbolReloadListener());
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SymbolSetReloadListener());
    }
}
