package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.InfoLineContainer;
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
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/background");
    private final InfoLineContainer lines;

    public EnchantmentInfoScreen(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
        this.lines = new InfoLineContainer(this.width/10 + 32, this.height/10 + 32, this.width * 8/10 - 64, this.height * 8/10 - 64);
        this.lines.addLine(Text.literal("a"));
        this.lines.addLine(Text.literal("bb"));
        this.lines.addLine(Text.literal("cccc"));
        this.lines.addLine(Text.literal("dddddddd"));
        this.lines.addLine(Text.literal("eeeeeeeeeeeeeeee"));
        this.lines.addLine(Text.literal("ffffffffffffffffffffffffffffffff"));
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawWindow(context, this.width/10, this.height/10);
        this.lines.render(context, mouseX, mouseY, delta);
    }

    public void drawWindow(DrawContext context, int x, int y) {
        int w = this.width * 8/10;
        int h = this.height * 8/10;
        RenderSystem.enableBlend();
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                EnchantmentInfoScreen.BACKGROUND_TEXTURE,
                x, y, w, h
        );
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                EnchantmentInfoScreen.FRAME_TEXTURE,
                x, y, w, h
        );
        context.drawText(this.textRenderer, this.title, x + 8, y + 6, 0x404040, false);
    }

    @Override
    protected void init() {
        this.lines.refresh(this.width/10 + 32, this.height/10 + 64, this.width * 8/10 - 64, this.height * 8/10 - 96);
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
