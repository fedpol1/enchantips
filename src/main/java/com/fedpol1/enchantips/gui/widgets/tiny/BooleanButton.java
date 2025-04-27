package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BooleanButton extends BaseSetter<Boolean> {

    protected boolean value;

    public BooleanButton(int x, int y, boolean state) {
        super(x, y);
        this.value = state;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        String path = this.value ? "config/true" : "config/false";
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, path));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button, () -> this.value = !this.value);
    }

    public void setValue(Boolean value) {
        this.value = value;
    }
}
