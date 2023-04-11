package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ReadVisitor;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import com.fedpol1.enchantips.config.tree.visitor.WriteVisitor;

public class GroupNode extends AbstractNode{

    public GroupNode(String name) {
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
