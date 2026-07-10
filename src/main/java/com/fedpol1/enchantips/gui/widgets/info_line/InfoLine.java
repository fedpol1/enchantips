package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.tiny.BaseSetter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
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

public class InfoLine implements Renderable, GuiEventListener, NarratableEntry {

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
        this.lineSplits = new ArrayList<>();
    }

    public int getHeight(int index) {
        return InfoLine.LINE_HEIGHT * this.lineSplits.size();
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

        int offset = 0;
        for(BaseSetter<?, ?> setter : this.setters) {
            setter.extractRenderState(extractor, mouseX, mouseY, delta);
            offset += setter.getWidth() + 1;
        }
        this.renderText(extractor, mouseX, mouseY, delta);
    }

    public boolean shouldRender(GuiGraphicsExtractor context, int mouseX, int mouseY, float delta) {
        if(this.y < this.nearestScrollableParent.y) { return false; }
        if(this.y > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return false; }
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
    }

    @Override
    public void setFocused(boolean focused) {
        this.focused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.focused;
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        boolean ret = false;
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.mouseClicked(click, doubled)) {
                for (int i = 0; i < this.parent.lines.size(); i++) {
                    if (this.parent.lines.get(i) == this) {
                        this.refresh(i);
                        this.nearestScrollableParent.refresh(0);
                        ret = true;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public boolean keyPressed(KeyEvent input) {
        boolean ret = false;
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.keyPressed(input)) {
                ret = true;
            }
        }
        return ret;
    }

    public boolean charTyped(CharacterEvent input) {
        boolean ret = false;
        for(BaseSetter<?, ?> setter : this.setters) {
            if(setter.charTyped(input)) {
                ret = true;
            }
        }
        return ret;
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
