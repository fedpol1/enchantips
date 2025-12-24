package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentScreenHandlerAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.EnchantmentAppearanceHelper;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevel;
import com.fedpol1.enchantips.util.TooltipHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

@Mixin(AbstractContainerMenu.class)
public abstract class AbstractContainerMenuMixin implements EnchantmentScreenHandlerAccess {

    @Unique
    ScrollableTooltipSection[] enchantips$scrollableSections = new ScrollableTooltipSection[3];

    @Unique
    public ScrollableTooltipSection enchantips$getSection(int i) {
        return this.enchantips$scrollableSections[i];
    }

    @Inject(method = "setData(II)V", at = @At(value = "RETURN"))
    public void enchantips$setProperty(int id, int value, CallbackInfo ci) throws IllegalStateException {
        //noinspection ConstantValue
        if (!((Object)this instanceof EnchantmentMenu handler) ||
            !ModOption.EXTRA_ENCHANTMENTS_SWITCH.getValue()
        ) { return; }

        Level w = Minecraft.getInstance().level;
        if(w == null) { return; }
        RegistryAccess registryManager = w.registryAccess();
        IdMap<Holder<Enchantment>> indexedIterable = registryManager.lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();

        ItemStack stack = handler.getSlot(0).getItem();
        for (int i = 0; i < 3; i++) {
            Holder<Enchantment> givenEnchantment = indexedIterable.byId(handler.enchantClue[i]);
            if (givenEnchantment == null) { continue; }
            EnchantmentLevel enchLevel = EnchantmentLevel.of(givenEnchantment.value(), handler.levelClue[i]);

            int absoluteLowerBound = EnchantmentFilterer.getLowerBound(enchLevel, stack, handler.costs[i]);
            int absoluteUpperBound = EnchantmentFilterer.getUpperBound(enchLevel, stack, handler.costs[i]);

            List<EnchantmentLevel> levels = new ArrayList<>();
            for (Holder<Enchantment> current : indexedIterable) {
                if ((!(stack.is(current.value().getSupportedItems()) && current.value().isPrimaryItem(stack)) && !stack.is(Items.BOOK)) ||
                    !Enchantment.areCompatible(givenEnchantment, current) ||
                    (!current.is(EnchantmentTags.IN_ENCHANTING_TABLE))
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
            List<Component> extra = new ArrayList<>();
            for(EnchantmentLevel levelData : levels) {
                MutableComponent text = (MutableComponent) EnchantmentAppearanceHelper.getName(levelData);
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
