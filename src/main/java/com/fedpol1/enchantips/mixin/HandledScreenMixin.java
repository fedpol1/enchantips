package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HandledScreen.class)
public abstract class HandledScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "HEAD"))
    private void enchantips$resetScrollableTooltipSection(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScrollableTooltipSection.setActiveSection(ScrollableTooltipSection.EMPTY);
    }

    @Inject(method = "drawSlots(Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "HEAD", ordinal = 0))
    private void enchantips$drawHighlights(DrawContext context, CallbackInfo ci) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.drawEnchantedItemSlotHighlights(context, ((HandledScreen<?>) (Object) this).getScreenHandler(), 255);
        }
    }
}
