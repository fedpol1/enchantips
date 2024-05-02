package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin implements EnchantmentAccess {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantmentScreen;isPointWithinBounds(IIIIDD)Z")),
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void enchantips$renderExtraEnchantments(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, boolean bl, int i, int j, int k, Enchantment enchantment, int l, int m, List<Text> list) {
        EnchantmentScreen t = (EnchantmentScreen) (Object)this;
        EnchantmentScreenHandler handler = t.getScreenHandler();
        int tableLevel = handler.enchantmentPower[j];
        ItemStack itemStack = handler.getSlot(0).getStack();
        int absoluteLowerBound = EnchantmentFilterer.getLowerBound(enchantment, l, itemStack, tableLevel);
        int absoluteUpperBound = EnchantmentFilterer.getUpperBound(enchantment, l, itemStack, tableLevel);
        if(ModOption.MODIFIED_ENCHANTING_POWER_SWITCH.getValue()) {
            list.add(TooltipHelper.buildModifiedLevel(absoluteLowerBound, absoluteUpperBound));
        }
        if(ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()) {
            ScrollableTooltipSection section = ((EnchantmentScreenHandlerAccess)handler).enchantips$getSection(j);
            ScrollableTooltipSection.setActiveSection(section);
            list.addAll(section.getShownTextAll());
        }
    }
}
