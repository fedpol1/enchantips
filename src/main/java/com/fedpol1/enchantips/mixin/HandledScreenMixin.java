package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void enchantipsInjectRenderDrawHighlights(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScreenHandler handler = ((HandledScreen<?>) (Object) this).getScreenHandler();

        float[] oldShaderColor = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if((boolean) ModOption.SHOW_HIGHLIGHTS.getData().getValue()) {
            SlotHighlight.drawEnchantedItemSlotHighlights(matrices, handler, 255);
        }
        RenderSystem.setShaderColor(oldShaderColor[0], oldShaderColor[1], oldShaderColor[2], oldShaderColor[3]);
    }
}
