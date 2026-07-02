package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.resources.Identifier;

public class AnvilSwapWarn implements Renderable, GuiEventListener {

    private final int x;
    private final int y;
    private final int width;
    private final int height;
    private final Identifier texture;
    private boolean isFocused = false;

    public AnvilSwapWarn(int x, int y, int width, int height, Identifier texture) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        extractor.blitSprite(RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA, this.texture, this.x, this.y, this.width, this.height);
    }

    @Override
    public void setFocused(boolean focused) {
        this.isFocused = focused;
    }

    @Override
    public boolean isFocused() {
        return this.isFocused;
    }
}
