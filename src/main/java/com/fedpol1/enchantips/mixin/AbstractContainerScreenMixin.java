package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At(value = "HEAD"))
    private void enchantips$resetScrollableTooltipSection(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScrollableTooltipSection.setActiveSection(ScrollableTooltipSection.EMPTY);
    }

    @Inject(method = "extractSlots(Lnet/minecraft/client/gui/GuiGraphicsExtractor;II)V", at = @At(value = "HEAD", ordinal = 0))
    private void enchantips$drawHighlights(GuiGraphicsExtractor extractor, int i, int j, CallbackInfo ci) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.drawEnchantedItemSlotHighlights(extractor, ((AbstractContainerScreen<?>) (Object) this).getMenu(), 255);
        }
    }
}
