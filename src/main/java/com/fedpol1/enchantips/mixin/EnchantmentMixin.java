package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements EnchantmentAccess {

    @Mutable
    @Final
    @Shadow
    private final Enchantment.EnchantmentDefinition definition;

    protected EnchantmentMixin(Enchantment.EnchantmentDefinition definition) {
        this.definition = definition;
    }

    @Inject(method = "getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;", at = @At(value = "HEAD"), cancellable = true)
    private static void enchantips$overwriteName(Holder<Enchantment> enchantment, int level, CallbackInfoReturnable<Component> cir) throws IllegalStateException {
        if(ModOption.GLOBAL_NAME_MODIFICATION.getValue()) {
            cir.setReturnValue(
                    EnchantmentAppearanceHelper.getName(
                            EnchantmentLevel.of(enchantment.value(), level)
                    )
            );
        }
    }

    public HolderSet<Item> enchantips$getPrimaryItems() {
        return this.definition.primaryItems().isEmpty() ? this.definition.supportedItems() : this.definition.primaryItems().get();
    }

    public HolderSet<Item> enchantips$getSecondaryItems() {
        return this.definition.supportedItems();
    }
}
