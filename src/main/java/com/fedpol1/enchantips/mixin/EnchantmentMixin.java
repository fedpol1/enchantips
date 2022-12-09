package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantmentMixinAccess;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.ColorManager;
import com.fedpol1.enchantips.util.TooltipBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Enchantment.class)
public class EnchantmentMixin implements EnchantmentMixinAccess {

    @Shadow @Final private Enchantment.Rarity rarity;

    /**
     * @author fedpol1
     * @reason enchantment overhaul, show intensity and treasure status of enchantments in tooltip
     */
    @Overwrite
    public Text getName(int level) {
        return enchantipsGetName(level, false);
    }

    public Text enchantipsGetName(int level, boolean modifyRarity) {
        Enchantment t = ((Enchantment)(Object)this);

        float intensity = Math.max(0.0f, Math.min(1.0f, (float)(level - t.getMinLevel()) / t.getMaxLevel()));
        if(level == t.getMaxLevel()) {
            intensity = 1.0f; // case where enchantment maxes at 1 level, make it bright instead of dim
        }
        TextColor colorMin = ModConfig.ENCHANTMENT_NORMAL_MIN.getColor();
        TextColor colorMax = ModConfig.ENCHANTMENT_NORMAL_MAX.getColor();

        if (level > t.getMaxLevel() || level < t.getMinLevel()) {
            colorMin = ModConfig.ENCHANTMENT_OVERLEVELLED.getColor();
            colorMax = ModConfig.ENCHANTMENT_OVERLEVELLED.getColor();
            intensity = 1.0f;
        }
        else if (((Enchantment)(Object)this).isCursed()) {
            colorMin = ModConfig.ENCHANTMENT_CURSE_MIN.getColor();
            colorMax = ModConfig.ENCHANTMENT_CURSE_MAX.getColor();
        }
        else if (((Enchantment)(Object)this).isTreasure()) {
            colorMin = ModConfig.ENCHANTMENT_TREASURE_MIN.getColor();
            colorMax = ModConfig.ENCHANTMENT_TREASURE_MAX.getColor();
        }

        TextColor colorFinal = ColorManager.lerpColor(colorMin, colorMax, intensity);

        int r;
        switch (t.getRarity()) {
            case COMMON -> r= 1;
            case UNCOMMON -> r = 2;
            case RARE -> r = 4;
            case VERY_RARE -> r = 8;
            default -> r = 0;
        }

        if(modifyRarity && r != 0) {
            r = Math.max(1, r / 2);
        }

        MutableText rarityText = TooltipBuilder.buildRarity(r, colorFinal);
        MutableText enchantmentText = Text.translatable(((Enchantment)(Object)this).getTranslationKey());
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));
        if (level != 1 || ((Enchantment)(Object)this).getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Text.translatable("enchantment.level." + level));
        }
        rarityText.append(" ").append(enchantmentText);
        if(ModConfig.SHOW_RARITY.getValue()) {
            return rarityText;
        }
        return enchantmentText;
    }
}
