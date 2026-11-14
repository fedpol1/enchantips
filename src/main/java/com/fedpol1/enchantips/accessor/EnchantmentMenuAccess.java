package com.fedpol1.enchantips.accessor;

import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.IntBounds;
import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.level.Level;

public interface EnchantmentMenuAccess {

    IntBounds enchantips$getPowerBounds(int i);
    ScrollableTooltipSection enchantips$getEnchantmentHint(int i);
    ScrollableTooltipSection enchantips$getSeededHint(int i);
    void enchantips$updateContext(int dataSlotIndex);
    void enchantips$updateSeededHint(int slot, int seed);
    ScrollableTooltipSection enchantips$getHint(int i);
    boolean enchantips$isSeeded();
    void enchantips$setSeeded(boolean seeded);
    boolean enchantips$isStale();
    void enchantips$setStale(boolean stale);
    ContainerLevelAccess enchantips$getLevelAccess();
    int enchantips$getBookshelfCount(Level world, BlockPos blockPos);
}
