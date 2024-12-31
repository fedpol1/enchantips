package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import com.fedpol1.enchantips.util.TooltipHelper;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Optional;

@Mixin(EnchantmentScreen.class)
public class EnchantmentScreenMixin {

    @Inject(method = "render(Lnet/minecraft/client/gui/DrawContext;IIF)V",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantmentScreen;isPointWithinBounds(IIIIDD)Z")),
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER)
    )
    private void enchantips$renderExtraEnchantments(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci, @Local(ordinal = 3) int j, @Local Optional<RegistryEntry.Reference<Enchantment>> optional, @Local(ordinal = 5) int l, @Local List<Text> list) {
        if(optional.isEmpty()) { return; }
        Enchantment enchantment = optional.get().value();
        EnchantmentScreen t = (EnchantmentScreen) (Object)this;
        EnchantmentScreenHandler handler = t.getScreenHandler();
        EnchantmentLevel enchLevel = EnchantmentLevel.of(enchantment, l);
        int tableLevel = handler.enchantmentPower[j];
        ItemStack itemStack = handler.getSlot(0).getStack();
        int absoluteLowerBound = EnchantmentFilterer.getLowerBound(enchLevel, itemStack, tableLevel);
        int absoluteUpperBound = EnchantmentFilterer.getUpperBound(enchLevel, itemStack, tableLevel);
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
