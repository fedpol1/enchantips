package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.gui.widgets.CollapsibleInfoLine;
import com.fedpol1.enchantips.gui.widgets.InfoLine;
import com.fedpol1.enchantips.gui.widgets.ScrollableInfoLineContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class EnchantmentInfoScreen extends Screen {

    private final Screen parent;
    private static final Identifier FRAME_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/frame");
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/background");
    private final ScrollableInfoLineContainer lines;

    public EnchantmentInfoScreen(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
        this.lines = new ScrollableInfoLineContainer(this);

        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) { return; }
        Optional<Registry<Enchantment>> optionalWrapper = world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        if(optionalWrapper.isEmpty()) { return; }
        RegistryWrapper.Impl<Enchantment> wrapper = optionalWrapper.get();

        for(RegistryKey<Enchantment> key : wrapper.streamKeys().toList()) {
            Enchantment enchantment = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).get(key);
            if(enchantment == null) { continue; }
            CollapsibleInfoLine enchMeta = new CollapsibleInfoLine(enchantment.description());
            this.lines.addLine(enchMeta);

            // identifier
            enchMeta.addLine(new InfoLine(Text.literal(key.getValue().toString())));

            // primary and secondary items
            CollapsibleInfoLine primary = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.primary_items"));
            CollapsibleInfoLine secondary = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.secondary_items"));
            List<String> primaryItems = ((EnchantmentAccess)(Object) enchantment).enchantips$getPrimaryItems()
                    .stream()
                    .map(e -> Registries.ITEM.getId(e.value()).toString())
                    .sorted()
                    .toList();
            List<String> secondaryItems = ((EnchantmentAccess)(Object) enchantment).enchantips$getSecondaryItems()
                    .stream()
                    .map(e -> Registries.ITEM.getId(e.value()).toString())
                    .sorted()
                    .filter(e -> !primaryItems.contains(e))
                    .toList();
            if(!primaryItems.isEmpty()) { enchMeta.addLine(primary); }
            if(!secondaryItems.isEmpty()) { enchMeta.addLine(secondary); }
            primaryItems.forEach(e -> primary.addLine(Text.literal(e)));
            secondaryItems.forEach(e -> secondary.addLine(Text.literal(e)));

            // tags
            CollapsibleInfoLine tags = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.tags"));
            enchMeta.addLine(tags);
            Optional<RegistryEntry.Reference<Enchantment>> enchantmentReference = world
                    .getRegistryManager()
                    .getOrThrow(RegistryKeys.ENCHANTMENT)
                    .getEntry(key.getValue());
            if(enchantmentReference.isPresent()) {
                for (TagKey<Enchantment> tag : enchantmentReference.get().streamTags().toList()) {
                    tags.addLine(Text.literal("#").append(Text.literal(tag.id().toString())));
                }
            }
        }
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
