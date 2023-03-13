package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.ItemStackAccess;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.TextColor;

import java.util.*;

public abstract class SlotHighlightHelper extends DrawableHelper {

    public static void drawEnchantedItemSlotHighlights(MatrixStack matrices, ScreenHandler handler) {
        Slot slot;
        ItemStack slotStack;
        ArrayList<EnchantmentLevelData> arrayOfEnchLevel;
        ArrayList<TextColor> arrayOfColor;
        NbtList enchNbt;

        for (int i = 0; i < handler.slots.size(); i++) {
            slot = handler.slots.get(i);
            slotStack = slot.getStack();
            if(slotStack.getCount() == 0) { continue; }

            enchNbt = slotStack.isOf(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantmentNbt(slotStack) : slotStack.getEnchantments();
            arrayOfEnchLevel = EnchantmentLevelData.ofList(enchNbt);
            Collections.sort(arrayOfEnchLevel);

            ItemStackAccess stackAccess = (ItemStackAccess)(Object)slotStack;
            arrayOfColor = new ArrayList<>();
            if(stackAccess.enchantipsIsUnbreakable() && (stackAccess.enchantipsUnbreakableVisible() || !ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue())) {
                arrayOfColor.add(ModConfig.ENCHANTMENT_SPECIAL.getValue());
            }
            if(stackAccess.enchantipsEnchantmentsVisible() || !ModConfig.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue()) {
                for (EnchantmentLevelData levelData : arrayOfEnchLevel) {
                    if (!levelData.getDataEntry().showHighlight) { continue; }
                    arrayOfColor.add(levelData.getColor());
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
}
