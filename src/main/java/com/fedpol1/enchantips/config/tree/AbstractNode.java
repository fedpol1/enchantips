package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

import java.util.ArrayList;

public abstract class AbstractNode implements Node {

    protected final String name;
    protected String fullName;
    protected final ArrayList<Node> children = new ArrayList<>();
    protected Node parent;

    public AbstractNode(String name) {
        this.name = name;
        this.fullName = name;
        this.parent = null;
    }

    public int getNumChildren() {
        return this.children.size();
    }

    public Node getChild(int i) {
        return this.children.get(i);
    }

    public Node addChild(AbstractNode c) {
        c.fullName = this.getFullName() + "." + c.getName();
        c.parent = this;
        this.children.add(c);
        return c;
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public abstract Object accept(ScreenVisitor v, Object data);
}
