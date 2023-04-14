package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

public class CategoryNode extends AbstractNode{

    public CategoryNode(String name) {
        super(name);
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }
}
