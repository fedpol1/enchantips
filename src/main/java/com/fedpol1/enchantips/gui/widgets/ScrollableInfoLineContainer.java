package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public class ScrollableInfoLineContainer extends InfoLineContainer implements Drawable, Element {

    //
    private static final Identifier SCROLLER_TEXTURE = Identifier.ofVanilla("widget/scroller");
    private static final Identifier SCROLLER_BACKGROUND_TEXTURE = Identifier.ofVanilla("widget/scroller_background");

    protected int padding; // not really
    protected int scrollHeight;

    public ScrollableInfoLineContainer(int padding) {
        super();
        this.padding = padding;
        this.scrollHeight = 0;
        this.nearestScrollableParent = this;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height / InfoDelineator.LINE_HEIGHT * InfoDelineator.LINE_HEIGHT; // induce floor
    }

    public int getHeight() {
        return this.height;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);

        int scrollbarLength = this.getHeight() + 2 * this.padding;
        int scrollbarX = this.x + this.width + this.padding - 6;
        int scrollbarY = this.y - this.padding;
        int scrollerLength = (int) Math.clamp((float) scrollbarLength * this.getHeight() / super.getHeight(), 32, scrollbarLength);
        int scrollerY = Math.clamp (
                (int) (scrollbarY - (float) this.scrollHeight * (scrollbarLength - scrollerLength) / (super.getHeight() - this.getHeight())),
                scrollbarY,
                scrollbarY + scrollbarLength - scrollerLength
        );

        context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_BACKGROUND_TEXTURE, scrollbarX, scrollbarY, 6, scrollbarLength);
        context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_TEXTURE, scrollbarX, scrollerY, 6, scrollerLength);
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
