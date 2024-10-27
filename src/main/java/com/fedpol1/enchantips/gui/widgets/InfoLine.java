package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class InfoLine extends InfoDelineator implements Drawable, Element {

    protected static int LINE_HEIGHT = 11;
    protected static int INDENTATION = 16;

    protected Text text;

    public InfoLine(int x, int y, int width, Text text) {
        super(x, y, width);
        this.text = text;
    }

    public void refresh(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        // no height lol
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWrapped(MinecraftClient.getInstance().textRenderer, this.text, this.x, this.y, this.width, 0x404040);
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
