package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.awt.*;

public class EnchantmentAppearanceHelper {

    public static Text getName(EnchantmentLevel enchLevel, boolean modifyRarity) {
        int colorFinal = enchLevel.getColor().getRGB();

        Enchantment ench = enchLevel.getEnchantment();
        int level = enchLevel.getLevel();
        int r = ench.getAnvilCost();

        if(modifyRarity && r != 0) {
            r = Math.max(1, r / 2);
        }

        MutableText swatchText = TooltipHelper.buildSwatch(colorFinal);
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

        if(ModOption.SHOW_SWATCHES.getValue()) {
            finalText.append(swatchText).append(" ");
        }
        if(ModOption.SHOW_RARITY.getValue()) {
            finalText.append(rarityText).append(" ");
        }
        return finalText.append(enchantmentText);
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
