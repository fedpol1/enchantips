package com.fedpol1.enchantips;

import com.fedpol1.enchantips.config.*;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;

public class ModMenuEntry implements ModMenuApi {

    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return ModConfig::createGui;
    }

}
