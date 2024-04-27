package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;
import java.util.*;

public class EnchantmentAppearanceHelper {

    public static Map<TagKey<Item>, Symbols[]> ICONS = new HashMap<>();

    static {
        ICONS.put(ItemTags.FOOT_ARMOR_ENCHANTABLE, new Symbols[]{Symbols.BOOTS});
        ICONS.put(ItemTags.LEG_ARMOR_ENCHANTABLE, new Symbols[]{Symbols.LEGGINGS});
        ICONS.put(ItemTags.CHEST_ARMOR_ENCHANTABLE, new Symbols[]{Symbols.CHESTPLATE});
        ICONS.put(ItemTags.HEAD_ARMOR_ENCHANTABLE, new Symbols[]{Symbols.HELMET});
        ICONS.put(ItemTags.ARMOR_ENCHANTABLE, new Symbols[]{Symbols.BOOTS, Symbols.LEGGINGS, Symbols.CHESTPLATE, Symbols.HELMET});
        ICONS.put(ItemTags.SWORD_ENCHANTABLE, new Symbols[]{Symbols.SWORD});
        ICONS.put(ItemTags.FIRE_ASPECT_ENCHANTABLE, new Symbols[]{Symbols.SWORD, Symbols.MACE});
        ICONS.put(ItemTags.SHARP_WEAPON_ENCHANTABLE, new Symbols[]{Symbols.SWORD, Symbols.AXE});
        ICONS.put(ItemTags.WEAPON_ENCHANTABLE, new Symbols[]{Symbols.SWORD, Symbols.AXE, Symbols.MACE});
        ICONS.put(ItemTags.MINING_ENCHANTABLE, new Symbols[]{Symbols.AXE, Symbols.PICKAXE, Symbols.SHOVEL, Symbols.HOE, Symbols.SHEARS});
        ICONS.put(ItemTags.MINING_LOOT_ENCHANTABLE, new Symbols[]{Symbols.AXE, Symbols.PICKAXE, Symbols.SHOVEL, Symbols.HOE});
        ICONS.put(ItemTags.FISHING_ENCHANTABLE, new Symbols[]{Symbols.FISHING_ROD});
        ICONS.put(ItemTags.TRIDENT_ENCHANTABLE, new Symbols[]{Symbols.TRIDENT});
        ICONS.put(ItemTags.DURABILITY_ENCHANTABLE, new Symbols[]{Symbols.ALL});
        ICONS.put(ItemTags.BOW_ENCHANTABLE, new Symbols[]{Symbols.BOW});
        ICONS.put(ItemTags.EQUIPPABLE_ENCHANTABLE, new Symbols[]{Symbols.BOOTS, Symbols.LEGGINGS, Symbols.CHESTPLATE, Symbols.HELMET, Symbols.ELYTRA, Symbols.SKULL});
        ICONS.put(ItemTags.CROSSBOW_ENCHANTABLE, new Symbols[]{Symbols.CROSSBOW});
        ICONS.put(ItemTags.VANISHING_ENCHANTABLE, new Symbols[]{Symbols.ALL, Symbols.MISCELLANEOUS});
        ICONS.put(ItemTags.MACE_ENCHANTABLE, new Symbols[]{Symbols.MACE});
    }

    public static Text getName(EnchantmentLevel enchLevel, boolean modifyRarity) {
        int colorFinal = enchLevel.getColor().getRGB();

        Enchantment ench = enchLevel.getEnchantment();
        int level = enchLevel.getLevel();
        int r = ench.getAnvilCost();

        if(modifyRarity && r != 0) {
            r = Math.max(1, r / 2);
        }

        MutableText swatchText = Symbols.SWATCH.decorate(colorFinal);
        MutableText rarityText = TooltipHelper.buildRarity(r, colorFinal);
        MutableText enchantmentText = Text.translatable(ench.getTranslationKey());
        MutableText finalText = Text.literal("");

        if(ModOption.SHOW_SWATCHES.getValue()) {
            colorFinal = ModOption.DECORATION.getValue().getRGB();
        }
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));

        if (level != 1 || (ench).getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Text.translatable("enchantment.level." + level));
        }

        if(ModOption.SHOW_ENCHANTMENT_MAX_LEVEL.getValue()) {
            enchantmentText.append("/").append(Text.translatable("enchantment.level." + enchLevel.getEnchantment().getMaxLevel()));
        }

        if(ModOption.SHOW_SWATCHES.getValue()) {
            finalText.append(swatchText).append(" ");
        }
        if(ModOption.SHOW_RARITY.getValue()) {
            finalText.append(rarityText).append(" ");
        }
        if(ModOption.SHOW_ENCHANTMENT_TARGETS.getValue()) {
            finalText.append(((EnchantmentAccess) enchLevel.getEnchantment()).enchantipsGetEnchantmentTargetSymbolText().withColor(colorFinal)).append(" ");
        }
        return finalText.append(enchantmentText);
    }

    public static MutableText getEnchantmentTargetSymbolText(Enchantment ench) {
        TagKey<Item> primaryTag = ((EnchantmentAccess) ench).enchantipsGetPrimaryItems();
        TagKey<Item> secondaryTag = ((EnchantmentAccess) ench).enchantipsGetSecondaryItems();

        Symbols[] primarySymbols = ICONS.get(primaryTag).clone();
        Symbols[] secondarySymbols = ICONS.get(secondaryTag).clone();

        for(int i = 0; i < primarySymbols.length; i++) {
            for(int j = 0; j < secondarySymbols.length; j++) {
                if(secondarySymbols[j] == primarySymbols[i]) {
                    secondarySymbols[j] = null;
                }
            }
        }

        ArrayList<Symbols> primarySymbolsNoNull = new ArrayList<>();
        ArrayList<Symbols> secondarySymbolsNoNull = new ArrayList<>();
        for(Symbols s : primarySymbols) {
            if(s != null) { primarySymbolsNoNull.add(s); }
        }
        for(Symbols s : secondarySymbols) {
            if(s != null) { secondarySymbolsNoNull.add(s); }
        }
        Collections.sort(primarySymbolsNoNull);
        Collections.sort(secondarySymbolsNoNull);

        ArrayList<Symbols> finalSymbols = new ArrayList<>();
        finalSymbols.addAll(primarySymbolsNoNull);
        if(!secondarySymbolsNoNull.isEmpty()) { finalSymbols.add(Symbols.ANVIL); }
        finalSymbols.addAll(secondarySymbolsNoNull);
        return Symbols.mergeAndDecorate(finalSymbols);
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
