package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.BaseSetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.ArrayList;

public class InfoLine implements Drawable, Element {

    public static final int LINE_HEIGHT = 10;
    public static final int INDENTATION = 16;

    protected Text text;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean focused = false;
    protected InfoLineContainer parent;
    protected ScrollableInfoLineContainer nearestScrollableParent;
    protected final ArrayList<BaseSetter<?, ?>> setters;

    public InfoLine(Text text) {
        this.text = text;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.parent = null;
        this.nearestScrollableParent = null;
        this.setters = new ArrayList<>();
    }

    public int getHeight(int index) {
        return InfoLine.LINE_HEIGHT;
    }

    public int getHeight() {
        return this.getHeight(0);
    }

    public void setHeight() {
        this.height = InfoLine.LINE_HEIGHT;
    }

    public boolean isWithin(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.getHeight();
    }

    public void renderText(DrawContext context, int startOffset, int mouseX, int mouseY, float delta) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = renderer.getWidth(this.text);
        int scrollRange = textWidth + InfoLine.INDENTATION;
        boolean drawExtra = this.width < textWidth + startOffset;
        double dynamicOffset = (24.0 * (double) Util.getMeasuringTimeMs() / 1000.0) % scrollRange;

        context.enableScissor(
                this.x + startOffset,
                this.y,
                this.x + this.width,
                this.y + height
        );
        context.drawText(
                renderer,
                this.text,
                this.x + startOffset - (drawExtra ? (int) dynamicOffset : 0),
                this.y + 1,
                this.nearestScrollableParent.childColor,
                false
        );
        if(drawExtra) {
            context.drawText(
                    renderer,
                    this.text,
                    this.x + startOffset - (int) dynamicOffset + InfoLine.INDENTATION + renderer.getWidth(this.text),
                    this.y + 1,
                    this.nearestScrollableParent.childColor,
                    false
            );
        }
        context.disableScissor();
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!this.shouldRender(context, mouseX, mouseY, delta)) { return; }

        int offset = 0;
        for(BaseSetter<?, ?> setter : this.setters) {
            setter.render(context, mouseX, mouseY, delta);
            offset += setter.getWidth() + 1;
        }
        this.renderText(context, offset, mouseX, mouseY, delta);
    }

    public boolean shouldRender(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.y < this.nearestScrollableParent.y) { return false; }
        if(this.y + this.height > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return false; }
        return true;
    }

    public  void refresh(int index) {
        this.x = this.parent.x;
        this.y = this.parent.y + this.parent.getHeight(index);
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = this.parent.getWidth();
        this.height = this.getHeight(index);

        int offset = 0;
        for(BaseSetter<?, ?> setter : this.setters) {
            setter.setPosition(this.x + offset, this.y);
            offset += setter.getWidth() + 1;
        }
    }

    public InfoLineContainer getParent() {
        return this.parent;
    }

    public void setNearestScrollableParent(ScrollableInfoLineContainer container) {
        this.nearestScrollableParent = container;
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    public boolean mouseClicked(Click click, boolean doubled) {
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.mouseClicked(click, doubled)) {
                for (int i = 0; i < this.parent.lines.size(); i++) {
                    if (this.parent.lines.get(i) == this) {
                        this.refresh(i);
                        this.nearestScrollableParent.refresh(0);
                        return true;
                    }
                }
                break; // probably redundant
            }
        }
        return false;
    }

    public boolean keyPressed(KeyInput input) {
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.keyPressed(input)) {
                return true;
            }
        }
        return false;
    }

    public boolean charTyped(CharInput input) {
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.charTyped(input)) {
                return true;
            }
        }
        return false;
    }
}
