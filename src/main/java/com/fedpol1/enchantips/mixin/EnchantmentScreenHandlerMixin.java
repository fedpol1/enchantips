package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import com.fedpol1.enchantips.util.TooltipHelper;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(ScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin implements EnchantmentScreenHandlerAccess {
    @Unique
    ScrollableTooltipSection[] enchantips$scrollableSections = new ScrollableTooltipSection[3];

    @Unique
    public ScrollableTooltipSection enchantips$getSection(int i) {
        return this.enchantips$scrollableSections[i];
    }

    @Inject(method = "setProperty(II)V", at = @At(value = "RETURN"))
    public void enchantips$setProperty(int id, int value, CallbackInfo ci) {
        //noinspection ConstantValue
        if (!((Object)this instanceof EnchantmentScreenHandler handler) ||
            !ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()
        ) return;

        ItemStack stack = handler.getSlot(0).getStack();
        for (int i = 0; i < 3; i++) {
            Enchantment givenEnchantment = Enchantment.byRawId(handler.enchantmentId[i]);
            if (givenEnchantment == null) continue;

            int absoluteLowerBound = EnchantmentFilterer.getLowerBound(givenEnchantment, handler.enchantmentLevel[i], stack, handler.enchantmentPower[i]);
            int absoluteUpperBound = EnchantmentFilterer.getUpperBound(givenEnchantment, handler.enchantmentLevel[i], stack, handler.enchantmentPower[i]);

            List<EnchantmentLevel> levels = new ArrayList<>();
            for (Enchantment current : Registries.ENCHANTMENT) {
                if ((!(stack.isIn(current.getApplicableItems()) && current.isPrimaryItem(stack)) && !stack.isOf(Items.BOOK)) ||
                    !current.canCombine(givenEnchantment) ||
                    (!current.isAvailableForRandomSelection() || current.isTreasure())
                ) continue;

                for (int z = current.getMinLevel(); z <= current.getMaxLevel(); z++) {
                    EnchantmentLevel level = EnchantmentLevel.of(current, z);

                    if (level.getHighestModifiedLevel() >= absoluteLowerBound &&
                        level.getLowestModifiedLevel() <= absoluteUpperBound
                    ) {
                        levels.add(EnchantmentLevel.of(current, z));
                    }
                }
            }
            Collections.sort(levels);
            List<Text> extra = new ArrayList<>();
            for(EnchantmentLevel levelData : levels) {
                MutableText text = (MutableText) EnchantmentAppearanceHelper.getName(levelData);
                if (ModOption.MODIFIED_ENCHANTING_POWER_SWITCH.getValue()) {
                    text.append(" ").append(
                        TooltipHelper.buildModifiedLevelForEnchantment(
                            levelData.getLowestModifiedLevel(),
                            levelData.getHighestModifiedLevel()
                        )
                    );
                }
                extra.add(text);
            }
            enchantips$scrollableSections[i] = new ScrollableTooltipSection(extra, ModOption.EXTRA_ENCHANTMENTS_LIMIT.getValue());
        }
    }

}
