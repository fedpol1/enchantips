package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ResetButton extends BaseSetter<ConfigInfoLine<?>, Object> {

    public ResetButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y, line);
    }

    @Override
    public boolean canTrigger() {
        return !this.line.isDefault() || this.line.canSave();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id("config/reset"));
        List<Component> tooltipText = List.of(
                Component.translatable("enchantips.gui.resetter.restore." + (this.line.canSave() ? "value" : "default"))
        );
        if(this.isWithin(mouseX, mouseY)) {
            extractor.setComponentTooltipForNextFrame(Minecraft.getInstance().font, tooltipText, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return super.mouseClicked(click, doubled, () -> {
            if(this.line.canSave()) {
                this.line.undo();
            } else {
                this.line.reset();
                if(click.hasShiftDown()) {
                    this.line.save();
                }
            }
        });
    }
}
