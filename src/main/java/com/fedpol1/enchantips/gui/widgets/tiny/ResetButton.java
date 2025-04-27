package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ResetButton extends BaseSetter<Object> {

    protected final ConfigInfoLine<?> line;

    public ResetButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y);
        this.line = line;
    }

    @Override
    public void setValue(Object value) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, "config/reset"));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button, this.line::reset);
    }
}
