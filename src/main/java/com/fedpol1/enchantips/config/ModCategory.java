package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.ConfigTree;

public enum ModCategory {

    TOOLTIP_TOGGLES(ConfigTree.root.addCategory("toggles_tooltips")),
    TOOLTIP_COLORS(ConfigTree.root.addCategory("colors_tooltips")),
    HIGHLIGHTS(ConfigTree.root.addCategory("highlights")),
    MISCELLANEOUS(ConfigTree.root.addCategory("miscellaneous")),
    INDIVIDUAL_ENCHANTMENTS(ConfigTree.root.addCategory("individual_enchantments"));

    private final CategoryNode node;

    ModCategory(CategoryNode cat) {
        this.node = cat;
    }

    public CategoryNode getNode() {
        return this.node;
    }

    public static void init() {}
}
