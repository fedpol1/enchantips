package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.SlotHighlightHelper;
import net.minecraft.client.gui.screen.ingame.MerchantScreen;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(MerchantScreen.class)
public abstract class MerchantScreenMixin {

    @Shadow
    protected abstract void renderFirstBuyItem(MatrixStack matrices, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y);

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/MerchantScreen;renderFirstBuyItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;II)V"))
    private void enchantipsHighlightFirstbuyItem(MerchantScreen instance, MatrixStack matrices, ItemStack adjustedFirstBuyItem, ItemStack originalFirstBuyItem, int x, int y) {
        SlotHighlightHelper.highlightSingleSlot(matrices, adjustedFirstBuyItem, x, y, (int) ModConfig.data.get(ModOption.HIGHLIGHT_TRADING_ALPHA).getValue());
        ((MerchantScreenMixin) (Object) instance).renderFirstBuyItem(matrices, adjustedFirstBuyItem, originalFirstBuyItem, x, y);
    }

    @Redirect(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/ItemRenderer;renderInGui(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemStack;II)V"))
    private void enchantipsHighlightOtherTradeItems(ItemRenderer instance, MatrixStack matrices, ItemStack stack, int x, int y) {
        SlotHighlightHelper.highlightSingleSlot(matrices, stack, x, y, (int) ModConfig.data.get(ModOption.HIGHLIGHT_TRADING_ALPHA).getValue());
        instance.renderInGui(matrices, stack, x, y);
    }
}
