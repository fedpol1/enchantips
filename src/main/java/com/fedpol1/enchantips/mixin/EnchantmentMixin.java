package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements EnchantmentAccess {

    /**
     * overwrite getName method
     * @author fedpol1
     * @reason overhaul enchantment tooltips
     */
    @Inject(method = "getName(I)Lnet/minecraft/text/Text;", at = @At(value = "HEAD"), cancellable = true)
    public void enchantipsGetName(int level, CallbackInfoReturnable<Text> cir) {
        cir.setReturnValue(EnchantmentAppearanceHelper.getName(EnchantmentLevel.of((Enchantment)(Object)this, level), false));
    }
}
