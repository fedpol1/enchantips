package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.awt.*;
import java.util.Objects;

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

        MutableText rarityText = TooltipHelper.buildRarity(r, colorFinal);
        MutableText enchantmentText = Text.translatable(((Enchantment)(Object)this).getTranslationKey());
        enchantmentText.setStyle(Style.EMPTY.withColor(colorFinal));
        if (level != 1 || ((Enchantment)(Object)this).getMaxLevel() != 1) {
            enchantmentText.append(" ").append(Text.translatable("enchantment.level." + level));
        }
        rarityText.append(" ").append(enchantmentText);
        if((boolean) ModOption.SHOW_RARITY.getValue()) {
            return rarityText;
        }
        return enchantmentText;
    }

    // intensity dictates how much of a bias there is towards the "high" color
    public float enchantipsGetIntensity(int level) {
        Enchantment t = (Enchantment)(Object)this;
        if(level == t.getMaxLevel()) { return 1.0f; }
        else { return Math.max(0.0f, Math.min(1.0f, (float)(level - t.getMinLevel()) / (t.getMaxLevel() - t.getMinLevel()))); }
    }

    public TextColor enchantipsGetColor(int level) {
        float intensity = this.enchantipsGetIntensity(level);
        GroupNode gn = ModConfigData.get(Objects.requireNonNull((Enchantment) (Object)this));
        int rgbMin = ((Color) ((OptionNode<?>) (gn.getChild(ModConfigData.MIN_COLOR_KEY))).getValue()).getRGB();
        int rgbMax = ((Color) ((OptionNode<?>) (gn.getChild(ModConfigData.MAX_COLOR_KEY))).getValue()).getRGB();

        int r1 = (rgbMin & 0xff0000) >> 16;
        int r2 = (rgbMax & 0xff0000) >> 16;
        int g1 = (rgbMin & 0xff00) >> 8;
        int g2 = (rgbMax & 0xff00) >> 8;
        int b1 = (rgbMin & 0xff);
        int b2 = (rgbMax & 0xff);

        r1 += (r2-r1) * intensity;
        g1 += (g2-g1) * intensity;
        b1 += (b2-b1) * intensity;

        return TextColor.fromRgb((r1<<16) + (g1<<8) + b1);
    }

    public Color enchantipsGetDefaultMinColor() {
        Enchantment t = (Enchantment)(Object)this;
        if(t.isCursed()) { return new Color(0xbf0000); }
        if(t.isTreasure()) { return new Color(0x009f00); }
        return new Color(0x9f7f7f);
    }

    public Color enchantipsGetDefaultMaxColor() {
        Enchantment t = (Enchantment)(Object)this;
        if(t.isCursed()) { return new Color(0xff0000); }
        if(t.isTreasure()) { return new Color(0x00df00); }
        return new Color(0xffdfdf);
    }

    public int enchantipsGetDefaultOrder() {
        Enchantment t = (Enchantment)(Object)this;
        if(t.isCursed()) { return 0; }
        if(t.isTreasure()) { return 1; }
        return 2;
    }

    public boolean enchantipsGetDefaultHighlightVisibility() {
        return true;
    }
}
