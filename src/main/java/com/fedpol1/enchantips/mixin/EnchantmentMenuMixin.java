package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.AbstractContainerMenuAccess;
import com.fedpol1.enchantips.accessor.EnchantmentMenuAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(EnchantmentMenu.class)
public abstract class EnchantmentMenuMixin implements EnchantmentMenuAccess {

    @Mutable
    @Shadow
    @Final
    private final Container enchantSlots;

    @Shadow
    @Final
    public int[] costs;

    @Shadow
    @Final
    public int[] levelClue;
    @Unique
    private final ScrollableTooltipSection[] enchantips$extraHints = new ScrollableTooltipSection[3];

    @Unique
    private final IntBounds[] enchantips$powerBounds = new IntBounds[3];

    @Unique
    private int enchantips$finalDataSlot;

    protected EnchantmentMenuMixin(Container enchantSlots) {
        this.enchantSlots = enchantSlots;
    }

    @Unique
    public ScrollableTooltipSection enchantips$getEnchantmentHint(int i) {
        return this.enchantips$extraHints[i];
    }

    @Unique
    public IntBounds enchantips$getPowerBounds(int i) {
        return this.enchantips$powerBounds[i];
    }

    @Inject(method = "<init>(ILnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/world/inventory/ContainerLevelAccess;)V", at = @At(value = "RETURN"))
    private void enchantips$findLastDataSlot(int containerId, Inventory inventory, ContainerLevelAccess access, CallbackInfo ci) {
        this.enchantips$finalDataSlot = ((AbstractContainerMenuAccess)this).enchantips$getDataSlots().size() - 1;
    }

    @Inject(method = "clickMenuButton(Lnet/minecraft/world/entity/player/Player;I)Z", at = @At(value = "HEAD", ordinal = 0))
    private void enchantips$resetContext(Player player, int buttonId, CallbackInfoReturnable<Boolean> cir) {
        ClientLevel lev = Minecraft.getInstance().level;
        EnchantmentMenu t = (EnchantmentMenu)(Object)this;
        if(lev != null && lev.isClientSide() && t.costs[buttonId] > 0) {
            EnchantmentFilterContext.reset();
        }
    }

    // only update context for data slots corresponding to levelCost, otherwise weird desynced state can occur when
    //   hotkeying items to and from the enchanting slot
    public void enchantips$updateContext(int updatedDataSlot) {
        if(updatedDataSlot < this.enchantips$finalDataSlot - 2 || updatedDataSlot > this.enchantips$finalDataSlot) { return; }
        int slotIndex = updatedDataSlot - (this.enchantips$finalDataSlot - 2);
        EnchantmentMenu t = (EnchantmentMenu)(Object)this;
        Level world = Minecraft.getInstance().level;
        if(world == null) { return; }
        ItemStack itemStack = this.enchantSlots.getItem(0);
        if (itemStack.isEmpty() || !itemStack.isEnchantable()) {
            this.enchantips$extraHints[slotIndex] = null;
            this.enchantips$powerBounds[slotIndex] = null;
            return;
        }

        IdMap<Holder<Enchantment>> enchantmentMap = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
        Holder<Enchantment> enchantmentHolder = enchantmentMap.byId(t.enchantClue[slotIndex]);
        if(enchantmentHolder == null || t.levelClue[slotIndex] < 0 || t.costs[slotIndex] < 0) { return; }

        Enchantment givenEnchantment = enchantmentHolder.value();
        EnchantmentLevel enchLevel = EnchantmentLevel.of(givenEnchantment, t.levelClue[slotIndex]);
        IntBounds bounds = new IntBounds(
                EnchantmentFilterer.getLowerBound(enchLevel, itemStack, t.costs[slotIndex]),
                EnchantmentFilterer.getUpperBound(enchLevel, itemStack, t.costs[slotIndex])
        );
        if(ModOption.MODIFIED_ENCHANTING_POWER_CONTEXT_SWITCH.getValue()) {
            Enchantable enchComponent = itemStack.get(DataComponents.ENCHANTABLE);
            int enchantability = enchComponent == null ? 0 : enchComponent.value();
            EnchantmentFilterContext.put(this.costs[slotIndex], enchantability, slotIndex, bounds.lower(), bounds.upper());
            bounds = EnchantmentFilterContext.getBounds(this.costs[slotIndex], enchantability, slotIndex);
        }
        this.enchantips$powerBounds[slotIndex] = bounds;

        List<EnchantmentLevel> levels = new ArrayList<>();
        for (Holder<Enchantment> current : enchantmentMap) {
            if ((!(itemStack.is(current.value().getSupportedItems()) && current.value().isPrimaryItem(itemStack)) && !itemStack.is(Items.BOOK)) ||
                    !Enchantment.areCompatible(enchantmentHolder, current) ||
                    (!current.is(EnchantmentTags.IN_ENCHANTING_TABLE))
            ) { continue; }

            for (int z = current.value().getMinLevel(); z <= current.value().getMaxLevel(); z++) {
                EnchantmentLevel level = EnchantmentLevel.of(current.value(), z);

                if (level.getHighestModifiedLevel() >= this.enchantips$powerBounds[slotIndex].lower() &&
                        level.getLowestModifiedLevel() <= this.enchantips$powerBounds[slotIndex].upper() &&
                        level.getLowestModifiedLevel() <= level.getHighestModifiedLevel()
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
        this.enchantips$extraHints[slotIndex] = new ScrollableTooltipSection(extra, ModOption.EXTRA_ENCHANTMENTS_LIMIT.getValue());
    }
}
