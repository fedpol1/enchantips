package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.gui.ProtectionHud;
import com.fedpol1.enchantips.gui.ProtectionType;
import net.minecraft.enchantment.ProtectionEnchantment;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProtectionEnchantment.class)
public class ProtectionEnchantmentMixin {

    @Redirect(method = "getProtectionAmount(ILnet/minecraft/entity/damage/DamageSource;)I", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
    private boolean enchantipsHighlightOtherTradeItems(DamageSource instance, TagKey<DamageType> tag) {
        if(ProtectionHud.getProtectionType() == ProtectionType.PROJECTILE && tag == DamageTypeTags.IS_PROJECTILE) { return true; }
        if(ProtectionHud.getProtectionType() == ProtectionType.EXPLOSION && tag == DamageTypeTags.IS_EXPLOSION) { return true; }
        if(ProtectionHud.getProtectionType() == ProtectionType.FIRE && tag == DamageTypeTags.IS_FIRE) { return true; }
        if(ProtectionHud.getProtectionType() == ProtectionType.FALL && tag == DamageTypeTags.IS_FALL) { return true; }
        return instance.isIn(tag);
    }
}
