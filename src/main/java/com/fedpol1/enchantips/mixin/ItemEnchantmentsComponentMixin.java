package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.accessor.ItemEnchantmentsComponentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.client.item.TooltipType;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin implements ItemEnchantmentsComponentAccess {

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
        ItemEnchantmentsComponent t = (ItemEnchantmentsComponent)(Object) this;
        boolean modifyRarity = t == DataComponentTypes.STORED_ENCHANTMENTS;
        ArrayList<EnchantmentLevel> enchantmentLevels = EnchantmentLevel.ofList(t);
        Collections.sort(enchantmentLevels);
        for(EnchantmentLevel el : enchantmentLevels) {
            tooltip.accept(EnchantmentAppearanceHelper.getName(el, modifyRarity));
        }
    }

    @Override
    public boolean enchantipsShowInTooltip() {
        return this.showInTooltip;
    }
}
