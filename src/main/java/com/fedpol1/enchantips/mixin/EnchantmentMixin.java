package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements EnchantmentAccess {

    @Mutable
    @Final
    @Shadow
    private final Enchantment.Properties properties;

    private MutableText enchantipsEnchantmentTargetSymbolText;

    protected EnchantmentMixin(Enchantment.Properties properties) {
        this.properties = properties;
    }

    /**
     * @author fedpol1
     * @reason overhaul enchantment tooltips
     */
    @Overwrite
    public Text getName(int level) {
        return EnchantmentAppearanceHelper.getName(EnchantmentLevel.of((Enchantment)(Object)this, level), false);
    }

    @Inject(method = "<init>", at = @At(value = "TAIL"))
    private void enchantipsSetEnchantmentTargetSymbolText(Enchantment.Properties properties, CallbackInfo ci) {
        this.enchantipsEnchantmentTargetSymbolText = EnchantmentAppearanceHelper.getEnchantmentTargetSymbolText((Enchantment)(Object)this);
    }

    public TagKey<Item> enchantipsGetPrimaryItems() {
        return this.properties.primaryItems().isEmpty() ? this.properties.supportedItems() : this.properties.primaryItems().get();
    }

    public TagKey<Item> enchantipsGetSecondaryItems() {
        return this.properties.supportedItems();
    }

    public MutableText enchantipsGetEnchantmentTargetSymbolText() {
        return this.enchantipsEnchantmentTargetSymbolText;
    }
}
