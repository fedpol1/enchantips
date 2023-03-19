package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.ItemStackAccess;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.EnchantmentLevelData;
import com.fedpol1.enchantips.util.TooltipBuilder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Shadow
    private static boolean isSectionVisible(int flags, ItemStack.TooltipSection tooltipSection) { throw new AssertionError(); }

    @Shadow
    private int getHideFlags() { throw new AssertionError(); }

    @Shadow
    private NbtCompound nbt;

    @Final
    @Shadow
    private static String UNBREAKABLE_KEY;

    /**
     * @author fedpol1
     * @reason add sorting to enchantment tooltips
     */
    @Overwrite
    public static void appendEnchantments(List<Text> tooltip, NbtList enchantments) {
        ArrayList<EnchantmentLevelData> enchantmentLevelData = EnchantmentLevelData.ofList(enchantments);
        Collections.sort(enchantmentLevelData);
        for(EnchantmentLevelData ench : enchantmentLevelData) {
            tooltip.add(((EnchantmentAccess)ench.getEnchantment()).enchantipsGetName(ench.getLevel(), false));
        }
    }

    @Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsAddEnchantabilityTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemStack t = (ItemStack)(Object)this;
        if(t.getItem().isEnchantable(t) && ModConfig.SHOW_ENCHANTABILITY.getValue()) {
            if(!(t.hasEnchantments() && !ModConfig.SHOW_ENCHANTABILITY_WHEN_ENCHANTED.getValue())) {
                list.add(TooltipBuilder.buildEnchantability(t.getItem().getEnchantability()));
            }
        }
    }

    @Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 0), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsAddRepairCostTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list) {
        ItemStack t = (ItemStack)(Object)this;
        int cost = t.getRepairCost();
        if(!(t.getItem() instanceof EnchantedBookItem) && cost != 0 && ModConfig.SHOW_REPAIRCOST.getValue()) {
            list.add(TooltipBuilder.buildRepairCost(cost));
        }
    }

    // these two methods, in conjunction, replace the unbreakable tooltip
    @Redirect(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isSectionVisible(ILnet/minecraft/item/ItemStack$TooltipSection;)Z"))
    private boolean enchantipsRemoveOldUnbreakableTooltip(int flags, ItemStack.TooltipSection tooltipSection) {
        if(tooltipSection == ItemStack.TooltipSection.UNBREAKABLE) { return false; }
        return ItemStackMixin.isSectionVisible(flags, tooltipSection);
    }

    @Inject(method = "getTooltip(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipContext;)Ljava/util/List;",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;hasNbt()Z", ordinal = 1)),
            at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isSectionVisible(ILnet/minecraft/item/ItemStack$TooltipSection;)Z", ordinal = 0),
            locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantipsAddNewUnbreakableTooltip(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir, List<Text> list, MutableText mutableText, int i) {
        if (ItemStackMixin.isSectionVisible(i, ItemStack.TooltipSection.UNBREAKABLE) && this.nbt.getBoolean(UNBREAKABLE_KEY)) {
            list.add(TooltipBuilder.buildUnbreakable());
        }
    }

    @Override
    public boolean enchantipsIsUnbreakable() {
        ItemStack t = (ItemStack)(Object)this;
        NbtCompound n = t.getNbt();
        return n != null && n.getBoolean(UNBREAKABLE_KEY);
    }

    public boolean enchantipsUnbreakableVisible() {
        return ItemStackMixin.isSectionVisible(this.getHideFlags(), ItemStack.TooltipSection.UNBREAKABLE);
    }

    public boolean enchantipsEnchantmentsVisible() {
        return ItemStackMixin.isSectionVisible(this.getHideFlags(), ItemStack.TooltipSection.ENCHANTMENTS);
    }
}
