package com.fedpol1.enchantips.event;

import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import net.minecraft.client.gui.screen.Screen;

public class EnchantmentScreenEvents {

    public static void onScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        ScrollableTooltipSection.getActiveSection().scroll((int) -verticalAmount);
    }
}
