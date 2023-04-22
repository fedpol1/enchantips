package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public abstract class AbstractNode implements Node {

    protected final String name;
    protected String fullName;
    //protected final ArrayList<Node> children = new ArrayList<>();
    protected final HashMap<String, Node> children = new HashMap<>();
    protected Node parent;

    public AbstractNode(String name) {
        this.name = name;
        this.fullName = name;
        this.parent = null;
    }

    public Node getChild(String s) {
        return this.children.get(s);
    }

    public Set<Map.Entry<String, Node>> getChildren() {
        return this.children.entrySet();
    }

    public Node addChild(AbstractNode c) {
        c.fullName = this.getFullName() + "." + c.getName();
        c.parent = this;
        this.children.put(c.getName(), c);
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
