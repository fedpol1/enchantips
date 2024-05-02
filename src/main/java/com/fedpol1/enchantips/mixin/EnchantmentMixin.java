package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements EnchantmentAccess {

    @Mutable
    @Final
    @Shadow
    private final Enchantment.Properties properties;

    protected EnchantmentMixin(Enchantment.Properties properties) {
        this.properties = properties;
    }

    /**
     * @author fedpol1
     * @reason overhaul enchantment tooltips
     */
    @Overwrite
    public Text getName(int level) {
        return EnchantmentAppearanceHelper.getName(EnchantmentLevel.of((Enchantment)(Object)this, level));
    }

    public TagKey<Item> enchantips$getPrimaryItems() {
        return this.properties.primaryItems().isEmpty() ? this.properties.supportedItems() : this.properties.primaryItems().get();
    }

    public TagKey<Item> enchantips$getSecondaryItems() {
        return this.properties.supportedItems();
    }
}
