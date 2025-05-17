package com.fedpol1.enchantips.gui.widgets.info_line;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class InfoLineContainer implements InfoMultiLine, Drawable {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean focused = false;
    protected InfoLineContainer parent;
    protected ScrollableInfoLineContainer nearestScrollableParent;
    protected final ArrayList<InfoDelineator> lines;

    public InfoLineContainer() {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.parent = null;
        this.nearestScrollableParent = null;
        this.lines = new ArrayList<>();
    }

    public void addLine(Text t) {
        this.addLine(new InfoLine(t));
    }

    public void addLine(InfoDelineator line) {
        if(line == null) { return; }
        line.parent = this;
        line.setNearestScrollableParent(this.nearestScrollableParent);
        if(line instanceof CollapsibleInfoLine collapsible) {
            collapsible.lines.parent = this;
            collapsible.lines.setNearestScrollableParent(this.nearestScrollableParent);
        }
        this.lines.add(line);
    }

    public int getHeight(int index) {
        int sum = 0;
        for(int i = 0; i < this.lines.size(); i++) {
            if(i >= index) { return sum; }
            sum += this.lines.get(i).getHeight();
        }
        return sum;
    }

    public int getHeight() {
        return this.getHeight(this.lines.size());
    }

    public void setHeight() {
        this.height = InfoDelineator.LINE_HEIGHT;
    }

    public InfoDelineator getLast() {
        InfoDelineator last = this.lines.getLast();
        if(last instanceof CollapsibleInfoLine collapsible) {
            if(collapsible.isCollapsed() || collapsible.lines.lines.isEmpty()) {
                return collapsible;
            }
            return collapsible.lines.getLast();
        }
        return last;
    }

    public void refresh(int index) {
        this.x = this.parent.x + InfoDelineator.INDENTATION;
        this.y = this.parent.y + this.parent.getHeight(index) + InfoDelineator.LINE_HEIGHT;
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = this.parent.width - InfoDelineator.INDENTATION;
        this.height = this.getHeight();
        for (int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }
    }

    public void setNearestScrollableParent(ScrollableInfoLineContainer container) {
        this.nearestScrollableParent = container;
        for(InfoDelineator line : this.lines) {
            line.setNearestScrollableParent(container);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for(InfoDelineator info : lines) {
            info.render(context, mouseX, mouseY, delta);
        }
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(InfoDelineator line : lines) {
            line.mouseClicked(mouseX, mouseY, button);
        }
        return true;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        for(InfoDelineator line : lines) {
            line.keyPressed(keyCode, scanCode, modifiers);
        }
        return true;
    }

    public boolean charTyped(char chr, int modifiers) {
        for(InfoDelineator line : lines) {
            line.charTyped(chr, modifiers);
        }
        return true;
    }
}
