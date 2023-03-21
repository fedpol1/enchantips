package com.fedpol1.enchantips;

import com.fedpol1.enchantips.config.*;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuEntry implements ModMenuApi {

    public ConfigScreenFactory<Screen> getModConfigScreenFactory() {
        return ModConfig::createGui;
    }

}
