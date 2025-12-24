package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractContainerScreen.class)
public abstract class AbstractContainerScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "HEAD"))
    private void enchantips$resetScrollableTooltipSection(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        ScrollableTooltipSection.setActiveSection(ScrollableTooltipSection.EMPTY);
    }

    @Inject(method = "renderSlots(Lnet/minecraft/client/gui/GuiGraphics;II)V", at = @At(value = "HEAD", ordinal = 0))
    private void enchantips$drawHighlights(GuiGraphics guiGraphics, int i, int j, CallbackInfo ci) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.drawEnchantedItemSlotHighlights(guiGraphics, ((AbstractContainerScreen<?>) (Object) this).getMenu(), 255);
        }
    }
}
