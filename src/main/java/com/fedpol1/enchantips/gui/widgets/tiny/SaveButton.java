package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class SaveButton extends BaseSetter<ConfigInfoLine<?>, Object> {

    public SaveButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y, line);
    }

    @Override
    public boolean canTrigger() {
        return this.line.canSave();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/save"));
        List<Text> tooltipText = this.line.getSaveButtonTooltip();
        if(tooltipText != null && this.isWithin(mouseX, mouseY)) {
            context.drawTooltip(MinecraftClient.getInstance().textRenderer, tooltipText, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button, this.line::save);
    }
}
