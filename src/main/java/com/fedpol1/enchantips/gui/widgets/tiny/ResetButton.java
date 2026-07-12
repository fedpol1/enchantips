package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ResetButton extends BaseSetter<ConfigInfoLine<?>, Object> {

    public ResetButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y, line);
        this.action = () -> {
            if(!this.line.isSaved()) {
                this.line.undo();
            } else {
                this.line.reset();
                if(InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_LSHIFT) ||
                        InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), InputConstants.KEY_RSHIFT)) {
                    this.line.save();
                }
            }
        };
    }

    @Override
    public boolean canTrigger() {
        return !(this.line.isDefault() && this.line.isSaved());
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id("config/reset"));
    }

    protected List<Component> getTooltip() {
        if(!this.canTrigger()) { return null; }
        return List.of(
                Component.translatable("enchantips.gui.resetter.restore." + (this.line.isSaved() ? "default" : "value"))
        );
    }
}
