package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.resources.SymbolSet;
import com.fedpol1.enchantips.resources.Symbols;
import java.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class EnchantmentAppearanceHelper {

    public static Component getName(EnchantmentLevel enchLevel) {
        int colorFinal = enchLevel.getColor().getRGB();
        Level world = Minecraft.getInstance().level;

        ResourceKey<Enchantment> key = enchLevel.getKey();
        Enchantment ench = enchLevel.getEnchantment();
        if(ench == null) { return Component.empty(); }

        int level = enchLevel.getLevel();
        int r = ench.getAnvilCost();

        MutableComponent enchantmentText = MutableComponent.create(ench.description().getContents());
        MutableComponent finalText = Component.literal("");

        if(ModOption.SWATCHES_SWITCH.getValue()) {
            colorFinal = ModOption.SWATCHES_FALLBACK_COLOR.getValue().getRGB();
        }
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));

        if (level != 1 || ench.getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Component.translatable("enchantment.level." + level));
        }

        if(ModOption.MAXIMUM_ENCHANTMENT_LEVEL_SWITCH.getValue()) {
            MutableComponent maxLevelText = Component.literal("/")
                    .append(Component.translatable("enchantment.level." + enchLevel.getEnchantment().getMaxLevel()));
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
            MutableComponent symbolText = EnchantmentAppearanceHelper
                    .getEnchantmentTagSymbolText(key, world.registryAccess());

            finalText.append(symbolText).withColor(colorFinal);
            if(!symbolText.getString().isEmpty()) finalText.append(" "); // only append extra space if needed
        }
        if(ModOption.ENCHANTMENT_TARGETS_SWITCH.getValue() && world != null) {
            MutableComponent symbolText = EnchantmentAppearanceHelper
                    .getEnchantmentTargetSymbolText(key, world.registryAccess());

            finalText.append(symbolText).withColor(colorFinal);
            if(!symbolText.getString().isEmpty()) finalText.append(" "); // only append extra space if needed
        }
        return finalText.append(enchantmentText);
    }

    public static MutableComponent getEnchantmentTagSymbolText(ResourceKey<Enchantment> key, RegistryAccess registryManager) {
        SymbolSet symbolSet = Symbols.getSet("tags");
        List<Identifier> finalSymbols;
        Optional<Holder.Reference<Enchantment>> enchantmentReference = registryManager
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(key.identifier());
        if(enchantmentReference.isPresent()) {
            finalSymbols = symbolSet.getApplicableSymbols(
                    enchantmentReference.get().tags().map(TagKey::location).toList(),
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

    public static MutableComponent getEnchantmentTargetSymbolText(ResourceKey<Enchantment> key, RegistryAccess registryManager) {
        SymbolSet symbolSet = Symbols.getSet("items");
        Enchantment ench = registryManager.lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key).value();

        // primary items must be a subset of secondary items
        HolderSet<Item> primaryItems = ((EnchantmentAccess)(Object) ench).enchantips$getPrimaryItems();
        HolderSet<Item> secondaryItems = ((EnchantmentAccess)(Object) ench).enchantips$getSecondaryItems();
        HolderSet<Item> filteredPrimaryItems = HolderSet.direct(
                primaryItems.stream().filter(
                        i -> EnchantmentAppearanceHelper.canBePrimaryItem(
                                BuiltInRegistries.ITEM.getValue(i.unwrapKey().get().identifier()),
                                key,
                                primaryItems
                        )
                ).toList()
        );
        HolderSet<Item> filteredSecondaryItems = HolderSet.direct(
                secondaryItems.stream().filter(e -> !filteredPrimaryItems.contains(e)).toList()
        );

        List<Identifier> primarySymbols = symbolSet.getApplicableSymbols(
                filteredPrimaryItems.stream().map(i -> i.unwrapKey().get().identifier()).toList(),
                symbolSet.miscSymbol
        );
        List<Identifier> secondarySymbols = symbolSet.getApplicableSymbols(
                filteredSecondaryItems.stream().map(i -> i.unwrapKey().get().identifier()).toList(),
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

    public static boolean canBePrimaryItem(Item item, ResourceKey<Enchantment> key, HolderSet<Item> primaryItems) {
        Level w = Minecraft.getInstance().level;
        if(w == null) { return false; }
        Holder<Enchantment> entry = w.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(key);

        Optional<HolderSet.Named<Enchantment>> entryList = w
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(EnchantmentTags.IN_ENCHANTING_TABLE);
        if(entryList.isEmpty()) { return false; }
        else if(!entryList.get().contains(entry)) { return false; }

        String itemId = BuiltInRegistries.ITEM.wrapAsHolder(item).getRegisteredName();
        return primaryItems.stream().map(Holder::getRegisteredName).anyMatch(c -> c.equals(itemId));
    }

    public static MutableComponent beautifySymbolList(List<Identifier> symbols) {
        if(symbols == null || symbols.isEmpty()) { return Component.empty(); }

        MutableComponent finalText = Component.empty();
        finalText.append(Symbols.get(symbols.getFirst()));
        symbols
                .subList(1, symbols.size())
                .forEach(fs -> finalText.append(Symbols.SPACE).append(Symbols.get(fs)));
        return finalText;
    }
}
