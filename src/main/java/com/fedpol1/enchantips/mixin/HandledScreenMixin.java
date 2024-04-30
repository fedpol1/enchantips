package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.gui.SlotHighlight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "HEAD"))
    private void enchantipsResetScrollableTooltipSection(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScrollableTooltipSection.setActiveSection(ScrollableTooltipSection.EMPTY);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void enchantipsRenderDrawHighlights(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            float[] oldShaderColor = RenderSystem.getShaderColor();
            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            SlotHighlight.drawEnchantedItemSlotHighlights(context, ((HandledScreen<?>) (Object) this).getScreenHandler(), 255);
            RenderSystem.setShaderColor(oldShaderColor[0], oldShaderColor[1], oldShaderColor[2], oldShaderColor[3]);
        }
    }
}
