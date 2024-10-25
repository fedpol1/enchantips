package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class EnchantmentInfoScreen extends Screen {

    private final Screen parent;
    private static final Identifier FRAME_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/frame");

    public EnchantmentInfoScreen(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawWindow(context, this.width/10, this.height/10);
    }

    public void drawWindow(DrawContext context, int x, int y) {
        RenderSystem.enableBlend();
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                EnchantmentInfoScreen.FRAME_TEXTURE,
                x, y,
                this.width * 8/10, this.height * 8/10
        );
        context.drawText(this.textRenderer, this.title, x + 8, y + 6, 0x404040, false);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
