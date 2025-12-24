package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BuiltInRegistries.class)
public class BuiltInRegistriesMixin {

    // initialize config AFTER registries are frozen so per-enchant configs are loaded properly
    // likely not necessary in 1.21+ but it isn't broken so i won't change it
    @Inject(method = "freeze()V", at = @At(value = "TAIL"))
    private static void enchantips$initializeConfig(CallbackInfo ci) {
        ModConfig.registerConfig();
    }
}
