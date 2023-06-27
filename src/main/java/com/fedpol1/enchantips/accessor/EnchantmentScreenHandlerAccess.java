package com.fedpol1.enchantips.accessor;

import com.fedpol1.enchantips.gui.ScrollableTooltipSection;

public interface EnchantmentScreenHandlerAccess {

    ScrollableTooltipSection[] enchantipsSections = new ScrollableTooltipSection[3];
    ScrollableTooltipSection enchantipsGetSection(int i);
}
