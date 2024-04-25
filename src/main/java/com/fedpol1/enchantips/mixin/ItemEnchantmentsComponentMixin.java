package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin {

    @Mutable
    @Final
    @Shadow
    final boolean showInTooltip;

    public ItemEnchantmentsComponentMixin(boolean showInTooltip) {
        this.showInTooltip = showInTooltip;
    }

    /**
     * @author fedpol1
     * @reason sort enchantments in tooltip
     */
    @Overwrite
    public void appendTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type) {
        if(!this.showInTooltip) {
            return;
        }
        ArrayList<EnchantmentLevel> enchantmentLevels = EnchantmentLevel.ofList((ItemEnchantmentsComponent) (Object)this);
        Collections.sort(enchantmentLevels);
        for(EnchantmentLevel el : enchantmentLevels) {
            tooltip.accept(el.getEnchantment().getName(el.getLevel()));
        }
    }
}
