package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.gui.widgets.info_line.ActionConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ActionButton extends BaseSetter<ConfigInfoLine<Runnable>, Runnable> {

    public ActionButton(int x, int y, ActionConfigInfoLine line, Runnable action) {
        super(x, y, line);
        this.action = () -> {
            action.run();
            ((ActionConfigInfoLine)this.line).cascadeRefresh();
            ModConfig.writeConfig();
        };
    }

    public int getWidth() {
        return 19;
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        String path = "config/action";
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id(path));
    }

    @Override
    protected List<Component> getTooltip() {
        if(!this.canTrigger()) { return null; }
        return List.of(
                Component.translatable("enchantips.gui.setter.action")
        );
    }
}
