package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.Bootstrap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bootstrap.class)
public class BootstrapMixin {

    // initialize config AFTER registries are frozen so per-enchant configs are loaded properly
    @Inject(method = "initialize()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registries;bootstrap()V", ordinal = 0, shift = At.Shift.AFTER))
    private static void enchantipsConfigInitialize(CallbackInfo ci) {
        ModConfig.registerConfig();
    }
}
