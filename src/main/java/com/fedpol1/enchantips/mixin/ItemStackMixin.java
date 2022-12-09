package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.TooltipBuilder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public class ItemStackMixin {

    @Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsInjectGetTooltipEnchantability(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemStack t = (ItemStack)(Object)this;
        if(t.getItem().isEnchantable(t) && ModConfig.SHOW_ENCHANTABILITY.getValue()) {
            if(!(t.hasEnchantments() && !ModConfig.SHOW_ENCHANTABILITY_WHEN_ENCHANTED.getValue())) {
                list.add(TooltipBuilder.buildEnchantability(t.getItem().getEnchantability()));
            }
        }
    }

    @Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;appendEnchantments(Ljava/util/List;Lnet/minecraft/nbt/NbtList;)V", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsInjectGetTooltipRepairCost(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemStack t = (ItemStack)(Object)this;
        if(!(t.getItem() instanceof EnchantedBookItem) && ModConfig.SHOW_REPAIRCOST.getValue()) {
            int cost = t.getRepairCost();
            if(!(cost == 0 && !ModConfig.SHOW_REPAIRCOST_WHEN_0.getValue())) {
                list.add(TooltipBuilder.buildRepairCost(cost));
            }
        }
    }
}
