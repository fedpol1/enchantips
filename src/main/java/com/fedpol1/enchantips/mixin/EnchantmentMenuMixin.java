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
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
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

    @Unique
    public boolean enchantips$stale;

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
    public boolean enchantips$isStale() {
        return this.enchantips$stale;
    }

    @Unique
    public void enchantips$setStale(boolean stale) {
        this.enchantips$stale = stale;
    }

    @Unique
    public void enchantips$updateFullEnchantmentHint(int slot) {
        Level world = Minecraft.getInstance().level;
        if(world == null) { return; }
        if(EnchantmentSeedSearcher.getLatestCandidates().size() != 1) { return; }
        IdMap<Holder<Enchantment>> enchantmentMap = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).asHolderIdMap();
        Holder<Enchantment> hintHolder = enchantmentMap.byId(((EnchantmentMenu)(Object)this).enchantClue[slot]);
        if(hintHolder == null) { return; }
        int seed = EnchantmentSeedSearcher.getLatestCandidates().getFirst();
        ItemStack stack = this.enchantSlots.getItem(0);
        Enchantable component = stack.get(DataComponents.ENCHANTABLE);
        List<EnchantmentInstance> enchantmentInstances = EnchantmentSeedSearcher.getEnchantments(
                new LegacyRandomSource((long)seed + slot),
                stack.getItem(),
                component == null ? 0 : component.value(),
                this.costs[slot]
        );
        List<Component> hint = enchantmentInstances.stream()
                .map((instance) -> EnchantmentLevel.of(instance.enchantment().value(), instance.level()))
                .filter((el) -> el.getEnchantment() != hintHolder.value())
                .map(EnchantmentAppearanceHelper::getName).toList();

        this.enchantips$extraHints[slot] = new ScrollableTooltipSection(hint, ModOption.EXTRA_ENCHANTMENTS_LIMIT.getValue());
    }

    @Unique
    public void enchantips$updatePartialEnchantmentHint(int slot, IdMap<Holder<Enchantment>> enchantmentMap, Holder<Enchantment> hintHolder) {
        Level world = Minecraft.getInstance().level;
        if(world == null) { return; }
        if(hintHolder == null) { return; }
        ItemStack stack = this.enchantSlots.getItem(0);
        List<EnchantmentLevel> levels = new ArrayList<>();
        for (Holder<Enchantment> current : enchantmentMap) {
            if ((!(stack.is(current.value().getSupportedItems()) && current.value().isPrimaryItem(stack)) && !stack.is(Items.BOOK)) ||
                    !Enchantment.areCompatible(hintHolder, current) ||
                    (!current.is(EnchantmentTags.IN_ENCHANTING_TABLE))
            ) { continue; }

            for (int z = current.value().getMinLevel(); z <= current.value().getMaxLevel(); z++) {
                EnchantmentLevel level = EnchantmentLevel.of(current.value(), z);

                if (level.getHighestModifiedLevel() >= this.enchantips$powerBounds[slot].lower() &&
                        level.getLowestModifiedLevel() <= this.enchantips$powerBounds[slot].upper() &&
                        level.getLowestModifiedLevel() <= level.getHighestModifiedLevel()
                ) {
                    levels.add(EnchantmentLevel.of(current.value(), z));
                }
            }
        }
        Collections.sort(levels);
        List<Component> hint = new ArrayList<>();
        for(EnchantmentLevel levelData : levels) {
            MutableComponent text = (MutableComponent) EnchantmentAppearanceHelper.getName(levelData);
            if (ModOption.ENCHANTING_POWER_SWITCH.getValue()) {
                text.append(" ").append(TooltipHelper.buildModifiedLevelForEnchantment(
                        levelData.getLowestModifiedLevel(), levelData.getHighestModifiedLevel()
                ));
            }
            hint.add(text);
        }
        this.enchantips$extraHints[slot] = new ScrollableTooltipSection(hint, ModOption.EXTRA_ENCHANTMENTS_LIMIT.getValue());
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
            EnchantmentBoundsContext.reset();
            EnchantmentSeedContext.reset();
        }
    }

    @Unique
    public void enchantips$updateContext(int slotIndex) {
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
        Holder<Enchantment> hintHolder = enchantmentMap.byId(t.enchantClue[slotIndex]);
        if(hintHolder == null || t.levelClue[slotIndex] < 0 || t.costs[slotIndex] < 0) { return; }

        Enchantment hintEnchantment = hintHolder.value();
        EnchantmentLevel enchLevel = EnchantmentLevel.of(hintEnchantment, t.levelClue[slotIndex]);
        IntBounds bounds = new IntBounds(
                EnchantmentFilterer.getLowerBound(enchLevel, itemStack, t.costs[slotIndex]),
                EnchantmentFilterer.getUpperBound(enchLevel, itemStack, t.costs[slotIndex])
        );
        Enchantable enchComponent = itemStack.get(DataComponents.ENCHANTABLE);
        int enchantability = enchComponent == null ? 0 : enchComponent.value();
        if(ModOption.ENCHANTING_CONTEXT_SWITCH.getValue()) {
            EnchantmentBoundsContext.put(this.costs[slotIndex], enchantability, slotIndex, bounds.lower(), bounds.upper());
            bounds = EnchantmentBoundsContext.getBounds(this.costs[slotIndex], enchantability, slotIndex);
            if(ModOption.ENCHANTMENT_SEED_SWITCH.getValue() && EnchantmentSeedSearcher.shouldSearch()) {
                EnchantmentSeedContext.put(this.costs[slotIndex], itemStack, slotIndex, hintEnchantment, t.levelClue[slotIndex]);
                EnchantmentSeedSearcher.startSearch(t.getEnchantmentSeed());
            }
        }
        this.enchantips$powerBounds[slotIndex] = bounds;
        if(EnchantmentSeedSearcher.getLatestCandidates().size() == 1) {
            this.enchantips$updateFullEnchantmentHint(slotIndex);
        } else {
            this.enchantips$updatePartialEnchantmentHint(slotIndex, enchantmentMap, hintHolder);
        }
    }
}
