package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.component.ComponentType;
import net.minecraft.component.type.EnchantableComponent;
import net.minecraft.component.type.TooltipDisplayComponent;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Inject(method = "getTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;)Ljava/util/List;", at = @At(value = "INVOKE", target = "java/util/List.add (Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantips$addExtraTooltips(Item.TooltipContext context, PlayerEntity player, TooltipType type, CallbackInfoReturnable<List<Text>> cir, TooltipDisplayComponent tooltipDisplayComponent, List<Text> list) {
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

    @WrapOperation(
            method = "appendTooltip(Lnet/minecraft/item/Item$TooltipContext;Lnet/minecraft/component/type/TooltipDisplayComponent;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/item/tooltip/TooltipType;Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "FIELD",
                    opcode = Opcodes.GETSTATIC,
                    target = "net/minecraft/item/ItemStack.UNBREAKABLE_TEXT:Lnet/minecraft/text/Text;",
                    ordinal = 0,
                    shift = At.Shift.AFTER
            )
    )
    private <T> void enchantips$modifyUnbreakableText(Consumer<T> instance, Object t, Operation<Void> original) {
        MutableText text = (MutableText) t;
        original.call(instance, text.withColor(ModOption.UNBREAKABLE_COLOR.getValue().getRGB()));
    }

    public boolean enchantips$isUnbreakable() {
        ItemStack t = (ItemStack)(Object)this;
        return t.get(DataComponentTypes.UNBREAKABLE) != null;
    }

    public boolean enchantips$unbreakableVisible() {
        ItemStack t = (ItemStack)(Object)this;
        TooltipDisplayComponent tc = t.get(DataComponentTypes.TOOLTIP_DISPLAY);
        return tc != null && tc.shouldDisplay(DataComponentTypes.UNBREAKABLE);
    }

    public boolean enchantips$enchantmentsVisible() {
        ItemStack t = (ItemStack)(Object)this;
        ComponentType<ItemEnchantmentsComponent> componentType = t.isOf(Items.ENCHANTED_BOOK) ?
                DataComponentTypes.STORED_ENCHANTMENTS : DataComponentTypes.ENCHANTMENTS;
        TooltipDisplayComponent tc = t.get(DataComponentTypes.TOOLTIP_DISPLAY);
        return tc != null && tc.shouldDisplay(componentType);
    }
}
