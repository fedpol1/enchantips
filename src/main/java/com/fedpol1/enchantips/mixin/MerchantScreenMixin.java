package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.SlotHighlight;
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
    private void enchantips$highlightFirstbuyItem(MerchantScreen instance, DrawContext context, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(context, adjustedFirstBuyItem, x, y, ModOption.HIGHLIGHTS_ALPHA_TRADING.getValue());
        }
        ((MerchantScreenMixin) (Object) instance).renderFirstBuyItem(context, adjustedFirstBuyItem, originalFirstBuyItem, x, y);
    }

    @Redirect(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawItemWithoutEntity(Lnet/minecraft/item/ItemStack;II)V"))
    private void enchantips$highlightOtherTradeItems(DrawContext context, ItemStack stack, int x, int y) {
        if(ModOption.HIGHLIGHTS_SWITCH.getValue()) {
            SlotHighlight.highlightSingleSlot(context, stack, x, y, ModOption.HIGHLIGHTS_ALPHA_TRADING.getValue());
        }
        context.drawItemWithoutEntity(stack, x, y);
    }
}
