package com.fedpol1.enchantips.gui.widgets.info_line;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class InfoLine extends InfoDelineator implements Drawable, Element {

    public InfoLine(Text text) {
        super(text);
    }

    public void refresh(int index) {
        this.x = this.parent.x;
        this.y = this.parent.y + this.parent.getHeight(index);
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = this.parent.width;
        this.height = this.getHeight(index);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!this.shouldRender(context, mouseX, mouseY, delta)) { return; }
        this.renderText(context, 0, mouseX, mouseY, delta);
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return false;
    }
}
