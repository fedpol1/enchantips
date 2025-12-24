package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class SaveButton extends BaseSetter<ConfigInfoLine<?>, Object> {

    public SaveButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y, line);
    }

    @Override
    public boolean canTrigger() {
        return this.line.canSave();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta, EnchantipsClient.id("config/save"));
        List<Component> tooltipText = this.line.getSaveButtonTooltip();
        if(tooltipText != null && this.isWithin(mouseX, mouseY)) {
            context.setComponentTooltipForNextFrame(Minecraft.getInstance().font, tooltipText, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return super.mouseClicked(click, doubled, this.line::save);
    }
}
