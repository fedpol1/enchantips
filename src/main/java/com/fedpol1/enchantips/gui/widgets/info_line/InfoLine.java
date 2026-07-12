package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.NavigationAction;
import com.fedpol1.enchantips.gui.widgets.tiny.BaseSetter;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.List;

public class InfoLine implements Renderable, NarratableEntry {

    public static final int LINE_HEIGHT = 10;
    public static final int INDENTATION = 16;

    protected Component text;
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected int offset; // indentation caused by setters
    protected boolean focused = false;
    protected InfoLineContainer parent;
    protected ScrollableInfoLineContainer nearestScrollableParent;
    protected final ArrayList<BaseSetter<?, ?>> setters;
    protected BaseSetter<?, ?> focusedSetter;
    protected List<FormattedCharSequence> lineSplits;

    public InfoLine(Component text) {
        this.text = text;
        this.x = 0;
        this.y = 0;
        this.width = 0;
        this.height = 0;
        this.offset = 0;
        this.parent = null;
        this.nearestScrollableParent = null;
        this.setters = new ArrayList<>();
        this.focusedSetter = null;
        this.lineSplits = new ArrayList<>();
    }

    public int getHeight(int index) {
        return InfoLine.LINE_HEIGHT * this.lineSplits.size();
    }

    public int getHeight() {
        return this.getHeight(0);
    }

    public boolean isWithin(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.width && mouseY >= this.y && mouseY < this.y + this.getHeight();
    }

    public void renderText(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        Font renderer = Minecraft.getInstance().font;

        extractor.enableScissor(
                this.x + this.offset,
                this.y,
                this.x + this.width,
                this.y + this.height
        );
        for(int i = 0; i < this.lineSplits.size(); i++) {
            if(
                    this.y + InfoLine.LINE_HEIGHT * (i + 1) >
                    this.nearestScrollableParent.y + this.nearestScrollableParent.height
            ) { break; }
            extractor.text(
                    renderer,
                    this.lineSplits.get(i),
                    this.x + this.offset,
                    this.y + 1 + InfoLine.LINE_HEIGHT * i,
                    this.nearestScrollableParent.childColor,
                    false
            );
        }
        extractor.disableScissor();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        if(!this.shouldRender(extractor, mouseX, mouseY, delta)) { return; }
        if(this.parent != null && this.parent.getParent() != null &&
                this.parent.getParent().isCollapsed()
        ) { return; }

        if(this.focused) {
            extractor.blitSprite(
                    RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                    Identifier.fromNamespaceAndPath(EnchantipsClient.MODID, "config/focused"),
                    this.x - 10, this.y,
                    9, 9
            );
        }

        for(BaseSetter<?, ?> setter : this.setters) {
            setter.extractRenderState(extractor, mouseX, mouseY, delta);
        }
        this.renderText(extractor, mouseX, mouseY, delta);
    }

