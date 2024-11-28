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
    private static final int SCROLLER_WIDTH = 6;

    protected int padding; // not really
    protected int scrollHeight;
    private int scrollbarX;
    private int scrollbarY;
    private int scrollbarHeight;
    private int scrollerY;
    private int scrollerHeight;
    private boolean scrolling;

    public ScrollableInfoLineContainer(int padding) {
        super();
        this.padding = padding;
        this.scrollHeight = 0;
        this.scrolling = false;
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

        if(this.scrollerHeight < this.scrollbarHeight) {
            context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_BACKGROUND_TEXTURE, this.scrollbarX, this.scrollbarY, 6, this.scrollbarHeight);
            context.drawGuiTexture(RenderLayer::getGuiTextured, SCROLLER_TEXTURE, this.scrollbarX, this.scrollerY, 6, this.scrollerHeight);
        }
    }

    public void refresh(int index) {
        for(int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }

        this.scrollbarHeight = this.getHeight() + 2 * this.padding;
        this.scrollbarX = this.x + this.width + this.padding - SCROLLER_WIDTH;
        this.scrollbarY = this.y - this.padding;
        this.scrollerHeight = (int) Math.clamp((float) this.scrollbarHeight * this.getHeight() / super.getHeight(), 32, this.scrollbarHeight);
        this.scrollerY = Math.clamp (
                (int) (scrollbarY - (float) this.scrollHeight * (this.scrollbarHeight - this.scrollerHeight) / (super.getHeight() - this.getHeight())),
                this.scrollbarY,
                this.scrollbarY + this.scrollbarHeight - this.scrollerHeight
        );
    }

    public void scroll(int s) {
        int scroll = s * InfoDelineator.LINE_HEIGHT;
        InfoDelineator last = this.getLast();
        int scrollingCapacity = Math.max(0, last.y + last.height - (this.y + this.height));
        this.scrollHeight += Math.clamp(scroll, -scrollingCapacity, -this.scrollHeight);
        this.refresh(0);
    }

    private void scrollTo(int destination) {
        float destinationFraction = Math.clamp(
                ((float) destination - this.scrollbarY - this.scrollerHeight/2.0f) / (this.scrollbarHeight - this.scrollerHeight),
                0.0f,
                1.0f
        );
        this.scroll((int) -((this.scrollHeight + destinationFraction * (super.getHeight() - this.getHeight())) / InfoDelineator.LINE_HEIGHT));
    }

    private boolean isWithinScrollbar(double mouseX, double mouseY) {
        return mouseX >= this.scrollbarX && mouseX < this.scrollbarX + SCROLLER_WIDTH &&
                mouseY >= this.scrollbarY && mouseY < this.scrollbarY + this.scrollbarHeight;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = button == 0 && this.isWithinScrollbar(mouseX, mouseY);
        if(this.scrolling) {
            this.scrollTo((int) mouseY);
        }

        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if(super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY)) { return true; }
        if(this.scrolling) {
            this.scrollTo((int) mouseY);
            return true;
        }
        return false;
    }
}
