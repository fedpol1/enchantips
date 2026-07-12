package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.gui.widgets.info_line.InfoLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

import java.util.List;

public abstract class BaseSetter<T extends InfoLine, U> {

    protected int x;
    protected int y;
    protected T line;
    protected U value;
    protected ButtonAction action;

    public static void playSound() {
        Minecraft
                .getInstance()
                .getSoundManager()
                .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
    }

    public BaseSetter(int x, int y, T line) {
        this.x = x;
        this.y = y;
        this.line = line;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getWidth() {
        return 9;
    }

    public int getHeight() {
        return 9;
    }

    public void setPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setValue(U value) {
        this.value = value;
    }

    public U getValue() {
        return this.value;
    }

    public boolean isWithin(double mouseX, double mouseY) {
        return mouseX >= this.x && mouseX < this.x + this.getWidth() && mouseY >= this.y && mouseY < this.y + this.getHeight();
    }

    public abstract boolean canTrigger();

    public abstract void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta);

    protected void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta, Identifier texture) {
        String namespace = texture.getNamespace();
        String path = texture.getPath();
        if(!this.canTrigger()) {
            path = path + "_disabled";
        }
        if(this.isWithin(mouseX, mouseY) || this.isFocused()) {
            path = path + "_hover";
        }
        List<Component> tooltipText = this.getTooltip();
        if(tooltipText != null && (this.isWithin(mouseX, mouseY) || this.isFocused())) {
            int tooltipX = this.isFocused() ? this.x : mouseX;
            int tooltipY = this.isFocused() ? this.y : mouseY;
            extractor.setComponentTooltipForNextFrame(Minecraft.getInstance().font, tooltipText, tooltipX, tooltipY);
        }
        extractor.blitSprite(
                RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                Identifier.fromNamespaceAndPath(namespace, path),
                this.x, this.y,
                this.getWidth(), this.getHeight()
        );
    }

    protected List<Component> getTooltip() {
        return null;
    }

    public void takeFocus() {
        this.line.setFocusedSetter(this);
    }

    public boolean isFocused() {
        return this.line.compareFocusedSetter(this) && this.line.isFocused();
    }

    public void onUnfocus() {}

    public void execute() {
        this.action.execute();
    }

    public boolean keyPressed(KeyEvent input) {
        return false;
    }

    public boolean charTyped(CharacterEvent input) {
        return false;
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if(this.isWithin(click.x(), click.y()) && this.canTrigger() && click.button() == 0) {
            this.line.takeFocus();
            this.takeFocus();
            this.action.execute();
            BaseSetter.playSound();
            return true;
        }
        return false;
    }
}
