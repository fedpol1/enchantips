package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.CollapsibleButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class CollapsibleInfoLine extends InfoDelineator implements InfoMultiLine, Drawable, Element {

    protected final InfoLineContainer lines;
    private final CollapsibleButton button;

    public CollapsibleInfoLine(Text text) {
        super(text);
        this.lines = new InfoLineContainer();
        this.button = new CollapsibleButton(this.x, this.y, true);
    }

    public void addLine(Text line) {
        this.addLine(new InfoLine(line));
    }

    public void addLine(InfoDelineator line) {
        if(line == null) { return; }
        line.parent = this.lines;
        line.setNearestScrollableParent(this.nearestScrollableParent);
        this.lines.addLine(line);
    }

    public int getHeight(int index) {
        return InfoDelineator.LINE_HEIGHT + (this.isCollapsed() ? 0 : this.lines.getHeight(index));
    }

    public int getHeight() {
        return this.getHeight(this.lines.lines.size());
    }

    public boolean isCollapsed() {
        return this.button.isCollapsed();
    }

    @Override
    public void refresh(int index) {
        this.x = this.parent.x;
        this.y = this.parent.y + this.parent.getHeight(index);
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.button.setPosition(this.x, this.y);
        this.width = this.parent.width;
        this.height = this.getHeight(index) + (this.isCollapsed() ? 0 : this.lines.getHeight());
        if(!this.isCollapsed()) {
            this.lines.refresh(index);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!this.isCollapsed()) {
            this.lines.render(context, mouseX, mouseY, delta);
        }

        if(this.y < this.nearestScrollableParent.y) { return; }
        if(this.y + InfoDelineator.LINE_HEIGHT > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return; }

        this.button.render(context, mouseX, mouseY, delta);
        this.renderText(context, this.button.getWidth() + 1, mouseX, mouseY, delta);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        for(InfoDelineator line : this.lines.lines) {
            if(line.mouseClicked(mouseX, mouseY, button)) {
                return true;
            }
        }

        if(this.button.mouseClicked(mouseX, mouseY, button)) {
            for (int i = 0; i < this.parent.lines.size(); i++) {
                if (this.parent.lines.get(i) == this) {
                    this.refresh(i);
                    this.nearestScrollableParent.refresh(0);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        for(InfoDelineator line : this.lines.lines) {
            if(line.keyPressed(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if(super.charTyped(chr, modifiers)) {
            return true;
        }
        for(InfoDelineator line : this.lines.lines) {
            if(line.charTyped(chr, modifiers)) {
                return true;
            }
        }
        return false;
    }
}
