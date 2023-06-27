package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.event.KeyInputHandler;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.gui.SlotHighlight;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.ScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "HEAD"))
    private void enchantipsInjectResetScrollableTooltipSection(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScrollableTooltipSection.setActiveSection(null);
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;translate(FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void enchantipsInjectRenderDrawHighlights(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScreenHandler handler = ((HandledScreen<?>) (Object) this).getScreenHandler();

        float[] oldShaderColor = RenderSystem.getShaderColor();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
        if((boolean) ModOption.SHOW_HIGHLIGHTS.getValue()) {
            SlotHighlight.drawEnchantedItemSlotHighlights(context, handler, 255);
        }
        RenderSystem.setShaderColor(oldShaderColor[0], oldShaderColor[1], oldShaderColor[2], oldShaderColor[3]);
    }

    @Inject(method = "keyPressed(III)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/HandledScreen;handleHotbarKeyPressed(II)Z", ordinal = 0), cancellable = true)
    private void enchantipsInjectHandleTooltipScroll(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        HandledScreen<?> t = (HandledScreen<?>) (Object) this;
        if(t instanceof EnchantmentScreen) {
            ScrollableTooltipSection s = ScrollableTooltipSection.getActiveSection();
            int factor = (modifiers & 3) > 0 ? 4 : 1; // fast scrolling if ctrl or shift held down
            if (KeyInputHandler.scrollUpKey.matchesKey(keyCode, scanCode) && s != null) {
                s.scroll(-1 * factor);
                cir.setReturnValue(true);
            }
            if (KeyInputHandler.scrollDownKey.matchesKey(keyCode, scanCode) && s != null) {
                s.scroll(1 * factor);
                cir.setReturnValue(true);
            }
        }
    }
}
