package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.CollapsibleInfoLine;
import com.fedpol1.enchantips.gui.widgets.ScrollableInfoLineContainer;
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
    private final ScrollableInfoLineContainer lines;

    public EnchantmentInfoScreen(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
        this.lines = new ScrollableInfoLineContainer(this);
        CollapsibleInfoLine c = new CollapsibleInfoLine(Text.literal("new"));
        this.lines.addLine(Text.literal("a"));
        this.lines.addLine(c);
        c.addLine(Text.literal("qqq1"));
        c.addLine(Text.literal("qqq2"));
        c.addLine(Text.literal("qqq3"));
        this.lines.addLine(Text.literal("bb"));
        this.lines.addLine(Text.literal("cccc"));
        this.lines.addLine(Text.literal("dddddddd"));
        this.lines.addLine(Text.literal("eeeeeeeeeeeeeeee"));
        this.lines.addLine(Text.literal("ffffffffffffffffffffffffffffffff"));
        this.lines.addLine(Text.literal("1"));
        this.lines.addLine(Text.literal("2"));
        this.lines.addLine(Text.literal("3"));
        this.lines.addLine(Text.literal("4"));
        this.lines.addLine(Text.literal("5"));
        this.lines.addLine(Text.literal("6"));
        this.lines.addLine(Text.literal("7"));
        this.lines.addLine(Text.literal("8"));
        this.lines.addLine(Text.literal("9"));
        this.lines.addLine(Text.literal("10"));

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
        this.lines.refresh(0);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        lines.scroll((int)verticalAmount);
        return false;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.lines.mouseClicked(mouseX, mouseY, button);
        return true;
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
