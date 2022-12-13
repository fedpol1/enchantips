package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.EnchantmentMixinAccess;
import com.fedpol1.enchantips.config.ModConfig;
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

public class SlotHighlightHelper {

    public static void drawSpecialEnchantedItemSlotHighlights(MatrixStack matrices, HandledScreen screen, ScreenHandler handler) {
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            ItemStack slotStack = slot.getStack();
            NbtList slotEnchantments;
            if(slotStack.isOf(Items.ENCHANTED_BOOK)) { slotEnchantments = EnchantedBookItem.getEnchantmentNbt(slotStack); }
            else { slotEnchantments = slot.getStack().getEnchantments(); }

            TextColor finalColor = null;
            EnchantmentPriority priority = EnchantmentPriority.NORMAL;
            float intensity = 0.0f;
            for(int j = 0; j < slotEnchantments.size(); j++) {
                Optional<Enchantment> enchantment = Registry.ENCHANTMENT.getOrEmpty(EnchantmentHelper.getIdFromNbt(slotEnchantments.getCompound(j)));
                int level = EnchantmentHelper.getLevelFromNbt(slotEnchantments.getCompound(j));
                if(enchantment.isPresent()) {
                    EnchantmentMixinAccess e = (EnchantmentMixinAccess)(enchantment.get());
                    EnchantmentPriority pr = e.enchantipsGetPriority(level);
                    float in = e.enchantipsGetIntensity(level);
                    if(pr.ge(priority) && in >= intensity) {
                        finalColor = e.enchantipsGetColor(level);
                        priority = pr;
                        intensity = in;
                    }
                }
            }
            if(finalColor == null) { continue; }
            float[] color = ColorManager.intToFloats(finalColor.getRgb());
            RenderSystem.setShaderColor(color[0], color[1], color[2], 1.0f);
            HandledScreen.drawSlotHighlight(matrices, slot.x, slot.y, screen.getZOffset());
        }
    }

    public static void drawEnchantedBookSlotHighlights(MatrixStack matrices, HandledScreen screen, ScreenHandler handler, NbtList offhandEnchantments) {
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            ItemStack slotStack = slot.getStack();
            NbtList slotEnchantments;
            if(slotStack.isOf(Items.ENCHANTED_BOOK)) { slotEnchantments = EnchantedBookItem.getEnchantmentNbt(slotStack); }
            else { slotEnchantments = slot.getStack().getEnchantments(); }

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
