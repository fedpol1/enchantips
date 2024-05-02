package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.ItemEnchantmentsComponentAccess;
import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Inject(method = "getTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/client/item/TooltipType;)Ljava/util/List;", at = @At(value = "INVOKE", target = "java/util/List.add (Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantips$addExtraTooltips(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, List<Text> list, MutableText mutableText) {
        ItemStack t = (ItemStack)(Object)this;

        Boolean glint = t.get(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        if(ModOption.GLINT_OVERRIDE_SWITCH.getValue() && glint != null) {
            list.add(TooltipHelper.buildForcedGlint(glint));
        }

        if(t.getItem().isEnchantable(t) && ModOption.ENCHANTABILITY_SWITCH.getValue()
                && t.get(DataComponentTypes.ENCHANTMENTS) != null
                && (t.getEnchantments().getEnchantmentsMap().isEmpty() || ModOption.ENCHANTABILITY_SWITCH_WHEN_ENCHANTED.getValue())
        ) {
            list.add(TooltipHelper.buildEnchantability(t.getItem().getEnchantability()));
        }

        Integer cost = t.get(DataComponentTypes.REPAIR_COST);
        if(!(t.getItem() instanceof EnchantedBookItem) && cost != null && cost != 0 && ModOption.REPAIR_COST_SWITCH.getValue()) {
            list.add(TooltipHelper.buildRepairCost(cost));
        }
    }

    @Override
    public boolean enchantips$isUnbreakable() {
        ItemStack t = (ItemStack)(Object)this;
        return t.get(DataComponentTypes.UNBREAKABLE) != null;
    }

    public boolean enchantips$unbreakableVisible() {
        ItemStack t = (ItemStack)(Object)this;
        UnbreakableComponent ub = t.get(DataComponentTypes.UNBREAKABLE);
        return ub != null && ub.showInTooltip();
    }

    public boolean enchantips$enchantmentsVisible() {
        ItemStack t = (ItemStack)(Object)this;
        ItemEnchantmentsComponent ench = t.get(
                t.isOf(Items.ENCHANTED_BOOK) ?
                        DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS
        );
        ItemEnchantmentsComponentAccess enchAccess = (ItemEnchantmentsComponentAccess) ench;
        return enchAccess != null && enchAccess.enchantips$showInTooltip();
    }
}
