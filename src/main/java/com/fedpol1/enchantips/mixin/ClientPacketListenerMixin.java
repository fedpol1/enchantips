package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.multiplayer.CommonListenerCookie;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.Connection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public abstract class ClientPacketListenerMixin {

    @Mutable
    @Final
    @Shadow
    private RegistryAccess.Frozen registryAccess;

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void enchantips$registerPerEnchantmentConfig(Minecraft client, Connection clientConnection, CommonListenerCookie clientConnectionState, CallbackInfo ci) {
        ModConfig.registerPerEnchantmentConfig(this.registryAccess);
    }
}
