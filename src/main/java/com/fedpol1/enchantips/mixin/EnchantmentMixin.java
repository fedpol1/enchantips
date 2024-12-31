package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentAccess {

    @Final
    @Shadow
    private Enchantment.Definition definition;

    @Inject(method = "getName", at = @At("HEAD"), cancellable = true)
    private static void enchantips$modifyName(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(EnchantmentAppearanceHelper.getName(EnchantmentLevel.of(enchantment.value(), level)));
    }


    @Override
    public RegistryEntryList<Item> enchantips$getPrimaryItems() {
        return this.definition.primaryItems().isEmpty() ? this.definition.supportedItems() : this.definition.primaryItems().get();
    }

    @Override
    public RegistryEntryList<Item> enchantips$getSecondaryItems() {
        return this.definition.supportedItems();
    }
}
