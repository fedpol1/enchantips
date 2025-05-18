package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.CollapsibleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class CollapsibleInfoLine extends InfoLine implements InfoMultiLine, Drawable, Element {

    protected final InfoLineContainer lines;
    protected final CollapsibleButton expandButton;

    public CollapsibleInfoLine(Text text) {
        super(text);
        this.lines = new InfoLineContainer(this);
        this.expandButton = new CollapsibleButton(this.x, this.y, this,true);
        this.setters.add(this.expandButton);
    }

    public void addLine(Text line) {
        this.addLine(new InfoLine(line));
    }

    public void addLine(InfoLine line) {
        if(line == null) { return; }
        line.parent = this.lines;
        line.setNearestScrollableParent(this.nearestScrollableParent);
        this.lines.addLine(line);
    }

    public int getHeight(int index) {
        return InfoLine.LINE_HEIGHT + (this.isCollapsed() ? 0 : this.lines.getHeight(index));
    }

    public int getHeight() {
        return this.getHeight(this.lines.lines.size());
    }

    public boolean isCollapsed() {
        return this.expandButton.isCollapsed();
    }

    public int numChildren() {
        return this.lines.lines.size();
    }

    @Override
    public void refresh(int index) {
        super.refresh(index);
        if(!this.isCollapsed()) {
            this.lines.refresh(index);
        }
    }

    public void setNearestScrollableParent(ScrollableInfoLineContainer container) {
        super.setNearestScrollableParent(container);
        this.lines.setNearestScrollableParent(container);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        if(!this.isCollapsed()) {
            this.lines.render(context, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldRender(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.y < this.nearestScrollableParent.y) { return false; }
        if(this.y + InfoLine.LINE_HEIGHT > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return false; }
        return true;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(!this.isCollapsed() && this.lines.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(!this.isCollapsed() && this.lines.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int modifiers) {
        if(!this.isCollapsed() && this.lines.charTyped(chr, modifiers)) {
            return true;
        }
        return super.charTyped(chr, modifiers);
    }
}
