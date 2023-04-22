package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.ItemStackAccess;
import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.EnchantmentLevelData;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import java.awt.Color;
import java.util.*;

public abstract class SlotHighlight extends DrawableHelper {

    public static void drawEnchantedItemSlotHighlights(MatrixStack matrices, ScreenHandler handler, int alpha) {
        Slot slot;
        for (int i = 0; i < handler.slots.size(); i++) {
            slot = handler.slots.get(i);
            highlightSingleSlot(matrices, slot.getStack(), slot.x, slot.y, alpha);
        }
    }

    public static void highlightSingleSlot(MatrixStack matrices, ItemStack stack, int x, int y, int alpha) {
        if(stack.getCount() == 0) { return; }

        NbtList enchNbt = stack.isOf(Items.ENCHANTED_BOOK) ? EnchantedBookItem.getEnchantmentNbt(stack) : stack.getEnchantments();
        ArrayList<EnchantmentLevelData> arrayOfEnchLevel = EnchantmentLevelData.ofList(enchNbt);
        Collections.sort(arrayOfEnchLevel);

        ItemStackAccess stackAccess = (ItemStackAccess)(Object)stack;
        ArrayList<Color> arrayOfColor = new ArrayList<>();
        if(stackAccess.enchantipsIsUnbreakable() && (stackAccess.enchantipsUnbreakableVisible() || !(boolean) ModOption.HIGHLIGHTS_RESPECT_HIDEFLAGS.getData().getValue())) {
            arrayOfColor.add((Color) ModOption.UNBREAKABLE_COLOR.getData().getValue());
        }
        if(stackAccess.enchantipsEnchantmentsVisible() || !(boolean) ModOption.HIGHLIGHTS_RESPECT_HIDEFLAGS.getData().getValue()) {
            for (EnchantmentLevelData levelData : arrayOfEnchLevel) {
                if (!ModConfigData.isEnchantmentHighlighted(levelData.getEnchantment())) { continue; }
                arrayOfColor.add(levelData.getColor());
            }
        }
        drawEnchantments(matrices, arrayOfColor, x, y, alpha);
    }

    public static void drawEnchantments(MatrixStack matrices, ArrayList<Color> arrayOfColor, int x, int y, int alpha) {
        int limit = Math.min(arrayOfColor.size(), (int) ModOption.HIGHLIGHT_LIMIT.getData().getValue());
        float frac = 16.0f / limit;
        for(int i = 0; i < limit; i++) {
            HandledScreen.fill(matrices, x + Math.round(i * frac), y, x + Math.round((i+1) * frac), y + 16, (arrayOfColor.get(i).getRGB() & 0xffffff) | (alpha << 24) );
        }
    }
}
