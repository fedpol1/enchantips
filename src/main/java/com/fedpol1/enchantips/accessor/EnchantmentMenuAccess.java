package com.fedpol1.enchantips.accessor;

import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.IntBounds;

public interface EnchantmentMenuAccess {

    IntBounds enchantips$getPowerBounds(int i);
    ScrollableTooltipSection enchantips$getEnchantmentHint(int i);
    void enchantips$updateContext(int dataSlotIndex);
    void enchantips$updateFullEnchantmentHint(int slot);
    boolean enchantips$isStale();
    void enchantips$setStale(boolean stale);
}
