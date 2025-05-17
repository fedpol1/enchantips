package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.BaseSetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;
import net.minecraft.util.Util;

import java.util.ArrayList;

public class InfoDelineator implements Drawable, Element {

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

    public InfoDelineator(Text text) {
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
        return InfoDelineator.LINE_HEIGHT;
    }

    public int getHeight() {
        return this.getHeight(0);
    }

    public void setHeight() {
        this.height = InfoDelineator.LINE_HEIGHT;
    }

    public boolean isWithin(double mouseX, double mouseY) {
        int x = this.x;
        int y = this.y + 1;
        return mouseX >= x && mouseX < x + this.width && mouseY >= y && mouseY < y + this.getHeight();
    }

    public void renderText(DrawContext context, int startOffset, int mouseX, int mouseY, float delta) {
        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        int textWidth = renderer.getWidth(this.text);
        int scrollRange = Math.max(0, textWidth - (this.width - startOffset));
        double time = (double) Util.getMeasuringTimeMs() / 1000.0;
        double cosine = scrollRange * (Math.cos(3.14159465358989 * 8 * time / scrollRange) / 2.0 + 0.5);

        context.enableScissor(
                this.x + startOffset,
                this.y,
                this.x + this.width,
                this.y + height
        );
        context.drawText(
                renderer,
                this.text,
                this.x + startOffset - (int) cosine,
                this.y + 1,
                this.nearestScrollableParent.childColor,
                false
        );
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
        this.width = this.parent.width;
        this.height = this.getHeight(index);

        int offset = 0;
        for(BaseSetter<?, ?> setter : this.setters) {
            setter.setPosition(this.x + offset, this.y);
            offset += setter.getWidth() + 1;
        }
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

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        return false;
    }
}
