package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ReadVisitor;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import com.fedpol1.enchantips.config.tree.visitor.WriteVisitor;

import java.util.ArrayList;

public abstract class AbstractNode implements Node {

    private final ArrayList<Node> children = new ArrayList<>();
    protected final String name;

    public AbstractNode(String name) {
        this.name = name;
    }
    public int getNumChildren() {
        return this.children.size();
    }

    public Node getChild(int i) {
        return this.children.get(i);
    }

    public String getName() {
        return this.name;
    }

    public Node addChild(Node c) {
        this.children.add(c);
        return c;
    }

    public abstract Object accept(ScreenVisitor v, Object data);
    public abstract Object accept(ReadVisitor v, Object data);
    public abstract Object accept(WriteVisitor v, Object data);
}
