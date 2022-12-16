package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.ColorManager;
import com.fedpol1.enchantips.util.EnchantmentPriority;
import com.fedpol1.enchantips.util.TooltipBuilder;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(Enchantment.class)
public abstract class EnchantmentMixin implements EnchantmentAccess {

    /**
     * @author fedpol1
     * @reason overhaul enchantment tooltips
     */
    @Overwrite
    public Text getName(int level) {
        return this.enchantipsGetName(level, false);
    }

    public Text enchantipsGetName(int level, boolean modifyRarity) {
        Enchantment t = ((Enchantment)(Object)this);

        TextColor colorFinal = this.enchantipsGetColor(level);

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

    // intensity dictates how much of a bias there is towards the "high" color
    public float enchantipsGetIntensity(int level) {
        Enchantment t = (Enchantment)(Object)this;
        if(level == t.getMaxLevel()) { return 1.0f; }
        if(this.enchantipsGetPriority(level) == EnchantmentPriority.OVERLEVELLED) { return 1.0f; }
        else { return Math.max(0.0f, Math.min(1.0f, (float)(level - t.getMinLevel()) / t.getMaxLevel())); }
    }

    // different priorities have different respective colors
    // higher priorities take precedence
    public EnchantmentPriority enchantipsGetPriority(int level) {
        Enchantment t = (Enchantment)(Object)this;
        if(level > t.getMaxLevel() || level < t.getMinLevel()) { return EnchantmentPriority.OVERLEVELLED;}
        if(t.isCursed()) { return EnchantmentPriority.CURSED;}
        if(t.isTreasure()) { return EnchantmentPriority.TREASURE;}
        return EnchantmentPriority.NORMAL;
    }

    public TextColor enchantipsGetColor(int level) {
        float intensity = this.enchantipsGetIntensity(level);
        EnchantmentPriority priority = this.enchantipsGetPriority(level);

        TextColor colorMin = ModConfig.ENCHANTMENT_NORMAL_MIN.getColor();
        TextColor colorMax = ModConfig.ENCHANTMENT_NORMAL_MAX.getColor();

        if (priority == EnchantmentPriority.OVERLEVELLED) {
            colorMin = ModConfig.ENCHANTMENT_OVERLEVELLED.getColor();
            colorMax = ModConfig.ENCHANTMENT_OVERLEVELLED.getColor();
        }
        else if (priority == EnchantmentPriority.CURSED) {
            colorMin = ModConfig.ENCHANTMENT_CURSE_MIN.getColor();
            colorMax = ModConfig.ENCHANTMENT_CURSE_MAX.getColor();
        }
        else if (priority == EnchantmentPriority.TREASURE) {
            colorMin = ModConfig.ENCHANTMENT_TREASURE_MIN.getColor();
            colorMax = ModConfig.ENCHANTMENT_TREASURE_MAX.getColor();
        }

        return ColorManager.lerpColor(colorMin, colorMax, intensity);
    }
}
