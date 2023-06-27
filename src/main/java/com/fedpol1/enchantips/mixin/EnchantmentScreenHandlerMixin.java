package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevelData;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(EnchantmentScreenHandler.class)
public abstract class EnchantmentScreenHandlerMixin implements EnchantmentScreenHandlerAccess {

    public ScrollableTooltipSection enchantipsGetSection(int i) {
        return this.enchantipsSections[i];
    }

    @Inject(method = "onContentChanged(Lnet/minecraft/inventory/Inventory;)V", at = @At(value = "RETURN"))
    public void enchantipsOnContentChanged(Inventory inventory, CallbackInfo ci) {
        if(!(boolean) ModOption.SHOW_EXTRA_ENCHANTMENTS.getData().getValue()) { return; }
        EnchantmentScreenHandler t = (EnchantmentScreenHandler) (Object) this;
        ItemStack stack = t.getSlot(0).getStack();
        for(int i = 0; i < 3; i++) {
            Enchantment givenEnchantment = Enchantment.byRawId(t.enchantmentId[i]);
            if(givenEnchantment == null) { continue; }
            int absoluteLowerBound = EnchantmentFilterer.getLowerBound(givenEnchantment, t.enchantmentLevel[i], stack, t.enchantmentPower[i]);
            int absoluteUpperBound = EnchantmentFilterer.getUpperBound(givenEnchantment, t.enchantmentLevel[i], stack, t.enchantmentPower[i]);

            ArrayList<EnchantmentLevelData> enchantmentLevelData = new ArrayList<>();
            for (Enchantment current : Registries.ENCHANTMENT) {
                if(!current.target.isAcceptableItem(stack.getItem()) && !stack.isOf(Items.BOOK)) { continue; }
                if(!current.canCombine(givenEnchantment)) { continue; }
                if(!current.isAvailableForRandomSelection() || current.isTreasure()) { continue; }
                for(int z = current.getMinLevel(); z <= current.getMaxLevel(); z++) {
                    EnchantmentLevelData enchLevel = EnchantmentLevelData.of(current, z);
                    if(enchLevel.getHighestModifiedLevel() >= absoluteLowerBound && enchLevel.getLowestModifiedLevel() <= absoluteUpperBound) {
                        enchantmentLevelData.add(EnchantmentLevelData.of(current, z));
                    }
                }
            }
            Collections.sort(enchantmentLevelData);
            List<Text> extra = new ArrayList<>();
            for(EnchantmentLevelData levelData : enchantmentLevelData) {
                MutableText text = (MutableText) ((EnchantmentAccess) levelData.getEnchantment()).enchantipsGetName(levelData.getLevel(), stack.isOf(Items.ENCHANTED_BOOK));
                if((boolean) ModOption.SHOW_MODIFIED_ENCHANTMENT_LEVEL.getData().getValue()) {
                    text.append(" ").append(
                            TooltipHelper.buildModifiedLevelForEnchantment(levelData.getLowestModifiedLevel(), levelData.getHighestModifiedLevel())
                    );
                }
                extra.add(text);
            }
            enchantipsSections[i] = new ScrollableTooltipSection(extra);
        }
    }

}
