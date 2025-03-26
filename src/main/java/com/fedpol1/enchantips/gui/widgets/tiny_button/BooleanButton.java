package com.fedpol1.enchantips.gui.widgets.tiny_button;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BooleanButton extends BaseButton {

    protected boolean state;

    public BooleanButton(int x, int y, boolean state) {
        super(x, y);
        this.state = state;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        String path = this.state ? "config/true" : "config/false";
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, path));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button, () -> this.state = !this.state);
    }
}
