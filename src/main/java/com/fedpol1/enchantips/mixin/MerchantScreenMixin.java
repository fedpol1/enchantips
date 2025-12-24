package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin {

    @Shadow
    protected abstract void renderAndDecorateCostA(GuiGraphics context, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y);

    @Redirect(method = "renderContents(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;renderAndDecorateCostA(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;II)V"))
    private void enchantips$highlightFirstbuyItem(MerchantScreen instance, GuiGraphics context, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(context, adjustedFirstBuyItem, x, y, ModOption.HIGHLIGHTS_ALPHA_TRADING.getValue());
        }
        ((MerchantScreenMixin) (Object) instance).renderAndDecorateCostA(context, adjustedFirstBuyItem, originalFirstBuyItem, x, y);
    }

    @Redirect(method = "renderContents(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;renderFakeItem(Lnet/minecraft/world/item/ItemStack;II)V"))
    private void enchantips$highlightOtherTradeItems(GuiGraphics context, ItemStack stack, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(context, stack, x, y, ModOption.HIGHLIGHTS_ALPHA_TRADING.getValue());
        }
        context.renderFakeItem(stack, x, y);
    }
}
