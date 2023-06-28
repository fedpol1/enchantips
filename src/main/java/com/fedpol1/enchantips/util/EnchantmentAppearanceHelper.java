package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;

import java.awt.*;

public class EnchantmentAppearanceHelper {

    public static Text getName(EnchantmentLevel enchLevel, boolean modifyRarity) {
        TextColor colorFinal = TextColor.fromRgb(enchLevel.getColor().getRGB());
        int r;
        Enchantment ench = enchLevel.getEnchantment();
        int level = enchLevel.getLevel();
        switch (ench.getRarity()) {
            case COMMON -> r= 1;
            case UNCOMMON -> r = 2;
            case RARE -> r = 4;
            case VERY_RARE -> r = 8;
            default -> r = 0;
        }

        if(modifyRarity && r != 0) {
            r = Math.max(1, r / 2);
        }

        MutableText rarityText = TooltipHelper.buildRarity(r, colorFinal);
        MutableText enchantmentText = Text.translatable(ench.getTranslationKey());
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));
        if (level != 1 || (ench).getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Text.translatable("enchantment.level." + level));
        }
        rarityText.append(" ").append(enchantmentText);
        if(ModOption.SHOW_RARITY.getValue()) {
            return rarityText;
        }
        return enchantmentText;
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

    public static int getDefaultOrder(Enchantment e) {
        if(e.isCursed()) { return 0; }
        if(e.isTreasure()) { return 1; }
        return 2;
    }

    public static boolean getDefaultHighlightVisibility(Enchantment e) {
        return true;
    }
}
