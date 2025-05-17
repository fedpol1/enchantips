package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.gui.widgets.info_line.InfoLine;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

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

    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    protected void render(DrawContext context, int mouseX, int mouseY, float delta, Identifier texture) {
        String namespace = texture.getNamespace();
        String path = texture.getPath();
        if(!this.canTrigger()) {
            path = path + "_disabled";
        }
        if(this.isWithin(mouseX, mouseY)) {
            path = path + "_hover";
        }
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                Identifier.of(namespace, path),
                this.x, this.y,
                this.getWidth(), this.getHeight()
        );
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        return false;
    }

    public abstract boolean mouseClicked(double mouseX, double mouseY, int button);

    protected boolean mouseClicked(double mouseX, double mouseY, int button, ButtonAction action) {
        if(this.isWithin(mouseX, mouseY) && this.canTrigger() && button == 0) {
            action.execute();
            MinecraftClient
                    .getInstance()
                    .getSoundManager()
                    .play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
            return true;
        }
        return false;
    }
}
