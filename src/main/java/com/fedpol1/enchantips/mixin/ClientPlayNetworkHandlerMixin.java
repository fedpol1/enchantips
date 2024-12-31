package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.registry.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Final
    @Shadow
    private DynamicRegistryManager.Immutable combinedDynamicRegistries;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void enchantips$registerPerEnchantmentConfig(CallbackInfo ci) {
        ModConfig.registerPerEnchantmentConfig(this.combinedDynamicRegistries);
    }
}
