package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;
import net.minecraft.enchantment.Enchantment;

import java.util.HashMap;

public class ModConfigData {

    public static final String MIN_COLOR_KEY = "min_color";
    public static final String MAX_COLOR_KEY = "max_color";
    public static final String OVERMAX_COLOR_KEY = "overmax_color";
    public static final String ORDER_KEY = "order";
    public static final String HIGHLIGHT_KEY = "highlight";

    protected static HashMap<Enchantment, GroupNode> enchantmentData = new HashMap<>();

    public static GroupNode get(Enchantment e) {
        return ModConfigData.enchantmentData.get(e);
    }

    public static boolean isEnchantmentHighlighted(Enchantment e) {
        return (boolean) ((OptionNode<?>) ModConfigData.enchantmentData.get(e).getChild(ModConfigData.HIGHLIGHT_KEY)).getValue();
    }

    public static int getEnchantmentOrder(Enchantment e) {
        return (int) ((OptionNode<?>) ModConfigData.enchantmentData.get(e).getChild(ModConfigData.ORDER_KEY)).getValue();
    }
}
