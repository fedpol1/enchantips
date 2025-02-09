package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class InfoLine extends InfoDelineator implements Drawable, Element {

    protected Text text;

    public InfoLine(Text text) {
        super();
        this.text = text;
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
        if(this.y < this.nearestScrollableParent.y) { return; }
        if(this.y + this.height > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return; }

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(
                renderer,
                this.text,
                this.x,
                this.y + 1,
                this.nearestScrollableParent.childColor,
                false
        );
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
