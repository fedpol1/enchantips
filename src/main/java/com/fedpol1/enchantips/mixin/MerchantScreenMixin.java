package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.MerchantScreen;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin {

    @Shadow
    protected abstract void extractAndDecorateCostA(GuiGraphicsExtractor extractor, ItemStack costA, ItemStack baseCostA, int sellItem1X, int decorHeight);

    @Redirect(method = "extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/MerchantScreen;extractAndDecorateCostA(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemStack;II)V"))
    private void enchantips$highlightFirstbuyItem(MerchantScreen instance, GuiGraphicsExtractor extractor, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(extractor, adjustedFirstBuyItem, x, y, ModOption.HIGHLIGHTS_ALPHA_TRADING.getValue());
        }
        ((MerchantScreenMixin) (Object) instance).extractAndDecorateCostA(extractor, adjustedFirstBuyItem, originalFirstBuyItem, x, y);
    }

    @Redirect(method = "extractContents(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fakeItem(Lnet/minecraft/world/item/ItemStack;II)V"))
    private void enchantips$highlightOtherTradeItems(GuiGraphicsExtractor extractor, ItemStack stack, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(extractor, stack, x, y, ModOption.HIGHLIGHTS_ALPHA_TRADING.getValue());
        }
        extractor.fakeItem(stack, x, y);
    }
}
