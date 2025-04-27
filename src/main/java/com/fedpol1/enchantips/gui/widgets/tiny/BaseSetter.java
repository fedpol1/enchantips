package com.fedpol1.enchantips.gui.widgets.tiny;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;

public abstract class BaseSetter<T> {

    protected int x;
    protected int y;

    public BaseSetter(int x, int y) {
        this.x = x;
        this.y = y;
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

    public abstract void setValue(T value);

    public boolean isWithin(double mouseX, double mouseY) {
        int x = this.x;
        int y = this.y + 1;
        return mouseX >= x && mouseX < x + this.getWidth() && mouseY >= y && mouseY < y + this.getHeight();
    }

    public abstract void render(DrawContext context, int mouseX, int mouseY, float delta);

    protected void render(DrawContext context, int mouseX, int mouseY, float delta, Identifier texture) {
        String namespace = texture.getNamespace();
        String path = texture.getPath();
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
        if(this.isWithin(mouseX, mouseY) && button == 0) {
            action.execute();
            return true;
        }
        return false;
    }
}
