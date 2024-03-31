package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.tree.visitor.TreeVisitor;

public class GroupNode extends Node implements OptionParent {

    protected GroupNode(String name, Node parent) {
        super(name, parent);
    }

    public <T> OptionNode<T> addOption(ModOption<T> meta) {
        return new OptionNode<>(meta, this);
    }

    public Object accept(TreeVisitor v, Object data) {
        return v.visit(this, data);
    }
}
