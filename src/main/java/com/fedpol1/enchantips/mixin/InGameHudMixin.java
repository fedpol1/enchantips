package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.InGameHudAccess;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.gui.ProtectionHud;
import com.fedpol1.enchantips.util.SlotHighlightHelper;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper implements InGameHudAccess {

    int enchantipsPreviousTexture;
    Identifier enchantipsPreviousTextureId;

    @Inject(method = "renderHotbarItem(Lnet/minecraft/client/util/math/MatrixStack;IIFLnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/ItemStack;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;getBobbingAnimationTime()I", ordinal = 0))
    private void enchantipsRenderHighlightsInHotbar(MatrixStack matrixStack, int i, int j, float f, PlayerEntity playerEntity, ItemStack itemStack, int k, CallbackInfo ci) {
        if(ModConfig.SHOW_HIGHLIGHTS_SPECIALLY_ENCHANTED.getValue()) {
            SlotHighlightHelper.highlightSingleSlot(matrixStack, itemStack, i, j, ModConfig.HIGHLIGHT_HOTBAR_ALPHA.getValue());
        }
    }

    @Inject(method = "renderStatusBars(Lnet/minecraft/client/util/math/MatrixStack;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getProfiler()Lnet/minecraft/util/profiler/Profiler;", ordinal = 1), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsStoreStuffFromBarRenderer(MatrixStack matrices, CallbackInfo ci, PlayerEntity playerEntity, int i, boolean bl, long l, int j, HungerManager hungerManager, int k, int m, int n, int o, float f, int p, int q, int r, int s, int t, int u, int v) {
        if(ModConfig.SHOW_PROTECTION_BAR.getValue()) {
            RenderSystem.setShaderTexture(0, ProtectionHud.ICONS);
            ProtectionHud.renderWholeProtectionBar(matrices, m, s);
            RenderSystem.setShaderTexture(this.enchantipsPreviousTexture, this.enchantipsPreviousTextureId);
        }
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;F)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/systems/RenderSystem;setShaderTexture(ILnet/minecraft/util/Identifier;)V"))
    private void enchantipsStoreTextureInfo(int texture, Identifier id) {
        this.enchantipsPreviousTexture = texture;
        this.enchantipsPreviousTextureId = id;
        RenderSystem.setShaderTexture(texture, id);
    }
}
