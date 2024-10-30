package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

public class ScrollableInfoLineContainer extends InfoLineContainer implements Drawable, Element {

    protected int padding;
    protected int scrollHeight;

    public ScrollableInfoLineContainer() {
        super();
        this.scrollHeight = 0;
        this.nearestScrollableParent = this;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public int getHeight() {
        return this.height;
    }

    public void refresh(int index) {
        for(int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }
    }

    public void scroll(int s) {
        int scroll = s * InfoDelineator.LINE_HEIGHT;
        InfoDelineator last = this.getLast();
        int scrollingCapacity = Math.max(0, last.y + last.height - (this.y + this.height));
        this.scrollHeight += Math.clamp(scroll, -scrollingCapacity, -this.scrollHeight);
        this.refresh(0);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
