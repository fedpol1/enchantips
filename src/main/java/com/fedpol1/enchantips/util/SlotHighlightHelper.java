package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.EnchantmentColorDataEntry;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.ItemStackAccess;
import net.minecraft.client.gui.DrawableHelper;
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

import java.util.*;

public abstract class SlotHighlightHelper extends DrawableHelper {

    public static void drawSpecialEnchantedItemSlotHighlights(MatrixStack matrices, ScreenHandler handler) {
        Slot slot;
        ItemStack slotStack;
        ArrayList<EnchantmentColorDataEntry> arrayOfEnchantmentData;
        ArrayList<TextColor> arrayOfColor;

        for (int i = 0; i < handler.slots.size(); i++) {
            slot = handler.slots.get(i);
            slotStack = slot.getStack();
            if(slotStack.getCount() == 0) { continue; }

            arrayOfEnchantmentData = new ArrayList<>();
            for(Enchantment e : EnchantmentListHelper.getAllEnchantments(slotStack)) {
                arrayOfEnchantmentData.add(ModConfig.individualColors.get(Objects.requireNonNull(Registry.ENCHANTMENT.getId(e)).toString()));
            }
            Collections.sort(arrayOfEnchantmentData, null);

            ItemStackAccess stackAccess = (ItemStackAccess)(Object)slotStack;
            arrayOfColor = new ArrayList<>();
            if(stackAccess.enchantipsIsUnbreakable() && (stackAccess.enchantipsUnbreakableVisible() || !ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue())) {
                arrayOfColor.add(ModConfig.ENCHANTMENT_SPECIAL.getValue());
            }
            if(stackAccess.enchantipsEnchantmentsVisible() || !ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue()) {
                float intensity;
                ItemStack tempStack = slotStack;
                if(slotStack.isOf(Items.ENCHANTED_BOOK)) {
                    tempStack = slotStack.copy();
                    tempStack.setSubNbt(ItemStack.ENCHANTMENTS_KEY, EnchantedBookItem.getEnchantmentNbt(slotStack)); // enchanted books store enchantments differently
                }
                for (EnchantmentColorDataEntry dataEntry : arrayOfEnchantmentData) {
                    if (!dataEntry.showHighlight) { continue; }
                    intensity = ((EnchantmentAccess) (dataEntry.enchantment)).enchantipsGetIntensity(EnchantmentHelper.getLevel(dataEntry.enchantment, tempStack));
                    arrayOfColor.add(ColorManager.lerpColor(dataEntry.minColor, dataEntry.maxColor, intensity));
                }
            }
            drawEnchantments(matrices, arrayOfColor, slot.x, slot.y);
        }
    }

    public static void drawEnchantments(MatrixStack matrices, ArrayList<TextColor> arrayOfColor, int x, int y) {
        int limit = Math.min(arrayOfColor.size(), ModConfig.HIGHLIGHT_LIMIT.getValue());
        float frac = 16.0f / limit;
        for(int i = 0; i < limit; i++) {
            HandledScreen.fill(matrices, x + Math.round(i * frac), y, x + Math.round((i+1) * frac), y + 16, 0xff000000 | arrayOfColor.get(i).getRgb());
        }
    }

    public static void drawEnchantedBookSlotHighlights(MatrixStack matrices, ScreenHandler handler, NbtList offhandEnchantments) {
        for (int i = 0; i < handler.slots.size(); i++) {
            Slot slot = handler.slots.get(i);
            ItemStack slotStack = slot.getStack();
            if(!((ItemStackAccess)(Object)slotStack).enchantipsEnchantmentsVisible() && ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue()) { continue; }
            NbtList slotEnchantments = slotStack.getEnchantments();
            if(slotStack.isOf(Items.ENCHANTED_BOOK)) { slotEnchantments = EnchantedBookItem.getEnchantmentNbt(slotStack); }

            int matches = EnchantmentListHelper.countMatches(offhandEnchantments, slotEnchantments, false);
            if(matches > 0) {
                int color;
                color = (matches == offhandEnchantments.size() ? ModConfig.SLOT_HIGHLIGHT_FULL_MATCH : ModConfig.SLOT_HIGHLIGHT_PARTIAL_MATCH).getValue().getRgb();
                HandledScreen.fill(matrices, slot.x, slot.y, slot.x + 16, slot.y + 16, 0xff000000 | color);
            }
        }
    }
}
