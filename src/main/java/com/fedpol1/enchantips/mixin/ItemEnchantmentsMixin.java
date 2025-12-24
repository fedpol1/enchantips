package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.ItemEnchantments;

@Mixin(ItemEnchantments.class)
public class ItemEnchantmentsMixin {

    /**
     * @author fedpol1
     * @reason sort enchantments in tooltip
     */
    @Overwrite
    public void addToTooltip(Item.TooltipContext context, Consumer<Component> textConsumer, TooltipFlag type, DataComponentGetter components) {
        ArrayList<EnchantmentLevel> enchantmentLevels = EnchantmentLevel.ofList((ItemEnchantments)(Object) this);
        Collections.sort(enchantmentLevels);
        for(EnchantmentLevel el : enchantmentLevels) {
            textConsumer.accept(EnchantmentAppearanceHelper.getName(el));
        }
    }
}
