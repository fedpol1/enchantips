package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentMixin {

    @Redirect(method = "drawItem(IIILjava/util/List;ILnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/gui/DrawContext;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItem(Lnet/minecraft/item/ItemStack;III)V"))
    private void enchantips$highlightInnerItems(DrawContext context, ItemStack stack, int x, int y, int seed) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(context, stack, x, y, 255);
        }
        context.drawItem(stack, x, y, seed);
    }
}
