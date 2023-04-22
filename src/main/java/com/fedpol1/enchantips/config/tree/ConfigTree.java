package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

public class ConfigTree extends AbstractNode {

    public static ConfigTree root = new ConfigTree(EnchantipsClient.MODID + ".config");

    ConfigTree(String name) {
        super(name);
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }
}
