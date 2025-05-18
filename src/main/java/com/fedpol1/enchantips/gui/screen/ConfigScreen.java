package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.ConfigTree;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor2;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class ConfigScreen extends BaseScreen {

    public ConfigScreen(Text title, @Nullable Screen parent) {
        super(title, parent);
        this.lines = (ScrollableInfoLineContainer) ConfigTree.root.accept(new ScreenVisitor2(), null);
    }

    @Override
    public void close() {
        super.close();
        ModConfig.writeConfig();
    }
}
