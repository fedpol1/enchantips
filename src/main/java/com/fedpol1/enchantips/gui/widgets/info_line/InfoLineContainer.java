package com.fedpol1.enchantips.gui.widgets.info_line;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

public class InfoLineContainer implements InfoMultiLine, Renderable, Iterable<InfoLine> {

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

    public void addLine(Component t) {
        this.addLine(new InfoLine(t));
    }

    public void addLine(InfoLine line) throws NullPointerException {
        if(line == null) {
            throw new NullPointerException("Tried to add null InfoLine to InfoLineContainer");
        }
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

    public @NonNull Iterator<InfoLine> iterator() {
        return new InfoLineIterator(this);
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

    public boolean rightAligned() {
        return this.nearestScrollableParent.rightAligned();
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
        InfoLineContainer grandparent = this.parent.parent;
        this.x = this.rightAligned() ? grandparent.x : grandparent.x + InfoLine.INDENTATION;
        this.y = grandparent.y + grandparent.getHeight(index) + InfoLine.LINE_HEIGHT * this.parent.lineSplits.size();
        if(grandparent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = grandparent.getWidth() - InfoLine.INDENTATION;
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
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        for(InfoLine info : lines) {
            info.extractRenderState(extractor, mouseX, mouseY, delta);
        }
    }

    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    public boolean isFocused() {
        return this.focused;
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        boolean ret = false;
        @SuppressWarnings("unchecked")
        ArrayList<InfoLine> linesCopy = (ArrayList<InfoLine>) (lines.clone()); // avoid concurrent modification
        for(InfoLine line : linesCopy) {
            ret = line.mouseClicked(click, doubled) || ret; // avoid short circuit
        }
        return ret;
    }

    public boolean keyPressed(KeyEvent input) {
        boolean ret = false;
        for(InfoLine line : lines) {
            ret = ret || line.keyPressed(input);
        }
        return ret;
    }

    public boolean charTyped(CharacterEvent input) {
        boolean ret = false;
        for(InfoLine line : lines) {
            ret = ret || line.charTyped(input);
        }
        return ret;
    }

    public static class InfoLineIterator implements Iterator<InfoLine> {

        private InfoLine current;
        private final InfoLineContainer root;
        private final boolean empty;

        public InfoLineIterator(InfoLineContainer scrollable) {
            this.empty = scrollable.lines.isEmpty();
            this.root = scrollable;
            this.current = null;
        }

        private boolean isLast(InfoLine child, InfoLineContainer parent) {
            return parent.lines.indexOf(child) == parent.lines.size() - 1 &&
                    (parent == this.root || this.isLast(parent.parent, parent.parent.parent));
        }

        private InfoLine nextIfLast(InfoLine child, InfoLineContainer parent) {
            int childIndex = parent.lines.indexOf(child);
            if(childIndex == parent.lines.size() - 1) {
                return this.nextIfLast(parent.parent, parent.parent.parent);
            }
            return parent.lines.get(childIndex + 1);
        }

        @Override
        public boolean hasNext() {
            if(this.empty) { return false; }
            if(this.current == null) { return true; }
            if(this.current instanceof CollapsibleInfoLine collapsible && !collapsible.lines.lines.isEmpty()) {
                return true;
            }
            return !this.isLast(this.current, this.current.parent);
        }

        @Override
        public InfoLine next() throws NoSuchElementException {
            if(!this.hasNext()) { throw new NoSuchElementException(); }
            if(this.current == null) {
                this.current = this.root.lines.getFirst();
                return this.current;
            }
            if(this.current instanceof CollapsibleInfoLine collapsible && !collapsible.lines.lines.isEmpty()) {
                this.current = collapsible.lines.lines.getFirst();
                return this.current;
            }

            this.current = this.nextIfLast(this.current, this.current.parent);
            return this.current;
        }
    }
}
