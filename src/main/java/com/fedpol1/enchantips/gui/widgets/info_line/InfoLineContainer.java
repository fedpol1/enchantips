package com.fedpol1.enchantips.gui.widgets.info_line;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class InfoLineContainer implements InfoMultiLine, Drawable {

    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected boolean focused = false;
    protected CollapsibleInfoLine parent;
    protected ScrollableInfoLineContainer nearestScrollableParent;
    protected final ArrayList<InfoLine> lines;

    public InfoLineContainer(@Nullable CollapsibleInfoLine parent) {
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.parent = parent;
        this.nearestScrollableParent = null;
        this.lines = new ArrayList<>();
    }

    public void addLine(Text t) {
        this.addLine(new InfoLine(t));
    }

    public void addLine(InfoLine line) {
        if(line == null) { return; }
        line.parent = this;
        line.setNearestScrollableParent(this.nearestScrollableParent);
        this.lines.add(line);
    }

    public void removeLine(InfoLine line) {
        for(int i = 0; i < this.lines.size(); i++) {
            if (this.lines.get(i) == line) {
                this.lines.remove(i);
                this.refresh(i);
                this.nearestScrollableParent.refresh(0);
                break;
            }
        }
    }

    public int getWidth() {
        return this.width;
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
        this.height = InfoLine.LINE_HEIGHT;
    }

    public boolean isWithin(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.getHeight();
    }

    public InfoLine getLast() {
        InfoLine last = this.lines.getLast();
        if(last instanceof CollapsibleInfoLine collapsible) {
            if(collapsible.isCollapsed() || collapsible.lines.lines.isEmpty()) {
                return collapsible;
            }
            return collapsible.lines.getLast();
        }
        return last;
    }

    public CollapsibleInfoLine getParent() {
        return this.parent;
    }

    public void refresh(int index) {
        this.x = this.parent.parent.x + InfoLine.INDENTATION;
        this.y = this.parent.parent.y + this.parent.parent.getHeight(index) + InfoLine.LINE_HEIGHT;
        if(this.parent.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = this.parent.parent.getWidth() - InfoLine.INDENTATION;
        this.height = this.getHeight();
        for (int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }
    }

    public void setNearestScrollableParent(ScrollableInfoLineContainer container) {
        this.nearestScrollableParent = container;
        for(InfoLine line : this.lines) {
            line.setNearestScrollableParent(container);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for(InfoLine info : lines) {
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
        boolean ret = false;
        for(InfoLine line : lines) {
            ret = ret || line.mouseClicked(mouseX, mouseY, button);
        }
        return ret;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean ret = false;
        for(InfoLine line : lines) {
            ret = ret || line.keyPressed(keyCode, scanCode, modifiers);
        }
        return ret;
    }

    public boolean charTyped(char chr, int modifiers) {
        boolean ret = false;
        for(InfoLine line : lines) {
            ret = ret || line.charTyped(chr, modifiers);
        }
        return ret;
    }
}
