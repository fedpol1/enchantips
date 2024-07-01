package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.awt.*;
import java.util.*;

public class EnchantmentAppearanceHelper {

    public static Text getName(EnchantmentLevel enchLevel) {
        int colorFinal = enchLevel.getColor().getRGB();

        RegistryKey<Enchantment> key = enchLevel.getKey();
        Enchantment ench = enchLevel.getEnchantment();
        if(ench == null) { return Text.empty(); }

        int level = enchLevel.getLevel();
        int r = ench.getAnvilCost();

        MutableText swatchText = Symbol.SWATCH.decorate(colorFinal);
        MutableText anvilCostText = TooltipHelper.buildAnvilCost(r, colorFinal);
        MutableText enchantmentText = MutableText.of(ench.description().getContent());
        MutableText finalText = Text.literal("");

        if(ModOption.SWATCHES_SWITCH.getValue()) {
            colorFinal = ModOption.SWATCHES_FALLBACK_COLOR.getValue().getRGB();
        }
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));

        if (level != 1 || (ench).getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Text.translatable("enchantment.level." + level));
        }

        if(ModOption.MAXIMUM_ENCHANTMENT_LEVEL_SWITCH.getValue()) {
            enchantmentText.append("/").append(Text.translatable("enchantment.level." + enchLevel.getEnchantment().getMaxLevel()));
        }

        if(ModOption.SWATCHES_SWITCH.getValue()) {
            finalText.append(swatchText).append(" ");
        }
        if(ModOption.ANVIL_COST_SWITCH.getValue()) {
            finalText.append(anvilCostText).append(" ");
        }
        if(ModOption.ENCHANTMENT_TARGETS_SWITCH.getValue()) {
            World world = MinecraftClient.getInstance().world;
            if(world != null) {
                finalText.append(EnchantmentAppearanceHelper
                        .getEnchantmentTargetSymbolText(key, world.getRegistryManager())
                ).withColor(colorFinal).append(" ");
            }
        }
        return finalText.append(enchantmentText);
    }

    public static MutableText getEnchantmentTargetSymbolText(RegistryKey<Enchantment> key, DynamicRegistryManager registryManager) {
        Enchantment ench = registryManager.get(RegistryKeys.ENCHANTMENT).entryOf(key).value();

        RegistryEntryList<Item> primaryItems = ((EnchantmentAccess)(Object) ench).enchantips$getPrimaryItems();
        RegistryEntryList<Item> secondaryItems = ((EnchantmentAccess)(Object) ench).enchantips$getSecondaryItems();

        HashSet<Item> acceptableItems = new HashSet<>();

        // primary items MUST be a subset of secondary items
        // therefore we do not iterate through primary items
        for(RegistryEntry<Item> i : secondaryItems) {
            acceptableItems.add(i.value());
        }

        ArrayList<Symbol> primarySymbols = new ArrayList<>();
        ArrayList<Symbol> secondarySymbols = new ArrayList<>();
        ArrayList<Symbol> addTo;

        // populate normal symbols
        for(Map.Entry<Item, Symbol> e : SymbolMap.SYMBOLS.entrySet()) {
            if(acceptableItems.contains(e.getKey())) {
                addTo = EnchantmentAppearanceHelper.canBePrimaryItem(e.getKey(), key, primaryItems) ?
                        primarySymbols : secondarySymbols;
                if(!addTo.contains(e.getValue())) {
                    addTo.add(e.getValue());
                }
            }
        }

        // populate misc symbols
        for(Item i : acceptableItems) {
            if(!SymbolMap.SYMBOLS.containsKey(i)) {
                addTo = EnchantmentAppearanceHelper.canBePrimaryItem(i, key, primaryItems) ?
                        primarySymbols : secondarySymbols;
                if(!addTo.isEmpty() && addTo.getLast() != Symbol.MISCELLANEOUS) {
                    addTo.add(Symbol.MISCELLANEOUS);
                }
            }
        }

        if(primarySymbols.size() > ModOption.ENCHANTMENT_TARGETS_LIMIT.getValue()) {
            primarySymbols = new ArrayList<>(Collections.singleton(Symbol.ALL));
        }
        if(secondarySymbols.size() > ModOption.ENCHANTMENT_TARGETS_LIMIT.getValue()) {
            secondarySymbols = new ArrayList<>(Collections.singleton(Symbol.ALL));
        }

        ArrayList<Symbol> finalSymbols = new ArrayList<>();
        finalSymbols.addAll(primarySymbols);
        if(!secondarySymbols.isEmpty()) { finalSymbols.add(Symbol.ANVIL); }
        finalSymbols.addAll(secondarySymbols);

        if(finalSymbols.isEmpty()) {
            finalSymbols = new ArrayList<>(Collections.singleton(Symbol.NONE));
        }
        return Symbol.mergeAndDecorate(finalSymbols);
    }

    private static boolean canBePrimaryItem(Item item, RegistryKey<Enchantment> key, RegistryEntryList<Item> primaryItems) {
        if(item.getEnchantability() == 0) { return false; }
        World w = MinecraftClient.getInstance().world;
        if(w == null) { return false; }
        RegistryEntry<Enchantment> entry = w.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(key);

        Optional<RegistryEntryList.Named<Enchantment>> entryList = w
                .getRegistryManager()
                .get(RegistryKeys.ENCHANTMENT)
                .getEntryList(EnchantmentTags.IN_ENCHANTING_TABLE);
        if(entryList.isEmpty()) { return false; }
        else if(entryList.get().contains(entry)) { return false; }

        return !primaryItems.contains(RegistryEntry.of(item));
    }

    public static Color getDefaultMinColor(RegistryKey<Enchantment> key) {
        Color base = new Color(0x9f7f7f);

        World w = MinecraftClient.getInstance().world;
        if(w == null) { return base; }
        RegistryEntry<Enchantment> entry = w.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(key);

        if(entry.isIn(EnchantmentTags.CURSE)) { return new Color(0xbf0000); }
        if(!entry.isIn(EnchantmentTags.IN_ENCHANTING_TABLE)) { return new Color(0x009f00); }
        return base;
    }

    public static Color getDefaultMaxColor(RegistryKey<Enchantment> key) {
        Color base = new Color(0xffdfdf);

        World w = MinecraftClient.getInstance().world;
        if(w == null) { return base; }
        RegistryEntry<Enchantment> entry = w.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(key);

        if(entry.isIn(EnchantmentTags.CURSE)) { return new Color(0xff0000); }
        if(!entry.isIn(EnchantmentTags.IN_ENCHANTING_TABLE)) { return new Color(0x00df00); }
        return base;
    }

    public static Color getDefaultOvermaxColor(RegistryKey<Enchantment> key) {
        Color base = new Color(0xffdf3f);

        World w = MinecraftClient.getInstance().world;
        if(w == null) { return base; }
        RegistryEntry<Enchantment> entry = w.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(key);

        if(entry.isIn(EnchantmentTags.CURSE)) { return new Color(0xff5f1f); }
        if(!entry.isIn(EnchantmentTags.IN_ENCHANTING_TABLE)) { return new Color(0x1fff3f); }
        return base;
    }

    public static int getDefaultOrder(RegistryKey<Enchantment> key) {
        int base = 2;

        World w = MinecraftClient.getInstance().world;
        if(w == null) { return base; }
        RegistryEntry<Enchantment> entry = w.getRegistryManager().get(RegistryKeys.ENCHANTMENT).entryOf(key);

        if(entry.isIn(EnchantmentTags.CURSE)) { return 0; }
        if(!entry.isIn(EnchantmentTags.IN_ENCHANTING_TABLE)) { return 1; }
        return base;
    }

    public static boolean getDefaultHighlightVisibility(RegistryKey<Enchantment> key) {
        return true;
    }
}
