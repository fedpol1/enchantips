package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.gui.widgets.info_line.InfoLine;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;

public abstract class BaseSetter<T extends InfoLine, U> {

    protected int x;
    protected int y;
    protected T line;
    protected U value;

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

    public abstract void render(GuiGraphics context, int mouseX, int mouseY, float delta);

    protected void render(GuiGraphics context, int mouseX, int mouseY, float delta, Identifier texture) {
        String namespace = texture.getNamespace();
        String path = texture.getPath();
        if(!this.canTrigger()) {
            path = path + "_disabled";
        }
        if(this.isWithin(mouseX, mouseY)) {
            path = path + "_hover";
        }
        context.blitSprite(
                RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                Identifier.fromNamespaceAndPath(namespace, path),
                this.x, this.y,
                this.getWidth(), this.getHeight()
        );
    }

    public boolean keyPressed(KeyEvent input) {
        return false;
    }

    public boolean charTyped(CharacterEvent input) {
        return false;
    }

    public abstract boolean mouseClicked(MouseButtonEvent click, boolean doubled);

    protected boolean mouseClicked(MouseButtonEvent click, boolean doubled, ButtonAction action) {
        if(this.isWithin(click.x(), click.y()) && this.canTrigger() && click.button() == 0) {
            action.execute();
            Minecraft
                    .getInstance()
                    .getSoundManager()
                    .play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return false;
    }
}
