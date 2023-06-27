package com.fedpol1.enchantips.event;

import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class KeyInputHandler {

    public static final String KEY_CATEGORY_MISC = "key.categories.misc";
    public static final String KEY_NAME_SCROLL_UP = "key.enchantips.scroll.up";
    public static final String KEY_NAME_SCROLL_DOWN = "key.enchantips.scroll.down";

    public static KeyBinding scrollUpKey;
    public static KeyBinding scrollDownKey;

    public static void registerKeybind() {
        scrollUpKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_NAME_SCROLL_UP, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_UP, // The keycode of the key
                KEY_CATEGORY_MISC // The translation key of the keybinding's category.
        ));
        scrollDownKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                KEY_NAME_SCROLL_DOWN, // The translation key of the keybinding's name
                InputUtil.Type.KEYSYM, // The type of the keybinding, KEYSYM for keyboard, MOUSE for mouse.
                GLFW.GLFW_KEY_DOWN, // The keycode of the key
                KEY_CATEGORY_MISC // The translation key of the keybinding's category.
        ));
    }
}
