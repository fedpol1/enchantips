package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.tooltip.BundleTooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BundleTooltipComponent.class)
public class BundleTooltipComponentMixin {

    @Redirect(method = "drawSlot(IIIZLnet/minecraft/client/font/TextRenderer;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/item/ItemRenderer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGuiWithOverrides(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;III)V"))
    private void enchantipsHighlightOtherTradeItems(ItemRenderer instance, MatrixStack matrices, ItemStack stack, int x, int y, int index) {
        if((boolean) ModOption.SHOW_HIGHLIGHTS.getData().getValue()) {
            SlotHighlight.highlightSingleSlot(matrices, stack, x, y, (int) ModOption.HIGHLIGHT_TRADING_ALPHA.getData().getValue());
        }
        instance.renderInGuiWithOverrides(matrices, stack, x, y, index);
    }
}
