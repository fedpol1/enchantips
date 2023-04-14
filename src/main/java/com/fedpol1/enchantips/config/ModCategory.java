package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.ConfigTree;
import com.fedpol1.enchantips.config.tree.Node;

public enum ModCategory {

    TOOLTIP_TOGGLES(new CategoryNode("toggles_tooltips"), ConfigTree.root),
    TOOLTIP_COLORS(new CategoryNode("colors_tooltips"), ConfigTree.root),
    HIGHLIGHTS(new CategoryNode("highlights"), ConfigTree.root),
    MISCELLANEOUS(new CategoryNode("miscellaneous"), ConfigTree.root),
    INDIVIDUAL_ENCHANTMENTS(new CategoryNode("individual_enchantments"), ConfigTree.root);

    private final CategoryNode node;

    ModCategory(CategoryNode cat, Node parent) {
        this.node = cat;
        parent.addChild(cat);
        ModConfigData.categoryData.put(cat.getName(), cat);
    }

    public CategoryNode getNode() {
        return this.node;
    }

    public static void init() {}
}
