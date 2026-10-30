package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.AbstractContainerMenuAccess;
import com.fedpol1.enchantips.accessor.EnchantmentMenuAccess;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;

import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;


@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin implements AbstractContainerMenuAccess {

    @Shadow
    @Final
    private List<DataSlot> dataSlots;

    public List<DataSlot> enchantips$getDataSlots() {
        return this.dataSlots;
    }

    @Inject(method = "setData(II)V", at = @At(value = "RETURN"))
    public void enchantips$setProperty(int id, int value, CallbackInfo ci) throws IllegalStateException {
        //noinspection ConstantValue
        if (((Object)this instanceof EnchantmentMenu handler) &&
                ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()
        ) {
            ((EnchantmentMenuAccess)handler).enchantips$updateContext(id);
        }
    }
}
