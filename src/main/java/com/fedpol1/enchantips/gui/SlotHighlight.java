package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.accessor.ItemStackAccess;
import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import java.awt.Color;
import java.util.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public abstract class SlotHighlight {

    public static void drawEnchantedItemSlotHighlights(GuiGraphics context, AbstractContainerMenu handler, int alpha) {
        Slot slot;
        for (int i = 0; i < handler.slots.size(); i++) {
            slot = handler.slots.get(i);
            highlightSingleSlot(context, slot.getItem(), slot.x, slot.y, alpha);
        }
    }

    public static void highlightSingleSlot(GuiGraphics context, ItemStack stack, int x, int y, int alpha) {
        if(stack.getCount() == 0) { return; }

        DataComponentType<ItemEnchantments> componentType = stack.is(Items.ENCHANTED_BOOK) ? DataComponents.STORED_ENCHANTMENTS : DataComponents.ENCHANTMENTS;
        ArrayList<EnchantmentLevel> arrayOfEnchLevel = EnchantmentLevel.ofList(stack.get(componentType));
        Collections.sort(arrayOfEnchLevel);

        ItemStackAccess stackAccess = (ItemStackAccess)(Object)stack;
        ArrayList<Color> arrayOfColor = new ArrayList<>();
        if(stackAccess.enchantips$isUnbreakable() && ModOption.UNBREAKABLE_HIGHLIGHT.getValue() &&
                (stackAccess.enchantips$unbreakableVisible() || !ModOption.HIGHLIGHTS_SWITCH_OVERRIDE.getValue())) {
            arrayOfColor.add(ModOption.UNBREAKABLE_COLOR.getValue());
        }
        if(stackAccess.enchantips$enchantmentsVisible() || !ModOption.HIGHLIGHTS_SWITCH_OVERRIDE.getValue()) {
            for (EnchantmentLevel levelData : arrayOfEnchLevel) {
                if (!ModConfigData.isEnchantmentHighlighted(levelData.getKey())) { continue; }
                arrayOfColor.add(levelData.getColor());
            }
        }
        drawEnchantments(context, arrayOfColor, x, y, alpha);
    }

    public static void drawEnchantments(GuiGraphics context, ArrayList<Color> arrayOfColor, int x, int y, int alpha) {
        int limit = Math.min(arrayOfColor.size(), ModOption.HIGHLIGHTS_LIMIT.getValue());
        float frac = 16.0f / limit;
        for(int i = 0; i < limit; i++) {
            context.fill(x + Math.round(i * frac), y, x + Math.round((i+1) * frac), y + 16, (arrayOfColor.get(i).getRGB() & 0xffffff) | (alpha << 24) );
        }
    }
}
