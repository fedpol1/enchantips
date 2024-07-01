package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.component.type.UnbreakableComponent;
import net.minecraft.item.Item;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;

import java.util.function.Consumer;

@Mixin(UnbreakableComponent.class)
public class UnbreakableComponentMixin {

    @Mutable
    @Final
    @Shadow
    private final boolean showInTooltip;

    public UnbreakableComponentMixin(boolean showInTooltip) {
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
        tooltip.accept(TooltipHelper.buildUnbreakable());
    }
}
