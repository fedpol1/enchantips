package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

@Mixin(UnbreakableComponent.class)
public class UnbreakableComponentMixin {

    @Inject(method = "appendTooltip", at = @At(value = "INVOKE", target = "Ljava/util/function/Consumer;accept(Ljava/lang/Object;)V"), cancellable = true)
    private void enchantips$modifyTooltip(Item.TooltipContext context, Consumer<Text> tooltip, TooltipType type, CallbackInfo ci) {
        tooltip.accept(TooltipHelper.buildUnbreakable());
        ci.cancel();
    }
}
