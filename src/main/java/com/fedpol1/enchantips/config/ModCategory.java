package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.ConfigTree;

public enum ModCategory {

    TOOLTIP_TOGGLES(new CategoryNode("toggles_tooltips")),
    TOOLTIP_COLORS(new CategoryNode("colors_tooltips")),
    HIGHLIGHTS(new CategoryNode("highlights")),
    MISCELLANEOUS(new CategoryNode("miscellaneous")),
    INDIVIDUAL_ENCHANTMENTS(new CategoryNode("individual_enchantments"));

    private final CategoryNode node;

    ModCategory(CategoryNode cat) {
        this.node = cat;
        ConfigTree.root.addChild(cat);
        ModConfigData.categories.put(cat.getName(), cat);
    }

    public CategoryNode getNode() {
        return this.node;
    }

    public static void init() {}
}
