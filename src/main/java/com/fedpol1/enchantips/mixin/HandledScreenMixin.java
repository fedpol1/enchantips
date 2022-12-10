package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.ColorManager;
import com.fedpol1.enchantips.util.EnchantmentListUtil;
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
public class HandledScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderColor(FFFF)V", ordinal = 0))
    private void enchantipsInjectRenderDrawHighlights(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(MinecraftClient.getInstance().player == null) { return; /* short circuit, do nothing */ }
        ItemStack offhand = MinecraftClient.getInstance().player.getStackInHand(Hand.OFF_HAND);
        if(offhand == null || !offhand.isOf(Items.ENCHANTED_BOOK)) { return; /* short circuit, do nothing */ }
        NbtList offhandEnchantments = EnchantedBookItem.getEnchantmentNbt(offhand);

        HandledScreen t = (HandledScreen)(Object)this;
        ScreenHandler handler = t.getScreenHandler();
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            ItemStack slotStack = slot.getStack();
            NbtList slotEnchantments;
            if(slotStack.isOf(Items.ENCHANTED_BOOK)) { slotEnchantments = EnchantedBookItem.getEnchantmentNbt(slotStack); }
            else { slotEnchantments = slot.getStack().getEnchantments(); }

            int matches =EnchantmentListUtil.countMatches(offhandEnchantments, slotEnchantments, false);
            if(matches > 0) {
                float[] color;
                if(matches == offhandEnchantments.size()) { color = ColorManager.intToFloats(ModConfig.SLOT_HIGHLIGHT_FULL_MATCH.getColor().getRgb()); }
                else { color = ColorManager.intToFloats(ModConfig.SLOT_HIGHLIGHT_PARTIAL_MATCH.getColor().getRgb()); }
                RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0f);
                HandledScreen.drawSlotHighlight(matrices, slot.x, slot.y, t.getZOffset());
            }
        }
    }
}
