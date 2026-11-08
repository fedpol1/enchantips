package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.accessor.EnchantmentMenuAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.*;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.enchantment.Enchantment;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin implements EnchantmentAccess {

    @Inject(method = "containerTick()V", at = @At(value = "RETURN"))
    private void enchantips$updateMenu(CallbackInfo ci) {
        EnchantmentMenuAccess menuAccess = (EnchantmentMenuAccess)((EnchantmentScreen)(Object)this).getMenu();
        if(!menuAccess.enchantips$isStale()) {
            menuAccess.enchantips$updateContext(0);
            menuAccess.enchantips$updateContext(1);
            menuAccess.enchantips$updateContext(2);
            menuAccess.enchantips$setStale(true);
        }
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/EnchantmentScreen;isHovering(IIIIDD)Z")),
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void enchantips$renderExtraEnchantments(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float ignored, CallbackInfo ci, float f, boolean bl, int i, int j, int k, Optional<Holder.Reference<Enchantment>> optional, int l, int m, List<Component> list)
    throws IllegalStateException {
        if(optional.isEmpty()) { return; }
        EnchantmentScreen t = (EnchantmentScreen) (Object)this;
        EnchantmentMenu handler = t.getMenu();
        ArrayList<Integer> candidates = EnchantmentSeedSearcher.getLatestCandidates();
        boolean fullHint = EnchantmentSeedSearcher.getLatestCandidates().size() == 1 && ModOption.ENCHANTMENT_SEED_SWITCH.getValue();
        if(fullHint && ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()) {
            ScrollableTooltipSection section = ((EnchantmentMenuAccess)handler).enchantips$getEnchantmentHint(j);
            if(section != null) {
                ScrollableTooltipSection.setActiveSection(section);
                list.addAll(section.getShownTextAll());
            }
        }
        if(ModOption.ENCHANTMENT_SEED_SWITCH.getValue()) {
            if(!ModOption.ENCHANTING_CONTEXT_SWITCH.getValue() || !fullHint) {
                list.add(TooltipHelper.buildSingleTranslatedInteger(
                        Short.toUnsignedInt((short) handler.getEnchantmentSeed()),
                        TooltipHelper.ENCHANTMENT_SEED_PARTIAL_TOOLTIP
                ));
            }
            if(ModOption.ENCHANTING_CONTEXT_SWITCH.getValue()) {
                list.add(fullHint ?
                        TooltipHelper.buildSingleTranslatedInteger(candidates.getFirst(), TooltipHelper.ENCHANTMENT_SEED_FULL_TOOLTIP) :
                        TooltipHelper.buildSingleTranslatedInteger(candidates.size(), TooltipHelper.ENCHANTMENT_SEED_CANDIDATES_TOOLTIP)
                );
            }
        }
        if(ModOption.ENCHANTING_POWER_SWITCH.getValue() && !fullHint) {
            IntBounds bounds = ((EnchantmentMenuAccess)handler).enchantips$getPowerBounds(j);
            if(bounds != null) {
                list.add(TooltipHelper.buildModifiedLevel(bounds.lower(), bounds.upper()));
            }
        }
        if(!fullHint && ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()) {
            ScrollableTooltipSection section = ((EnchantmentMenuAccess)handler).enchantips$getEnchantmentHint(j);
            if(section != null) {
                ScrollableTooltipSection.setActiveSection(section);
                list.addAll(section.getShownTextAll());
            }
        }
    }

    @Redirect(method = "extractRenderState(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/enchantment/Enchantment;getFullname(Lnet/minecraft/core/Holder;I)Lnet/minecraft/network/chat/Component;"))
    private Component enchantips$modifyClueName(Holder<Enchantment> enchantment, int level) {
        return EnchantmentAppearanceHelper.getName(EnchantmentLevel.of(enchantment.value(), level));
    }
}
