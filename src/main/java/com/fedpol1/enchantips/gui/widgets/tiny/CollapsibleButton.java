package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class CollapsibleButton extends BaseSetter<Object> {

    protected boolean collapsed;

    public CollapsibleButton(int x, int y, boolean collapsed) {
        super(x, y);
        this.collapsed = collapsed;
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    @Override
    public void setValue(Object value) {}

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        String path = this.collapsed ? "enchantment_info/expand" : "enchantment_info/collapse";
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, path));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button, () -> this.collapsed = !this.collapsed);
    }
}
