package com.fedpol1.enchantips.accessor;

import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import com.fedpol1.enchantips.util.IntPair;

public interface EnchantmentMenuAccess {

    IntPair enchantips$getPowerBounds(int i);
    ScrollableTooltipSection enchantips$getEnchantmentHint(int i);
    void enchantips$updateContext(int dataSlotIndex);
}
