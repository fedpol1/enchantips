package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.*;

public class EnchantmentAppearanceHelper {

    public static Text getName(EnchantmentLevel enchLevel) {
        int colorFinal = enchLevel.getColor().getRGB();

        Enchantment ench = enchLevel.getEnchantment();
        int level = enchLevel.getLevel();
        int r = ench.getAnvilCost();

        MutableText swatchText = Symbol.SWATCH.decorate(colorFinal);
        MutableText anvilCostText = TooltipHelper.buildAnvilCost(r, colorFinal);
        MutableText enchantmentText = Text.translatable(ench.getTranslationKey());
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
            finalText.append(EnchantmentAppearanceHelper.getEnchantmentTargetSymbolText(ench)).withColor(colorFinal).append(" ");
        }
        return finalText.append(enchantmentText);
    }

    public static MutableText getEnchantmentTargetSymbolText(Enchantment ench) {
        TagKey<Item> primaryTag = ((EnchantmentAccess) ench).enchantips$getPrimaryItems();
        TagKey<Item> secondaryTag = ((EnchantmentAccess) ench).enchantips$getSecondaryItems();

        HashSet<Item> acceptableItems = new HashSet<>();
        for(RegistryEntry<Item> i : Registries.ITEM.iterateEntries(primaryTag)) {
            acceptableItems.add(i.value());
        }
        for(RegistryEntry<Item> i : Registries.ITEM.iterateEntries(secondaryTag)) {
            acceptableItems.add(i.value());
        }

        ArrayList<Symbol> primarySymbols = new ArrayList<>();
        ArrayList<Symbol> secondarySymbols = new ArrayList<>();
        ArrayList<Symbol> addTo;

        // populate normal symbols
        for(Map.Entry<Item, Symbol> e : SymbolMap.SYMBOLS.entrySet()) {
            if(acceptableItems.contains(e.getKey())) {
                addTo = EnchantmentAppearanceHelper.canBePrimaryItem(e.getKey(), ench, primaryTag) ?
                        primarySymbols : secondarySymbols;
                if(!addTo.contains(e.getValue())) {
                    addTo.add(e.getValue());
                }
            }
        }

        // populate misc symbols
        for(Item i : acceptableItems) {
            if(!SymbolMap.SYMBOLS.containsKey(i)) {
                addTo = EnchantmentAppearanceHelper.canBePrimaryItem(i, ench, primaryTag) ?
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

    private static boolean canBePrimaryItem(Item item, Enchantment enchantment, TagKey<Item> primaryTag) {
        if(item.getEnchantability() == 0) { return false; }
        if(!enchantment.isAvailableForRandomSelection()) { return false; }
        if(enchantment.isTreasure()) { return false; } // non-treasure curses can be selected
        if(!item.getRegistryEntry().isIn(primaryTag)) { return false; }
        return true;
    }

    public static Color getDefaultMinColor(Enchantment e) {
        if(e.isCursed()) { return new Color(0xbf0000); }
        if(e.isTreasure()) { return new Color(0x009f00); }
        return new Color(0x9f7f7f);
    }

    public static Color getDefaultMaxColor(Enchantment e) {
        if(e.isCursed()) { return new Color(0xff0000); }
        if(e.isTreasure()) { return new Color(0x00df00); }
        return new Color(0xffdfdf);
    }

    public static Color getDefaultOvermaxColor(Enchantment e) {
        if(e.isCursed()) { return new Color(0xff5f1f); }
        if(e.isTreasure()) { return new Color(0x1fff3f); }
        return new Color(0xffdf3f);
    }

    public static int getDefaultOrder(Enchantment e) {
        if(e.isCursed()) { return 0; }
        if(e.isTreasure()) { return 1; }
        return 2;
    }

    public static boolean getDefaultHighlightVisibility(Enchantment e) {
        return true;
    }
}
