package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
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
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.*;

public class EnchantmentAppearanceHelper {

    public static Text getName(EnchantmentLevel enchLevel) {
        int colorFinal = enchLevel.getColor().getRGB();

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
            finalText.append(Symbols.get("swatch").copy().withColor(colorFinal)).append(" ");
        }
        if(ModOption.ANVIL_COST_SWITCH.getValue()) {
            finalText.append(TooltipHelper.buildAnvilCost(r, colorFinal)).append(" ");
        }
        if(ModOption.ENCHANTMENT_TARGETS_SWITCH.getValue()) {
            World world = MinecraftClient.getInstance().world;
            if(world != null) {
                MutableText symbolText = EnchantmentAppearanceHelper
                        .getEnchantmentTargetSymbolText(key, world.getRegistryManager());

                finalText.append(symbolText).withColor(colorFinal);
                if(!symbolText.getString().isEmpty()) finalText.append(" "); // only append extra space if needed
            }
        }
        return finalText.append(enchantmentText);
    }

    public static MutableText getEnchantmentTargetSymbolText(RegistryKey<Enchantment> key, DynamicRegistryManager registryManager) {
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

        List<Text> primarySymbols = Symbols.getSet("items").getApplicableSymbols(
                filteredPrimaryItems.stream().map(i -> i.getKey().get().getValue()).toList(),
                Symbols.get("miscellaneous_item")
        );
        List<Text> secondarySymbols = Symbols.getSet("items").getApplicableSymbols(
                filteredSecondaryItems.stream().map(i -> i.getKey().get().getValue()).toList(),
                Symbols.get("miscellaneous_item")
        );

        if(primarySymbols.size() > ModOption.ENCHANTMENT_TARGETS_LIMIT.getValue()) {
            primarySymbols = new ArrayList<>(Collections.singleton(Symbols.get("all")));
        }
        if(secondarySymbols.size() > ModOption.ENCHANTMENT_TARGETS_LIMIT.getValue()) {
            secondarySymbols = new ArrayList<>(Collections.singleton(Symbols.get("all")));
        }

        ArrayList<Text> finalSymbols = new ArrayList<>();
        finalSymbols.addAll(primarySymbols);
        if(!secondarySymbols.isEmpty()) { finalSymbols.add(Symbols.get("anvil")); }
        finalSymbols.addAll(secondarySymbols);

        if(finalSymbols.isEmpty()) {
            finalSymbols = new ArrayList<>(Collections.singleton(Symbols.get("none")));
        }

        MutableText finalText = Text.empty();
        finalSymbols.forEach(finalText::append);
        return finalText;
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
}
