package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

public class ResetButton extends BaseSetter<ConfigInfoLine<?>, Object> {

    public ResetButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y, line);
    }

    @Override
    public boolean canTrigger() {
        return !this.line.isDefault();
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta, EnchantipsClient.id("config/reset"));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return super.mouseClicked(click, doubled, this.line::reset);
    }
}
