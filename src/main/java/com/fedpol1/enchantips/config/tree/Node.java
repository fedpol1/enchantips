package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.TreeVisitor;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public abstract class Node {

    private final String name;
    protected String fullName;
    protected final LinkedHashMap<String, Node> children = new LinkedHashMap<>();
    protected Node parent;

    protected Node(String name, Node parent) {
        this.name = name;
        this.parent = parent;
        this.fullName = name;
        if(this.parent != null) {
            this.fullName = this.parent.getFullName() + "." + this.name;
            this.parent.children.put(this.getName(), this);
        }
    }

    public Node getChild(String s) {
        return this.children.get(s);
    }

    public Set<Map.Entry<String, Node>> getChildren() {
        return this.children.entrySet();
    }

    public void removeChild(String s) {
        this.children.remove(s);
    }

    public String getName() {
        return this.name;
    }

    public String getFullName() {
        return this.fullName;
    }

    public abstract Object accept(TreeVisitor v, Object data);
}