    public boolean shouldRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if(this.y < this.nearestScrollableParent.y) { return false; }
        if(this.y >= this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return false; }
        return true;
    }

    public void refresh(int index) {
        this.x = this.parent.x;
        this.y = this.parent.y + this.parent.getHeight(index);
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }

        this.offset = 0;
        for(BaseSetter<?, ?> setter : this.setters) {
            setter.setPosition(this.x + offset, this.y);
            this.offset += setter.getWidth() + 1;
        }

        // splitting depends on width; height depends on splits
        this.width = this.parent.getWidth();
        this.lineSplits = Minecraft.getInstance().font.split(this.text, this.width - this.offset);
        this.height = this.getHeight(index);
    }

    public InfoLineContainer getParent() {
        return this.parent;
    }

    public void setNearestScrollableParent(ScrollableInfoLineContainer container) {
        this.nearestScrollableParent = container;
    }

    public void takeFocus() {
        this.nearestScrollableParent.setLastFocused(this);
        this.focusSetterAt(0);
    }

    protected void setFocused(boolean focused) {
        this.focused = focused;
        this.focusedSetter = focused && !this.setters.isEmpty() ? this.setters.getFirst() : null;
    }

    protected boolean isFocused() {
        return this.focused;
    }

    public void setFocusedSetter(BaseSetter<?, ?> setter) {
        if(setter != null && !this.setters.contains(setter)) {
            throw new IllegalArgumentException("Setter not in setters");
        }
        if(this.focusedSetter != null) { this.focusedSetter.setFocused(false); }
        this.focusedSetter = setter;
        if(this.focusedSetter != null) { this.focusedSetter.setFocused(true); }
    }

    public BaseSetter<?, ?> getFocusedSetter() {
        return this.focusedSetter;
    }

    protected void focusSetterRelative(int offset) {
        int i = this.setters.indexOf(this.getFocusedSetter());
        if(this.setters.isEmpty()) {
            return;
        }
        i = Math.clamp(i + offset, 0, this.setters.size() - 1);
        this.setters.get(i).takeFocus();
    }

    protected void focusSetterAt(int index) {
        if(this.setters.isEmpty()) { return; }
        this.setters.get(Math.clamp(index, 0, this.setters.size() - 1)).takeFocus();
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        this.setFocusedSetter(null);
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.mouseClicked(click, doubled)) {
                this.refresh(this.parent.lines.indexOf(this));
                this.nearestScrollableParent.refresh(0);
                return true;
            }
        }
        return false;
    }

    public static NavigationAction navigationDirection(KeyEvent event) {
        return switch(event.key()) {
            case InputConstants.KEY_TAB -> event.hasShiftDown() ? NavigationAction.PREVIOUS : NavigationAction.NEXT;
            case InputConstants.KEY_LEFT -> NavigationAction.LEFT;
            case InputConstants.KEY_RIGHT -> NavigationAction.RIGHT;
            case InputConstants.KEY_DOWN -> NavigationAction.DOWN;
            case InputConstants.KEY_UP -> NavigationAction.UP;
            case InputConstants.KEY_RETURN, InputConstants.KEY_NUMPADENTER -> NavigationAction.ACCEPT;
            default -> null;
        };
    }

    private void handleNavigationDown(int index) {
        if(this instanceof CollapsibleInfoLine curCollapsible && !curCollapsible.isCollapsed()) {
            this.setFocusedSetter(null);
            InfoLine newFocus = curCollapsible.lines.lines.getFirst();
            newFocus.takeFocus();
            newFocus.focusSetterAt(index);
            return;
        }
        InfoLine current = this;
        int currentIndex = current.parent.lines.indexOf(current);
        while(currentIndex == current.parent.lines.size() - 1) {
            if(current.parent instanceof ScrollableInfoLineContainer) {
                return;
            }
            current = current.parent.parent;
            currentIndex = current.parent.lines.indexOf(current);
        }
        this.setFocusedSetter(null);
        InfoLine newFocus = current.parent.lines.get(currentIndex + 1);
        newFocus.takeFocus();
        newFocus.focusSetterAt(index);
    }

    private void handleNavigationUp(int index) {
        int thisIndex = this.parent.lines.indexOf(this);
        if(thisIndex == 0 && this.parent instanceof ScrollableInfoLineContainer) {
            return;
        }
        this.setFocusedSetter(null);
        if (thisIndex == 0) {
            this.parent.parent.takeFocus();
            this.parent.parent.focusSetterAt(index);
            return;
        }
        InfoLine current = this.parent.lines.get(thisIndex - 1);
        while(current instanceof CollapsibleInfoLine curCollapsible && !curCollapsible.isCollapsed()) {
            current = curCollapsible.lines.getLast();
        }
        current.takeFocus();
        current.focusSetterAt(index);
    }

    private void handleNavigation(NavigationAction dir) {
        switch(dir) {
            case ACCEPT -> {
                BaseSetter<?, ?> setter = this.focusedSetter;
                if(setter.canTrigger()) {
                    setter.execute();
                    this.refresh(this.parent.lines.indexOf(this));
                    this.nearestScrollableParent.refresh(0);
                    BaseSetter.playSound();
                }
            }
            case LEFT -> this.focusSetterRelative(-1);
            case RIGHT -> this.focusSetterRelative(1);
            case UP -> this.handleNavigationUp(this.setters.indexOf(this.focusedSetter));
            case DOWN -> this.handleNavigationDown(this.setters.indexOf(this.focusedSetter));
            case NEXT -> {
                if(this.setters.indexOf(this.focusedSetter) == this.setters.size() - 1) {
                    this.handleNavigationDown(0);
                } else {
                    this.focusSetterRelative(1);
                }
            }
            case PREVIOUS -> {
                if(this.setters.indexOf(this.focusedSetter) == 0) {
                    this.handleNavigationUp(Integer.MAX_VALUE);
                } else {
                    this.focusSetterRelative(-1);
                }
            }
        }
    }

    public boolean keyPressed(KeyEvent input) {
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.keyPressed(input)) {
                return true;
            }
        }
        NavigationAction nav = InfoLine.navigationDirection(input);
        if(nav != null && this.focused) {
            this.handleNavigation(nav);
            return true;
        }
        return false;
    }

    public boolean charTyped(CharacterEvent input) {
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.charTyped(input)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput output) {
        return;
    }
}
