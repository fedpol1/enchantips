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
        context.drawHorizontalLine(this.x, this.x + width, this.y, 0xff0000ff);
        context.drawHorizontalLine(this.x, this.x + width, this.y + LINE_HEIGHT, 0xff0000ff);
        context.drawVerticalLine(this.x, this.y, this.y + LINE_HEIGHT, 0xff0000ff);
        context.drawVerticalLine(this.x + this.width, this.y, this.y + LINE_HEIGHT, 0xff0000ff);

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        context.drawTextWrapped(renderer, renderer.trimToWidth(this.text, this.width), this.x, this.y + 1, this.width, 0x404040);
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
