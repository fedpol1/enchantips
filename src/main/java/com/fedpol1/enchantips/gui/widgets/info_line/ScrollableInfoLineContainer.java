package com.fedpol1.enchantips.gui.widgets.info_line;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class ScrollableInfoLineContainer extends InfoLineContainer implements InfoMultiLine, Renderable {

    //
    private static final Identifier SCROLLER_TEXTURE = Identifier.withDefaultNamespace("widget/scroller");
    private static final Identifier SCROLLER_BACKGROUND_TEXTURE = Identifier.withDefaultNamespace("widget/scroller_background");
    private static final int SCROLLER_WIDTH = 6;

    protected int childColor;
    protected int padding; // not really
    protected int scrollHeight;
    private int scrollbarX;
    private int scrollbarY;
    private int scrollbarHeight;
    private int scrollerY;
    private int scrollerHeight;
    private boolean scrolling;
    private InfoLine focusedLine;

    public ScrollableInfoLineContainer(int childColor, int padding) {
        super(null);
        this.childColor = childColor;
        this.padding = padding;
        this.scrollHeight = 0;
        this.scrolling = false;
        this.nearestScrollableParent = this;
        this.focusedLine = null;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setDimensions(int width, int height) {
        this.width = width;
        this.height = height / InfoLine.LINE_HEIGHT * InfoLine.LINE_HEIGHT; // induce floor
        if(this.height < InfoLine.LINE_HEIGHT) {
            throw new IllegalArgumentException("Height cannot be less than line height.");
        }
    }

    public int getWidth() {
        return super.getWidth() -  SCROLLER_WIDTH / 2;
    }

    public int getHeight() {
        return this.height;
    }

    public boolean scrollerVisible() {
        return this.scrollerHeight < this.scrollbarHeight;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta);

        if(this.scrollerVisible()) {
            extractor.blitSprite(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, SCROLLER_BACKGROUND_TEXTURE, this.scrollbarX, this.scrollbarY, 6, this.scrollbarHeight);
            extractor.blitSprite(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, SCROLLER_TEXTURE, this.scrollbarX, this.scrollerY, 6, this.scrollerHeight);
        }
    }

    public void refresh(int index) {
        for(int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }

        this.scrollbarHeight = this.getHeight() + 2 * this.padding;
        this.scrollbarX = this.x + this.width + this.padding - SCROLLER_WIDTH;
        this.scrollbarY = this.y - this.padding;
        this.scrollerHeight = (int) Math.clamp(
                (float) this.scrollbarHeight * this.getHeight() / super.getHeight(),
                Math.min(32, this.scrollbarHeight/4),
                this.scrollbarHeight
        );
        this.scrollerY = Math.clamp (
                (int) (scrollbarY - (float) this.scrollHeight * (this.scrollbarHeight - this.scrollerHeight) / (super.getHeight() - this.getHeight())),
                this.scrollbarY,
                this.scrollbarY + this.scrollbarHeight - this.scrollerHeight
        );
    }

    public void scroll(int s) {
        int scroll = s * InfoLine.LINE_HEIGHT;
        InfoLine last = this.getLast();
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
        this.scroll((int) -((this.scrollHeight + destinationFraction * (super.getHeight() - this.getHeight())) / InfoLine.LINE_HEIGHT));
    }

    private boolean isWithinScrollbar(double mouseX, double mouseY) {
        return mouseX >= this.scrollbarX && mouseX < this.scrollbarX + SCROLLER_WIDTH &&
                mouseY >= this.scrollbarY && mouseY < this.scrollbarY + this.scrollbarHeight;
    }

    public void setFocusedLine(InfoLine line) {
        if(this.focusedLine != null && this.focusedLine != line) {
            this.focusedLine.onUnfocus();
        }
        this.focusedLine = line;
        if(this.focusedLine == null) { return; }

        // scroll should move such that focused line is in view
        if(this.focusedLine.y < this.y) {
            this.scroll((this.y - this.focusedLine.y) / InfoLine.LINE_HEIGHT);
        }
        if(this.focusedLine.y >= this.y + this.height) {
            this.scroll((this.y + this.height - this.focusedLine.y) / InfoLine.LINE_HEIGHT - 1);
        }
    }

    public boolean compareFocusedLine(InfoLine line) {
        return this.focusedLine == line;
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.scrolling = click.button() == 0 && this.isWithinScrollbar(click.x(), click.y());
        if(this.scrolling) {
            this.setFocusedLine(null);
            this.scrollTo((int) click.y());
        }

        if(!super.mouseClicked(click, doubled)) {
            this.setFocusedLine(null);
            return false;
        }
        return true;
    }

    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        if(this.scrolling) {
            this.scrollTo((int) click.y());
            return true;
        }
        return false;
    }

    public boolean keyPressed(KeyEvent input) {
        if(this.focusedLine == null && !this.lines.isEmpty() && InfoLine.navigationDirection(input) != null) {
            this.lines.getFirst().takeFocus();
            this.lines.getFirst().focusSetterAt(0);
            return true;
        }
        return super.keyPressed(input);
    }

    public boolean charTyped(CharacterEvent input) {
        return super.charTyped(input);
    }
}
