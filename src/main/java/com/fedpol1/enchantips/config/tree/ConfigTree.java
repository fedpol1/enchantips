package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

public class ConfigTree extends Node {

    public static ConfigTree root = new ConfigTree(EnchantipsClient.MODID + ".config");

    private ConfigTree(String name) {
        super(name, null);
    }

    public CategoryNode addCategory(String name) {
        return new CategoryNode(name, this);
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }
}
