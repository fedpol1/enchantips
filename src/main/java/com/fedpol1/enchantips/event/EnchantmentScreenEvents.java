package com.fedpol1.enchantips.event;

import com.fedpol1.enchantips.gui.ScrollableTooltipSection;
import net.minecraft.client.gui.screen.Screen;

public class EnchantmentScreenEvents {

    public static boolean onScroll(Screen screen, double mouseX, double mouseY, double horizontalAmount, double verticalAmount, boolean consumed) {
        if(!consumed) {
            ScrollableTooltipSection.getActiveSection().scroll((int) -verticalAmount);
        }
        return true;
    }
}
