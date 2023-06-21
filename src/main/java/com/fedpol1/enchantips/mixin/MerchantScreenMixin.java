package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin {

    @Shadow
    protected abstract void renderFirstBuyItem(DrawContext context, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y);

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/MerchantScreen;renderFirstBuyItem(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;II)V"))
    private void enchantipsHighlightFirstbuyItem(MerchantScreen instance, DrawContext context, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y) {
        if((boolean) ModOption.SHOW_HIGHLIGHTS.getData().getValue()) {
            SlotHighlight.highlightSingleSlot(context, adjustedFirstBuyItem, x, y, (int) ModOption.HIGHLIGHT_TRADING_ALPHA.getData().getValue());
        }
        ((MerchantScreenMixin) (Object) instance).renderFirstBuyItem(context, adjustedFirstBuyItem, originalFirstBuyItem, x, y);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemInSlot(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/item/ItemStack;II)V"))
    private void enchantipsHighlightOtherTradeItems(DrawContext context, TextRenderer textRenderer, ItemStack stack, int x, int y) {
        if((boolean) ModOption.SHOW_HIGHLIGHTS.getData().getValue()) {
            SlotHighlight.highlightSingleSlot(context, stack, x, y, (int) ModOption.HIGHLIGHT_TRADING_ALPHA.getData().getValue());
        }
        context.drawItemInSlot(textRenderer, stack, x, y);
    }
}
