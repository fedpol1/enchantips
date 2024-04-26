package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.InGameHudAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ProtectionHud;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin implements InGameHudAccess {

    @Inject(method = "renderHotbarItem(Lnet/minecraft/client/gui/DrawContext;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getBobbingAnimationTime()I", ordinal = 0))
    private void enchantipsRenderHighlightsInHotbar(DrawContext context, int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack, int k, CallbackInfo ci) {
        if(ModOption.SHOW_HIGHLIGHTS.getValue()) {
            SlotHighlight.highlightSingleSlot(context, itemStack, i, j, ModOption.HIGHLIGHT_HOTBAR_ALPHA.getValue());
        }
    }

    @Inject(method = "renderStatusBars(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsRenderProtectionBar(DrawContext context, CallbackInfo ci, PlayerEntity playerEntity, int i, boolean bl, long l, int j, int k, int m, int n, float f, int o, int p, int q, int r, int s) {
        if(ModOption.SHOW_PROTECTION_BAR.getValue()) {
            ProtectionHud.renderWholeProtectionBar(context, k, n - (p - 1) * q - 10);
        }
    }
}
