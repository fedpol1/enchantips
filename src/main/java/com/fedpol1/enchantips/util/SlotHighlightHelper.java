package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.ItemStackAccess;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.TextColor;
import net.minecraft.util.registry.Registry;

import java.util.Optional;

public abstract class SlotHighlightHelper {

    private static float[] getSpecialEnchantedSlotColor(ItemStack stack) {
        NbtList enchantments = stack.getEnchantments();
        if(stack.isOf(Items.ENCHANTED_BOOK)) { enchantments = EnchantedBookItem.getEnchantmentNbt(stack); }

        TextColor finalColor = null;
        EnchantmentPriority priority = EnchantmentPriority.NORMAL;
        float intensity = 0.0f;
        ItemStackAccess stackAccess = (ItemStackAccess)(Object)stack;

        // treat unbreakable as an overlevelled unbreaking enchantment
        if(stackAccess.enchantipsIsUnbreakable() && (stackAccess.enchantipsUnbreakableVisible() || !ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue())) {
            finalColor = ModConfig.ENCHANTMENT_OVERLEVELLED.getColor();
            priority = EnchantmentPriority.OVERLEVELLED;
            intensity = 1.0f;
        }

        if(stackAccess.enchantipsEnchantmentsVisible() || !ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue()) {
            // determine enchantment colour
            for (int j = 0; j < enchantments.size(); j++) {
                Optional<Enchantment> enchantment = Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(enchantments.getCompound(j)));
                int level = EnchantmentHelper.getLevelFromNbt(enchantments.getCompound(j));
                if (enchantment.isEmpty()) { continue; }
                EnchantmentAccess e = (EnchantmentAccess) (enchantment.get());
                EnchantmentPriority currentPriority = e.enchantipsGetPriority(level);
                float currentIntensity = e.enchantipsGetIntensity(level);
                if (currentPriority.ge(priority) && currentIntensity >= intensity) {
                    finalColor = e.enchantipsGetColor(level);
                    priority = currentPriority;
                    intensity = currentIntensity;
                }
            }
        }
        if(finalColor == null) { return null; }
        return ColorManager.intToFloats(finalColor.getRgb());
    }

    public static void drawSpecialEnchantedItemSlotHighlights(MatrixStack matrices, HandledScreen screen, ScreenHandler handler) {
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            float[] color = SlotHighlightHelper.getSpecialEnchantedSlotColor(slot.getStack());
            if(color != null) {
                RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0f);
                HandledScreen.drawSlotHighlight(matrices, slot.x, slot.y, screen.getZOffset());
            }
        }
    }

    public static void drawEnchantedBookSlotHighlights(MatrixStack matrices, HandledScreen screen, ScreenHandler handler, NbtList offhandEnchantments) {
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            ItemStack slotStack = slot.getStack();
            if(!((ItemStackAccess)(Object)slotStack).enchantipsEnchantmentsVisible() && ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue()) { continue; }
            NbtList slotEnchantments = slotStack.getEnchantments();
            if(slotStack.isOf(Items.ENCHANTED_BOOK)) { slotEnchantments = EnchantedBookItem.getEnchantmentNbt(slotStack); }

            int matches = EnchantmentListUtil.countMatches(offhandEnchantments, slotEnchantments, false);
            if(matches > 0) {
                float[] color;
                if(matches == offhandEnchantments.size()) { color = ColorManager.intToFloats(ModConfig.SLOT_HIGHLIGHT_FULL_MATCH.getColor().getRgb()); }
                else { color = ColorManager.intToFloats(ModConfig.SLOT_HIGHLIGHT_PARTIAL_MATCH.getColor().getRgb()); }
                RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0f);
                HandledScreen.drawSlotHighlight(matrices, slot.x, slot.y, screen.getZOffset());
            }
        }
    }
}
