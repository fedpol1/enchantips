package com.fedpol1.enchantips;

import com.fedpol1.enchantips.event.EnchantmentScreenEvents;
import com.fedpol1.enchantips.gui.EnchantmentInfoScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

public class EnchantipsClient implements ClientModInitializer {

    public static final String MODID = "enchantips";
    public static final Identifier SYMBOL_FONT = Identifier.of(EnchantipsClient.MODID, "symbols");
    public static final Logger LOGGER = LogManager.getLogger();

    private static KeyBinding ENCHANTMENT_INFO_KEY = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.enchantips.enchantment_info",
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
                MinecraftClient.getInstance().setScreen(new EnchantmentInfoScreen(Text.of("aaaaaaaa"), current));
            }
        });
    }
}
