package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.ConfigTree;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor2;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ConfigScreen extends BaseScreen {

    public ConfigScreen(@Nullable Screen parent) {
        super(Component.translatable(EnchantipsClient.MODID + ".gui.config"), parent);
        this.lines = (ScrollableInfoLineContainer) ConfigTree.root.accept(new ScreenVisitor2(), null);
    }

    @Override
    public void onClose() {
        super.onClose();
        ModConfig.writeConfig();
    }
}
