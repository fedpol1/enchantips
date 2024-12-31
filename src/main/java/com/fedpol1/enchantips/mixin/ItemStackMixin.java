package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.ItemEnchantmentsComponentAccess;
import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Inject(method = "getTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;)Ljava/util/List;", at = @At(value = "INVOKE", target = "java/util/List.add (Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER))
    private void enchantips$addExtraTooltips(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, @Local List<Text> list) {
        ItemStack t = (ItemStack)(Object)this;

        Boolean glint = t.get(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
        if(ModOption.GLINT_OVERRIDE_SWITCH.getValue() && glint != null) {
            list.add(TooltipHelper.buildForcedGlint(glint));
        }

        if(t.isEnchantable() && ModOption.ENCHANTABILITY_SWITCH.getValue()
                && t.get(DataComponentTypes.ENCHANTMENTS) != null
                && (t.getEnchantments().isEmpty() || ModOption.ENCHANTABILITY_SWITCH_WHEN_ENCHANTED.getValue())
        ) {
            EnchantableComponent c = t.get(DataComponentTypes.ENCHANTABLE);
            assert c != null;
            list.add(TooltipHelper.buildEnchantability(c.value()));
        }

        Integer cost = t.get(DataComponentTypes.REPAIR_COST);
        if(!(t.isOf(Items.ENCHANTED_BOOK)) && cost != null && cost != 0 && ModOption.REPAIR_COST_SWITCH.getValue()) {
            list.add(TooltipHelper.buildRepairCost(cost));
        }
    }

    @Override
    public boolean enchantips$isUnbreakable() {
        ItemStack t = (ItemStack)(Object)this;
        return t.get(DataComponentTypes.UNBREAKABLE) != null;
    }

    @Override
    public boolean enchantips$unbreakableVisible() {
        ItemStack t = (ItemStack)(Object)this;
        UnbreakableComponent ub = t.get(DataComponentTypes.UNBREAKABLE);
        return ub != null && ub.showInTooltip();
    }

    @Override
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
