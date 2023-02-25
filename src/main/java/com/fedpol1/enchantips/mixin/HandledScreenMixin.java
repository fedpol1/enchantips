package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.SlotHighlightHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0))
    private void enchantipsInjectRenderDrawHighlights(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(MinecraftClient.getInstance().player == null) { return; /* short circuit, do nothing */ }
        ItemStack offhand = MinecraftClient.getInstance().player.getStackInHand(Hand.OFF_HAND);
        ScreenHandler handler = ((HandledScreen<?>) (Object) this).getScreenHandler();

        float[] oldShaderColor = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if(offhand == null || !offhand.isOf(Items.ENCHANTED_BOOK)) {
            if(ModConfig.SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.getValue()) {
                SlotHighlightHelper.drawSpecialEnchantedItemSlotHighlights(matrices, handler);
            }
        }
        else {
            if(ModConfig.SHOW_HIGHLIGHTS_ENCHANTMENT_MATCH.getValue()) {
                NbtList offhandEnchantments = EnchantedBookItem.getEnchantmentNbt(offhand);
                SlotHighlightHelper.drawEnchantedBookSlotHighlights(matrices, handler, offhandEnchantments);
            }
        }
        RenderSystem.setShaderColor(oldShaderColor[0], oldShaderColor[1], oldShaderColor[2], oldShaderColor[3]);
    }
}
