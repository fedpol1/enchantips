package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.EnchantmentTags;
import net.minecraft.screen.EnchantmentScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.collection.IndexedIterable;
import net.minecraft.world.World;
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
    public void enchantips$setProperty(int id, int value, CallbackInfo ci) throws IllegalStateException {
        //noinspection ConstantValue
        if (!((Object)this instanceof EnchantmentScreenHandler handler) ||
            !ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()
        ) { return; }

        World w = MinecraftClient.getInstance().world;
        if(w == null) { return; }
        DynamicRegistryManager registryManager = w.getRegistryManager();
        IndexedIterable<RegistryEntry<Enchantment>> indexedIterable = registryManager.get(RegistryKeys.ENCHANTMENT).getIndexedEntries();

        ItemStack stack = handler.getSlot(0).getStack();
        for (int i = 0; i < 3; i++) {
            RegistryEntry<Enchantment> givenEnchantment = indexedIterable.get(handler.enchantmentId[i]);
            if (givenEnchantment == null) { continue; }
            EnchantmentLevel enchLevel = EnchantmentLevel.of(givenEnchantment.value(), handler.enchantmentLevel[i]);

            int absoluteLowerBound = EnchantmentFilterer.getLowerBound(enchLevel, stack, handler.enchantmentPower[i]);
            int absoluteUpperBound = EnchantmentFilterer.getUpperBound(enchLevel, stack, handler.enchantmentPower[i]);

            List<EnchantmentLevel> levels = new ArrayList<>();
            for (RegistryEntry<Enchantment> current : indexedIterable) {
                if ((!(stack.isIn(current.value().getApplicableItems()) && current.value().isPrimaryItem(stack)) && !stack.isOf(Items.BOOK)) ||
                    !Enchantment.canBeCombined(givenEnchantment, current) ||
                    (!current.isIn(EnchantmentTags.IN_ENCHANTING_TABLE))
                ) { continue; }

                for (int z = current.value().getMinLevel(); z <= current.value().getMaxLevel(); z++) {
                    EnchantmentLevel level = EnchantmentLevel.of(current.value(), z);

                    if (level.getHighestModifiedLevel() >= absoluteLowerBound &&
                        level.getLowestModifiedLevel() <= absoluteUpperBound
                    ) {
                        levels.add(EnchantmentLevel.of(current.value(), z));
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
