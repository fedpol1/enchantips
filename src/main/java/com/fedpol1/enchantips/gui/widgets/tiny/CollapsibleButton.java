package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.CollapsibleInfoLine;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class CollapsibleButton extends BaseSetter<CollapsibleInfoLine, Object> {

    protected boolean collapsed;

    public CollapsibleButton(int x, int y, CollapsibleInfoLine line, boolean collapsed) {
        super(x, y, line);
        this.collapsed = collapsed;
        this.action = () -> this.collapsed = !this.collapsed;
    }

    public boolean isCollapsed() {
        return this.collapsed;
    }

    public boolean canTrigger() {
        return this.line.numChildren() != 0;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        String path = this.collapsed ? "enchantment_info/expand" : "enchantment_info/collapse";
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id(path));
    }
}
