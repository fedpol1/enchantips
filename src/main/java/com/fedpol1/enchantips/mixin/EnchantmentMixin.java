package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
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
public abstract class EnchantmentMixin implements EnchantmentAccess {

    @Mutable
    @Final
    @Shadow
    private final Enchantment.Definition definition;

    protected EnchantmentMixin(Enchantment.Definition definition) {
        this.definition = definition;
    }

    @Inject(method = "getName(Lnet/minecraft/registry/entry/RegistryEntry;I)Lnet/minecraft/text/Text;", at = @At(value = "HEAD"), cancellable = true)
    private static void enchantips$overwriteName(RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Text> cir) throws IllegalStateException {
        if(ModOption.GLOBAL_NAME_MODIFICATION.getValue()) {
            cir.setReturnValue(
                    EnchantmentAppearanceHelper.getName(
                            EnchantmentLevel.of(enchantment.value(), level)
                    )
            );
        }
    }

    public RegistryEntryList<Item> enchantips$getPrimaryItems() {
        return this.definition.primaryItems().isEmpty() ? this.definition.supportedItems() : this.definition.primaryItems().get();
    }

    public RegistryEntryList<Item> enchantips$getSecondaryItems() {
        return this.definition.supportedItems();
    }
}
