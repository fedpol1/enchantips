package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.gui.widgets.CollapsibleInfoLine;
import com.fedpol1.enchantips.gui.widgets.InfoDelineator;
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

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class EnchantmentInfoScreen extends Screen {

    private static final Identifier FRAME_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/frame");
    private static final Identifier BACKGROUND_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/background");

    private int windowX;
    private int windowY;
    private int windowWidth;
    private int windowHeight;
    private final ScrollableInfoLineContainer lines;
    private final Screen parent;

    public EnchantmentInfoScreen(Text title, @Nullable Screen parent) {
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
            CollapsibleInfoLine enchMeta = new CollapsibleInfoLine(Text.literal(key.getValue().toString()));
            this.lines.addLine(enchMeta);

            // misc
            enchMeta.addLine(new InfoLine(enchantment.description())); // description
            enchMeta.addLine(new InfoLine(Text.translatable("enchantips.gui.enchantment_info.max_level", enchantment.getMaxLevel())));
            enchMeta.addLine(new InfoLine(Text.translatable("enchantips.gui.enchantment_info.weight", enchantment.getWeight())));
            enchMeta.addLine(new InfoLine(Text.translatable("enchantips.gui.enchantment_info.anvil_cost", enchantment.getAnvilCost())));

            CollapsibleInfoLine minPowers = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.min_power"));
            CollapsibleInfoLine maxPowers = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.max_power"));
            enchMeta.addLine(minPowers);
            enchMeta.addLine(maxPowers);
            for(int i = 1; i < enchantment.getMaxLevel() + 1; i++) {
                minPowers.addLine(Text.translatable(
                        "enchantips.gui.enchantment_info.per_power",
                        Text.translatable("enchantment.level." + i),
                        enchantment.getMinPower(i)
                        ));
                maxPowers.addLine(Text.translatable(
                        "enchantips.gui.enchantment_info.per_power",
                        Text.translatable("enchantment.level." + i),
                        enchantment.getMaxPower(i)
                ));
            }

            // exclusive set
            CollapsibleInfoLine exclusive = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.exclusive_set"));
            List<String> exclusiveEnchs = enchantment.exclusiveSet().stream().map(RegistryEntry::getIdAsString).sorted().toList();
            if(!exclusiveEnchs.isEmpty()) { enchMeta.addLine(exclusive); }
            exclusiveEnchs.forEach(e -> exclusive.addLine(Text.literal(e)));

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

    private void calculateDimensions() {
        this.windowX = this.width/10;
        this.windowY = this.height/10;
        this.windowWidth = this.width * 8/10;
        this.windowHeight = this.height * 8/10;
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
        RenderSystem.enableBlend();
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                EnchantmentInfoScreen.BACKGROUND_TEXTURE,
                this.windowX, this.windowY, this.windowWidth, this.windowHeight
        );
        context.drawGuiTexture(
                RenderLayer::getGuiTextured,
                EnchantmentInfoScreen.FRAME_TEXTURE,
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
