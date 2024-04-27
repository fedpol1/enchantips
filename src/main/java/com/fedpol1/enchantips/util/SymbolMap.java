package com.fedpol1.enchantips.util;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.LinkedHashMap;
import java.util.Map;

public class SymbolMap {

    public static Map<Item, Symbol> SYMBOLS = new LinkedHashMap<>();

    static {
        SYMBOLS.put(Items.LEATHER_BOOTS, Symbol.BOOTS);
        SYMBOLS.put(Items.GOLDEN_BOOTS, Symbol.BOOTS);
        SYMBOLS.put(Items.CHAINMAIL_BOOTS, Symbol.BOOTS);
        SYMBOLS.put(Items.IRON_BOOTS, Symbol.BOOTS);
        SYMBOLS.put(Items.DIAMOND_BOOTS, Symbol.BOOTS);
        SYMBOLS.put(Items.NETHERITE_BOOTS, Symbol.BOOTS);

        SYMBOLS.put(Items.LEATHER_LEGGINGS, Symbol.LEGGINGS);
        SYMBOLS.put(Items.GOLDEN_LEGGINGS, Symbol.LEGGINGS);
        SYMBOLS.put(Items.CHAINMAIL_LEGGINGS, Symbol.LEGGINGS);
        SYMBOLS.put(Items.IRON_LEGGINGS, Symbol.LEGGINGS);
        SYMBOLS.put(Items.DIAMOND_LEGGINGS, Symbol.LEGGINGS);
        SYMBOLS.put(Items.NETHERITE_LEGGINGS, Symbol.LEGGINGS);

        SYMBOLS.put(Items.LEATHER_CHESTPLATE, Symbol.CHESTPLATE);
        SYMBOLS.put(Items.GOLDEN_CHESTPLATE, Symbol.CHESTPLATE);
        SYMBOLS.put(Items.CHAINMAIL_CHESTPLATE, Symbol.CHESTPLATE);
        SYMBOLS.put(Items.IRON_CHESTPLATE, Symbol.CHESTPLATE);
        SYMBOLS.put(Items.DIAMOND_CHESTPLATE, Symbol.CHESTPLATE);
        SYMBOLS.put(Items.NETHERITE_CHESTPLATE, Symbol.CHESTPLATE);

        SYMBOLS.put(Items.LEATHER_HELMET, Symbol.HELMET);
        SYMBOLS.put(Items.GOLDEN_HELMET, Symbol.HELMET);
        SYMBOLS.put(Items.CHAINMAIL_HELMET, Symbol.HELMET);
        SYMBOLS.put(Items.IRON_HELMET, Symbol.HELMET);
        SYMBOLS.put(Items.DIAMOND_HELMET, Symbol.HELMET);
        SYMBOLS.put(Items.NETHERITE_HELMET, Symbol.HELMET);
        SYMBOLS.put(Items.TURTLE_HELMET, Symbol.HELMET);

        SYMBOLS.put(Items.WITHER_SKELETON_SKULL, Symbol.SKULL);
        SYMBOLS.put(Items.SKELETON_SKULL, Symbol.SKULL);
        SYMBOLS.put(Items.CREEPER_HEAD, Symbol.SKULL);
        SYMBOLS.put(Items.DRAGON_HEAD, Symbol.SKULL);
        SYMBOLS.put(Items.PIGLIN_HEAD, Symbol.SKULL);
        SYMBOLS.put(Items.PLAYER_HEAD, Symbol.SKULL);
        SYMBOLS.put(Items.ZOMBIE_HEAD, Symbol.SKULL);

        SYMBOLS.put(Items.CARVED_PUMPKIN, Symbol.PUMPKIN);
        SYMBOLS.put(Items.ELYTRA, Symbol.ELYTRA);

        SYMBOLS.put(Items.WOODEN_SWORD, Symbol.SWORD);
        SYMBOLS.put(Items.STONE_SWORD, Symbol.SWORD);
        SYMBOLS.put(Items.IRON_SWORD, Symbol.SWORD);
        SYMBOLS.put(Items.GOLDEN_SWORD, Symbol.SWORD);
        SYMBOLS.put(Items.DIAMOND_SWORD, Symbol.SWORD);
        SYMBOLS.put(Items.NETHERITE_SWORD, Symbol.SWORD);

        SYMBOLS.put(Items.WOODEN_AXE, Symbol.AXE);
        SYMBOLS.put(Items.STONE_AXE, Symbol.AXE);
        SYMBOLS.put(Items.IRON_AXE, Symbol.AXE);
        SYMBOLS.put(Items.GOLDEN_AXE, Symbol.AXE);
        SYMBOLS.put(Items.DIAMOND_AXE, Symbol.AXE);
        SYMBOLS.put(Items.NETHERITE_AXE, Symbol.AXE);

        SYMBOLS.put(Items.WOODEN_PICKAXE, Symbol.PICKAXE);
        SYMBOLS.put(Items.STONE_PICKAXE, Symbol.PICKAXE);
        SYMBOLS.put(Items.IRON_PICKAXE, Symbol.PICKAXE);
        SYMBOLS.put(Items.GOLDEN_PICKAXE, Symbol.PICKAXE);
        SYMBOLS.put(Items.DIAMOND_PICKAXE, Symbol.PICKAXE);
        SYMBOLS.put(Items.NETHERITE_PICKAXE, Symbol.PICKAXE);

        SYMBOLS.put(Items.WOODEN_SHOVEL, Symbol.SHOVEL);
        SYMBOLS.put(Items.STONE_SHOVEL, Symbol.SHOVEL);
        SYMBOLS.put(Items.IRON_SHOVEL, Symbol.SHOVEL);
        SYMBOLS.put(Items.GOLDEN_SHOVEL, Symbol.SHOVEL);
        SYMBOLS.put(Items.DIAMOND_SHOVEL, Symbol.SHOVEL);
        SYMBOLS.put(Items.NETHERITE_SHOVEL, Symbol.SHOVEL);

        SYMBOLS.put(Items.WOODEN_HOE, Symbol.HOE);
        SYMBOLS.put(Items.STONE_HOE, Symbol.HOE);
        SYMBOLS.put(Items.IRON_HOE, Symbol.HOE);
        SYMBOLS.put(Items.GOLDEN_HOE, Symbol.HOE);
        SYMBOLS.put(Items.DIAMOND_HOE, Symbol.HOE);
        SYMBOLS.put(Items.NETHERITE_HOE, Symbol.HOE);

        SYMBOLS.put(Items.SHEARS, Symbol.SHEARS);
        SYMBOLS.put(Items.TRIDENT, Symbol.TRIDENT);
        SYMBOLS.put(Items.MACE, Symbol.MACE);
        SYMBOLS.put(Items.SHIELD, Symbol.SHIELD);
        SYMBOLS.put(Items.BOW, Symbol.BOW);
        SYMBOLS.put(Items.CROSSBOW, Symbol.CROSSBOW);
        SYMBOLS.put(Items.FISHING_ROD, Symbol.FISHING_ROD);
        SYMBOLS.put(Items.CARROT_ON_A_STICK, Symbol.BAITED_ROD);
        SYMBOLS.put(Items.WARPED_FUNGUS_ON_A_STICK, Symbol.BAITED_ROD);
        SYMBOLS.put(Items.BRUSH, Symbol.BRUSH);
        SYMBOLS.put(Items.FLINT_AND_STEEL, Symbol.FIRESTARTER);
        SYMBOLS.put(Items.COMPASS, Symbol.COMPASS);
    }
}
