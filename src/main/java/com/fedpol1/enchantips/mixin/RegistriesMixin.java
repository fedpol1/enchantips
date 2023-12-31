package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.Bootstrap;
import net.minecraft.registry.Registries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Registries.class)
public class RegistriesMixin {

    // initialize config AFTER registries are frozen so per-enchant configs are loaded properly
    @Inject(method = "freezeRegistries()V", at = @At(value = "TAIL"))
    private static void enchantipsConfigInitialize(CallbackInfo ci) {
        ModConfig.registerConfig();
    }
}
