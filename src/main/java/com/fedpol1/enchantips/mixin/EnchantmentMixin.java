package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements EnchantmentAccess {

    @Mutable
    @Final
    @Shadow
    private final Enchantment.Definition definition;

    protected EnchantmentMixin(Enchantment.Definition definition) {
        this.definition = definition;
    }

    /**
     * @author fedpol1
     * @reason overhaul enchantment tooltips
     */
    @Overwrite
    public static Text getName(RegistryEntry<Enchantment> ench, int level) throws IllegalStateException {
        return EnchantmentAppearanceHelper.getName(EnchantmentLevel.of(ench.value(), level));
    }

    public RegistryEntryList<Item> enchantips$getPrimaryItems() {
        return this.definition.primaryItems().isEmpty() ? this.definition.supportedItems() : this.definition.primaryItems().get();
    }

    public RegistryEntryList<Item> enchantips$getSecondaryItems() {
        return this.definition.supportedItems();
    }
}
