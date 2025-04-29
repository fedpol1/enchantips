package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.List;

public class SaveButton extends BaseSetter<Object> {

    protected final ConfigInfoLine<?> line;

    public SaveButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y);
        this.line = line;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        String path = this.line.canSave() ? "config/save" : "config/save_disabled";
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, path));
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
