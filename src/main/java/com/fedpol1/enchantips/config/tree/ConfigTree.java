package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.tree.visitor.ReadVisitor;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import com.fedpol1.enchantips.config.tree.visitor.WriteVisitor;

public class ConfigTree extends AbstractNode {

    public static ConfigTree root = new ConfigTree(EnchantipsClient.MODID + ".config");

    ConfigTree(String name) {
        super(name);
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }

    public Object accept(ReadVisitor v, Object data) {
        return v.visit(this, data);
    }

    public Object accept(WriteVisitor v, Object data) {
        return v.visit(this, data);
    }
}