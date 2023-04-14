package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;
import net.minecraft.enchantment.Enchantment;

import java.util.HashMap;

public class ModConfigData {

    protected static HashMap<Enchantment, GroupNode> enchantmentData = new HashMap<>();

    // used for reading config file
    protected static HashMap<String, OptionNode<?>> optionData = new HashMap<>();
    protected static HashMap<String, CategoryNode> categoryData = new HashMap<>();

    public static GroupNode get(Enchantment e) {
        return ModConfigData.enchantmentData.get(e);
    }

    public static boolean isEnchantmentHighlighted(Enchantment e) {
        return (boolean) ((OptionNode<?>) ModConfigData.enchantmentData.get(e).getChild(3)).getValue();
    }

    public static int getEnchantmentOrder(Enchantment e) {
        return (int) ((OptionNode<?>) ModConfigData.enchantmentData.get(e).getChild(2)).getValue();
    }

    public static OptionNode<?> getOption(String s) {
        return ModConfigData.optionData.get(s);
    }

    public static CategoryNode getCategory(String s) {
        return ModConfigData.categoryData.get(s);
    }
}
