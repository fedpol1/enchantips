package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.ItemEnchantmentsComponentAccess;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Consumer;

@Mixin(ItemEnchantmentsComponent.class)
public class ItemEnchantmentsComponentMixin implements ItemEnchantmentsComponentAccess {

    @Final
    @Shadow
    boolean showInTooltip;

    @Inject(method = "appendTooltip", at = @At("HEAD"), cancellable = true)
    private void enchantips$modifyTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        ci.cancel();

        if(!this.showInTooltip) {
            return;
        }
        ArrayList<EnchantmentLevel> enchantmentLevels = EnchantmentLevel.ofList((ItemEnchantmentsComponent)(Object) this);
        Collections.sort(enchantmentLevels);
        for(EnchantmentLevel el : enchantmentLevels) {
            tooltip.accept(EnchantmentAppearanceHelper.getName(el));
        }
    }

    @Override
    public boolean enchantips$showInTooltip() {
        return this.showInTooltip;
    }
}
