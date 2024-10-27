package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;

public abstract class InfoDelineator implements Drawable, Element {

    protected static final int LINE_HEIGHT = 11;
    protected static final int INDENTATION = 16;

    protected int x;
    protected int y;
    protected int width;
    protected boolean focused = false;

    public InfoDelineator(int x, int y, int width) {
        this.x = x;
        this.y = y;
        this.width = width;
    }

    @Override
    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    public abstract void refresh(int x, int y, int width, int height);

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }
}
