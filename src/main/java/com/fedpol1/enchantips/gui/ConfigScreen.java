package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.InfoDelineator;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import com.fedpol1.enchantips.gui.widgets.info_line.info_generic.EnchantmentInfoLine;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public class ConfigScreen extends Screen {

    private static final Identifier FRAME_TEXTURE = Identifier.of(EnchantipsClient.MODID, "config/frame");
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of(EnchantipsClient.MODID, "config/background");

    private int windowX;
    private int windowY;
    private int windowWidth;
    private int windowHeight;
    private final ScrollableInfoLineContainer lines;
    private final Screen parent;

    public ConfigScreen(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
        this.calculateDimensions();
        this.lines = new ScrollableInfoLineContainer(0x404040, 6);

        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) { return; }
        Optional<Registry<Enchantment>> optionalWrapper = world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        if(optionalWrapper.isEmpty()) { return; }
        RegistryWrapper.Impl<Enchantment> wrapper = optionalWrapper.get();

        for(RegistryKey<Enchantment> key : wrapper.streamKeys().sorted(Comparator.comparing(RegistryKey::toString)).toList()) {
            Enchantment enchantment = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).get(key);
            if(enchantment == null) { continue; }
            EnchantmentInfoLine enchMeta = new EnchantmentInfoLine(key);
            this.lines.addLine(enchMeta);
            enchMeta.populateMeta(enchantment);
            enchMeta.populatePowers(enchantment);
            enchMeta.populateExclusiveSet(enchantment);
            enchMeta.populateItems(enchantment);
            enchMeta.populateTags(key, world);
        }
    }

    private void calculateDimensions() {
        this.windowX = this.width/10;
        this.windowY = this.height/10;
        this.windowWidth = this.width * 8/10;
        this.windowHeight = Math.max(this.height * 8/10, InfoDelineator.LINE_HEIGHT + 39);
        int extra = Math.floorMod(this.windowHeight - 39, InfoDelineator.LINE_HEIGHT);
        this.windowY += extra/2;
        this.windowHeight -= extra;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawWindow(context);
        this.lines.render(context, mouseX, mouseY, delta);
    }

    public void drawWindow(DrawContext context) {
        this.calculateDimensions();
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                ConfigScreen.BACKGROUND_TEXTURE,
                this.windowX, this.windowY, this.windowWidth, this.windowHeight
        );
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                ConfigScreen.FRAME_TEXTURE,
                this.windowX, this.windowY, this.windowWidth, this.windowHeight
        );
        context.drawText(this.textRenderer, this.title, this.windowX + 8, this.windowY + 6, 0x404040, false);
    }

    @Override
    protected void init() {
        this.calculateDimensions();
        this.lines.setPosition(this.windowX + 15, this.windowY + 24);
        this.lines.setDimensions(this.windowWidth - 30, this.windowHeight - 39);
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

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        this.lines.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        return true;
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
