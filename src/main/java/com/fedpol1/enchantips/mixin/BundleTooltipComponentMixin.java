package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentMixin {

    @Redirect(method = "drawSlot(IIIZLnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;II)V"))
    private void enchantips$highlightInnerItems(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(context, stack, x, y, 255);
        }
        context.drawItemInSlot(textRenderer, stack, x, y);
    }
}
