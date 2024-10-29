package com.fedpol1.enchantips.gui.widgets;

import com.fedpol1.enchantips.gui.EnchantmentInfoScreen;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

public class ScrollableInfoLineContainer extends InfoLineContainer implements Drawable, Element {

    protected int scrollHeight;
    private final EnchantmentInfoScreen screen;

    public ScrollableInfoLineContainer(EnchantmentInfoScreen screen) {
        super();
        this.screen = screen;
        this.scrollHeight = 0;
        this.nearestScrollableParent = this;
    }

    public int getHeight() {
        return this.height;
    }

    public void refresh(int index) {
        this.x = this.screen.width/10 + 15;
        this.y = this.screen.height/10 + 24;
        this.width = this.screen.width * 8/10 - 30;
        this.height = this.screen.height * 8/10 - 39;

        for(int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }
    }

    public void scroll(int s) {
        int scroll = s * InfoDelineator.LINE_HEIGHT;
        InfoDelineator last = this.getLast();
        int scrollingCapacity = Math.max(0, last.y + last.height - this.y - this.height);
        this.scrollHeight += Math.clamp(scroll, -scrollingCapacity, -this.scrollHeight);
        this.refresh(0);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
