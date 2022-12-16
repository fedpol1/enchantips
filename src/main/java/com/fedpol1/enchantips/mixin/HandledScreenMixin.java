package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.ColorManager;
import com.fedpol1.enchantips.util.EnchantmentListUtil;
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
import net.minecraft.screen.slot.Slot;
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
        HandledScreen t = (HandledScreen) (Object) this;
        ScreenHandler handler = t.getScreenHandler();
        if(offhand == null || !offhand.isOf(Items.ENCHANTED_BOOK)) {
            if(ModConfig.SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.getValue()) {
                SlotHighlightHelper.drawSpecialEnchantedItemSlotHighlights(matrices, t, handler);
            }
        }
        else {
            if(ModConfig.SHOW_HIGHLIGHTS_ENCHANTMENT_MATCH.getValue()) {
                NbtList offhandEnchantments = EnchantedBookItem.getEnchantmentNbt(offhand);
                SlotHighlightHelper.drawEnchantedBookSlotHighlights(matrices, t, handler, offhandEnchantments);
            }
        }
    }
}
