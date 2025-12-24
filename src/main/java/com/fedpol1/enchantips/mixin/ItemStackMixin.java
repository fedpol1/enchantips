package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.function.Consumer;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin implements ItemStackAccess {

    @Inject(method = "getTooltipLines(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;)Ljava/util/List;", at = @At(value = "INVOKE", target = "java/util/List.add (Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void enchantips$addExtraTooltips(Item.TooltipContext context, Player player, TooltipFlag type, CallbackInfoReturnable<List<Component>> cir, TooltipDisplay tooltipDisplayComponent, List<Component> list) {
        ItemStack t = (ItemStack)(Object)this;

        Boolean glint = t.get(DataComponents.ENCHANTMENT_GLINT_OVERRIDE);
        if(ModOption.GLINT_OVERRIDE_SWITCH.getValue() && glint != null) {
            list.add(TooltipHelper.buildForcedGlint(glint));
        }

        if(t.isEnchantable() && ModOption.ENCHANTABILITY_SWITCH.getValue()
                && t.get(DataComponents.ENCHANTMENTS) != null
                && (t.getEnchantments().isEmpty() || ModOption.ENCHANTABILITY_SWITCH_WHEN_ENCHANTED.getValue())
        ) {
            Enchantable c = t.get(DataComponents.ENCHANTABLE);
            assert c != null;
            list.add(TooltipHelper.buildEnchantability(c.value()));
        }

        Integer cost = t.get(DataComponents.REPAIR_COST);
        if(!(t.is(Items.ENCHANTED_BOOK)) && cost != null && cost != 0 && ModOption.REPAIR_COST_SWITCH.getValue()) {
            list.add(TooltipHelper.buildRepairCost(cost));
        }
    }

    @WrapOperation(
            method = "addDetailsToTooltip(Lnet/minecraft/world/item/Item$TooltipContext;Lnet/minecraft/world/item/component/TooltipDisplay;Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/item/TooltipFlag;Ljava/util/function/Consumer;)V",
            at = @At(
                    value = "INVOKE",
                    opcode = Opcodes.INVOKEVIRTUAL,
                    target = "Lnet/minecraft/world/item/ItemStack;addUnitComponentToTooltip(Lnet/minecraft/core/component/DataComponentType;Lnet/minecraft/network/chat/Component;Lnet/minecraft/world/item/component/TooltipDisplay;Ljava/util/function/Consumer;)V",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "FIELD",
                            opcode = Opcodes.GETSTATIC,
                            target = "net/minecraft/world/item/ItemStack.UNBREAKABLE_TOOLTIP:Lnet/minecraft/network/chat/Component;",
                            ordinal = 0
                    ),
                    to = @At(value = "RETURN")
            )
    )
    private void enchantips$modifyUnbreakableText(ItemStack stack, DataComponentType<?> dataComponentType, Component component, TooltipDisplay tooltipDisplay, Consumer<Component> consumer, Operation<Void> original) {
        MutableComponent text = component.copy().withColor(ModOption.UNBREAKABLE_COLOR.getValue().getRGB());
        original.call(stack, dataComponentType, text, tooltipDisplay, consumer);
    }

    public boolean enchantips$isUnbreakable() {
        ItemStack t = (ItemStack)(Object)this;
        return t.get(DataComponents.UNBREAKABLE) != null;
    }

    public boolean enchantips$unbreakableVisible() {
        ItemStack t = (ItemStack)(Object)this;
        TooltipDisplay tc = t.get(DataComponents.TOOLTIP_DISPLAY);
        return tc != null && tc.shows(DataComponents.UNBREAKABLE);
    }

    public boolean enchantips$enchantmentsVisible() {
        ItemStack t = (ItemStack)(Object)this;
        DataComponentType<ItemEnchantments> componentType = t.is(Items.ENCHANTED_BOOK) ?
                DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
        TooltipDisplay tc = t.get(DataComponents.TOOLTIP_DISPLAY);
        return tc != null && tc.shows(componentType);
    }
}
