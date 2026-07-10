package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.CollapsibleButton;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;

public class CollapsibleInfoLine extends InfoLine implements InfoMultiLine, Renderable, GuiEventListener {

    protected final InfoLineContainer lines;
    protected final CollapsibleButton expandButton;

    public CollapsibleInfoLine(Component text) {
        super(text);
        this.lines = new InfoLineContainer(this);
        this.expandButton = new CollapsibleButton(this.x, this.y, this,true);
        this.setters.add(this.expandButton);
    }

    public void addLine(Component line) {
        this.addLine(new InfoLine(line));
    }

    public void addLine(InfoLine line) {
        if(line == null) { return; }
        line.parent = this.lines;
        line.setNearestScrollableParent(this.nearestScrollableParent);
        this.lines.addLine(line);
    }

    public int getHeight(int index) {
        return super.getHeight(index) + (this.isCollapsed() ? 0 : this.lines.getHeight(index));
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
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta);
        if(!this.isCollapsed()) {
            this.lines.extractRenderState(extractor, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean shouldRender(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        if(this.y < this.nearestScrollableParent.y) { return false; }
        if(this.y + InfoLine.LINE_HEIGHT > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return false; }
        return true;
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        boolean ret = !this.isCollapsed() && this.lines.mouseClicked(click, doubled);
        return super.mouseClicked(click, doubled) || ret;
    }

    public boolean keyPressed(KeyEvent input) {
        boolean ret = !this.isCollapsed() && this.lines.keyPressed(input);
        return super.keyPressed(input) || ret;
    }

    public boolean charTyped(CharacterEvent input) {
        boolean ret = !this.isCollapsed() && this.lines.charTyped(input);
        return super.charTyped(input) || ret;
    }
}
