package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class CollapsibleInfoLine extends InfoDelineator implements Drawable, Element {

    private final Text text;
    private final InfoLineContainer children;
    private boolean collapsed;

    public CollapsibleInfoLine(Text text, int x, int y, int width, int height, InfoDelineator... children) {
        super(x, y, width);
        this.text = text;
        this.children = new InfoLineContainer(x + 16, y + 11, width - LINE_HEIGHT, height - LINE_HEIGHT, children);
        this.collapsed = true;
    }

    @Override
    public void refresh(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        // no height lol
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        context.drawTextWrapped(MinecraftClient.getInstance().textRenderer, this.text, this.x + 16, this.y, this.width - 16, 0x404040);
        if(!this.collapsed) {
            this.children.render(context, mouseX, mouseY, delta);
        }
    }
}
