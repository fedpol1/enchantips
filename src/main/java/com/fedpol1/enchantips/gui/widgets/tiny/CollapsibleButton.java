package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.CollapsibleInfoLine;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class CollapsibleButton extends BaseSetter<CollapsibleInfoLine, Object> {

    protected boolean collapsed;

    public CollapsibleButton(int x, int y, CollapsibleInfoLine line, boolean collapsed) {
        super(x, y, line);
        this.collapsed = collapsed;
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    public boolean canTrigger() {
        return this.line.numChildren() != 0;
    }

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
