package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.registry.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(RegistryLoader.class)
public abstract class RegistryLoaderMixin  {

    @Inject(method = "load(Lnet/minecraft/registry/RegistryLoader$RegistryLoadable;Lnet/minecraft/registry/DynamicRegistryManager;Ljava/util/List;)Lnet/minecraft/registry/DynamicRegistryManager$Immutable;", at = @At(value = "RETURN"))
    private static void enchantips$onRegistryLoad(@Coerce /* DynamicRegistryManager.RegistryLoadable */ Object loadable, DynamicRegistryManager baseRegistryManager, List<RegistryLoader.Entry<?>> entries, CallbackInfoReturnable<DynamicRegistryManager.Immutable> cir) {
        ModConfig.registerPerEnchantmentConfig(baseRegistryManager);
    }
}
