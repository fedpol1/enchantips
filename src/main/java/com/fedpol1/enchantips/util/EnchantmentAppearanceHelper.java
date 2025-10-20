package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.resources.SymbolSet;
import com.fedpol1.enchantips.resources.Symbols;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.*;

public class EnchantmentAppearanceHelper {

    public static Text getName(EnchantmentLevel enchLevel) {
        int colorFinal = enchLevel.getColor().getRGB();
        World world = MinecraftClient.getInstance().world;

        RegistryKey<Enchantment> key = enchLevel.getKey();
        Enchantment ench = enchLevel.getEnchantment();
        if(ench == null) { return Text.empty(); }

        int level = enchLevel.getLevel();
        int r = ench.getAnvilCost();

        MutableText enchantmentText = MutableText.of(ench.description().getContent());
        MutableText finalText = Text.literal("");

        if(ModOption.SWATCHES_SWITCH.getValue()) {
            colorFinal = ModOption.SWATCHES_FALLBACK_COLOR.getValue().getRGB();
        }
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));

        if (level != 1 || ench.getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Text.translatable("enchantment.level." + level));
        }

        if(ModOption.MAXIMUM_ENCHANTMENT_LEVEL_SWITCH.getValue()) {
            MutableText maxLevelText = Text.literal("/")
                    .append(Text.translatable("enchantment.level." + enchLevel.getEnchantment().getMaxLevel()));
            if(level == ench.getMaxLevel()) {
                if(ModOption.MAXIMUM_ENCHANTMENT_LEVEL_SWITCH_AT_MAX.getValue()) {
                    enchantmentText.append(maxLevelText);
                }
            }
            else if(level > ench.getMaxLevel()) {
                if(ModOption.MAXIMUM_ENCHANTMENT_LEVEL_SWITCH_OVER_MAX.getValue()) {
                    enchantmentText.append(maxLevelText);
                }
            }
            else {
                enchantmentText.append(maxLevelText);
            }
        }

        if(ModOption.SWATCHES_SWITCH.getValue()) {
            finalText.append(Symbols.get(EnchantipsClient.id("swatch")).copy()
                    .withColor(enchLevel.getColor().getRGB())).append(" ");
        }
        if(ModOption.ANVIL_COST_SWITCH.getValue()) {
            finalText.append(TooltipHelper.buildAnvilCost(r, colorFinal)).append(" ");
        }
        if(ModOption.ENCHANTMENT_TAGS_SWITCH.getValue() && world != null) {
            MutableText symbolText = EnchantmentAppearanceHelper
                    .getEnchantmentTagSymbolText(key, world.getRegistryManager());

            finalText.append(symbolText).withColor(colorFinal);
            if(!symbolText.getString().isEmpty()) finalText.append(" "); // only append extra space if needed
        }
        if(ModOption.ENCHANTMENT_TARGETS_SWITCH.getValue() && world != null) {
            MutableText symbolText = EnchantmentAppearanceHelper
                    .getEnchantmentTargetSymbolText(key, world.getRegistryManager());

            finalText.append(symbolText).withColor(colorFinal);
            if(!symbolText.getString().isEmpty()) finalText.append(" "); // only append extra space if needed
        }
        return finalText.append(enchantmentText);
    }

    public static MutableText getEnchantmentTagSymbolText(RegistryKey<Enchantment> key, DynamicRegistryManager registryManager) {
        SymbolSet symbolSet = Symbols.getSet("tags");
        List<Identifier> finalSymbols;
        Optional<RegistryEntry.Reference<Enchantment>> enchantmentReference = registryManager
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(key.getValue());
        if(enchantmentReference.isPresent()) {
            finalSymbols = symbolSet.getApplicableSymbols(
                    enchantmentReference.get().streamTags().map(TagKey::id).toList(),
                    symbolSet.miscSymbol
            );
        } else {
            finalSymbols = symbolSet.unknownSymbol.isEmpty() ? List.of() : List.of(symbolSet.unknownSymbol.get());
        }

        if(finalSymbols.size() > ModOption.ENCHANTMENT_TAGS_LIMIT.getValue()) {
            finalSymbols =  symbolSet.allSymbol.isEmpty() ? List.of() : List.of(symbolSet.allSymbol.get());
        }

        if(finalSymbols.isEmpty()) {
            finalSymbols = symbolSet.noneSymbol.isEmpty() ? List.of() : List.of(symbolSet.noneSymbol.get());
        }

        return beautifySymbolList(finalSymbols);
    }

    public static MutableText getEnchantmentTargetSymbolText(RegistryKey<Enchantment> key, DynamicRegistryManager registryManager) {
        SymbolSet symbolSet = Symbols.getSet("items");
        Enchantment ench = registryManager.getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(key).value();

        // primary items must be a subset of secondary items
        RegistryEntryList<Item> primaryItems = ((EnchantmentAccess)(Object) ench).enchantips$getPrimaryItems();
        RegistryEntryList<Item> secondaryItems = ((EnchantmentAccess)(Object) ench).enchantips$getSecondaryItems();
        RegistryEntryList<Item> filteredPrimaryItems = RegistryEntryList.of(
                primaryItems.stream().filter(
                        i -> EnchantmentAppearanceHelper.canBePrimaryItem(
                                Registries.ITEM.get(i.getKey().get().getValue()),
                                key,
                                primaryItems
                        )
                ).toList()
        );
        RegistryEntryList<Item> filteredSecondaryItems = RegistryEntryList.of(
                secondaryItems.stream().filter(e -> !filteredPrimaryItems.contains(e)).toList()
        );

        List<Identifier> primarySymbols = symbolSet.getApplicableSymbols(
                filteredPrimaryItems.stream().map(i -> i.getKey().get().getValue()).toList(),
                symbolSet.miscSymbol
        );
        List<Identifier> secondarySymbols = symbolSet.getApplicableSymbols(
                filteredSecondaryItems.stream().map(i -> i.getKey().get().getValue()).toList(),
                symbolSet.miscSymbol
        );

        if(primarySymbols.size() > ModOption.ENCHANTMENT_TARGETS_LIMIT.getValue()) {
            primarySymbols = symbolSet.allSymbol.isEmpty() ? List.of() : List.of(symbolSet.allSymbol.get());
        }
        if(secondarySymbols.size() > ModOption.ENCHANTMENT_TARGETS_LIMIT.getValue()) {
            secondarySymbols = symbolSet.allSymbol.isEmpty() ? List.of() : List.of(symbolSet.allSymbol.get());
        }

        ArrayList<Identifier> finalSymbols = new ArrayList<>();
        finalSymbols.addAll(primarySymbols);
        if(!secondarySymbols.isEmpty()) { finalSymbols.add(EnchantipsClient.id("anvil")); }
        finalSymbols.addAll(secondarySymbols);

        if(finalSymbols.isEmpty()) {
            finalSymbols = symbolSet.noneSymbol.isEmpty() ? new ArrayList<>() : new ArrayList<>(List.of(symbolSet.noneSymbol.get()));
        }

        return beautifySymbolList(finalSymbols);
    }

    public static boolean canBePrimaryItem(Item item, RegistryKey<Enchantment> key, RegistryEntryList<Item> primaryItems) {
        World w = MinecraftClient.getInstance().world;
        if(w == null) { return false; }
        RegistryEntry<Enchantment> entry = w.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(key);

        Optional<RegistryEntryList.Named<Enchantment>> entryList = w
                .getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getOptional(EnchantmentTags.IN_ENCHANTING_TABLE);
        if(entryList.isEmpty()) { return false; }
        else if(!entryList.get().contains(entry)) { return false; }

        String itemId = Registries.ITEM.getEntry(item).getIdAsString();
        return primaryItems.stream().map(RegistryEntry::getIdAsString).anyMatch(c -> c.equals(itemId));
    }

    public static MutableText beautifySymbolList(List<Identifier> symbols) {
        if(symbols == null || symbols.isEmpty()) { return Text.empty(); }

        MutableText finalText = Text.empty();
        finalText.append(Symbols.get(symbols.getFirst()));
        symbols
                .subList(1, symbols.size())
                .forEach(fs -> finalText.append(Symbols.SPACE).append(Symbols.get(fs)));
        return finalText;
    }
}
