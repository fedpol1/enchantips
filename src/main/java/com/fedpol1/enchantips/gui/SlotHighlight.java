package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import java.awt.Color;
import java.util.*;

public abstract class SlotHighlight {

    public static void drawEnchantedItemSlotHighlights(DrawContext context, ScreenHandler handler, int alpha) {
        Slot slot;
        for (int i = 0; i < handler.slots.size(); i++) {
            slot = handler.slots.get(i);
            highlightSingleSlot(context, slot.getStack(), slot.x, slot.y, alpha);
        }
    }

    public static void highlightSingleSlot(DrawContext context, ItemStack stack, int x, int y, int alpha) {
        if(stack.getCount() == 0) { return; }

        NbtList enchNbt = stack.isOf(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantmentNbt(stack) : stack.getEnchantments();
        ArrayList<EnchantmentLevel> arrayOfEnchLevel = EnchantmentLevel.ofList(enchNbt);
        Collections.sort(arrayOfEnchLevel);

        ItemStackAccess stackAccess = (ItemStackAccess)(Object)stack;
        ArrayList<Color> arrayOfColor = new ArrayList<>();
        if(stackAccess.enchantipsIsUnbreakable() && (stackAccess.enchantipsUnbreakableVisible() || !ModOption.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue())) {
            arrayOfColor.add(ModOption.UNBREAKABLE_COLOR.getValue());
        }
        if(stackAccess.enchantipsEnchantmentsVisible() || !ModOption.HIGHLIGHTS_RESPECT_HIDEFLAGS.getValue()) {
            for (EnchantmentLevel levelData : arrayOfEnchLevel) {
                if (!ModConfigData.isEnchantmentHighlighted(levelData.getEnchantment())) { continue; }
                arrayOfColor.add(levelData.getColor());
            }
        }
        drawEnchantments(context, arrayOfColor, x, y, alpha);
    }

    public static void drawEnchantments(DrawContext context, ArrayList<Color> arrayOfColor, int x, int y, int alpha) {
        int limit = Math.min(arrayOfColor.size(), ModOption.HIGHLIGHT_LIMIT.getValue());
        float frac = 16.0f / limit;
        for(int i = 0; i < limit; i++) {
            context.fill(x + Math.round(i * frac), y, x + Math.round((i+1) * frac), y + 16, (arrayOfColor.get(i).getRGB() & 0xffffff) | (alpha << 24) );
        }
    }
}
