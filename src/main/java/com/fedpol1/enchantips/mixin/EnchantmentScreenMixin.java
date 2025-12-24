package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import com.fedpol1.enchantips.util.TooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin implements EnchantmentAccess {

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EnchantmentScreen;isHovering(IIIIDD)Z")),
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void enchantips$renderExtraEnchantments(GuiGraphics context, int mouseX, int mouseY, float delta, CallbackInfo ci, float f, boolean bl, int i, int j, int k, Optional<Holder.Reference<Enchantment>> optional, int l, int m, List<Component> list)
    throws IllegalStateException {
        if(optional.isEmpty()) { return; }
        Enchantment enchantment = optional.get().value();
        EnchantmentScreen t = (EnchantmentScreen) (Object)this;
        EnchantmentMenu handler = t.getMenu();
        EnchantmentLevel enchLevel = EnchantmentLevel.of(enchantment, l);
        int tableLevel = handler.costs[j];
        ItemStack itemStack = handler.getSlot(0).getItem();
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

    @Redirect(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;"))
    private Component enchantips$modifyClueName(Holder<Enchantment> enchantment, int level) {
        return EnchantmentAppearanceHelper.getName(EnchantmentLevel.of(enchantment.value(), level));
    }
}
