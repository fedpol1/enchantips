package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.ConfigTree;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.OptionNode;

public class WriteVisitor implements Visitor{

    public Object visit(ConfigTree n, Object data) {
        StringBuilder start = new StringBuilder("{\n");
        for(int i = 0; i < n.getNumChildren(); i++) {
            start.append(n.getChild(i).accept(this, (Integer) data + 2));
        }
        return start.append("}\n");
    }

    public Object visit(CategoryNode n, Object data) {
        StringBuilder start = new StringBuilder(n.getName()).append(": {\n");
        for(int i = 0; i < n.getNumChildren(); i++) {
            start.append(n.getChild(i).accept(this, (Integer) data + 2));
        }
        return start.append("}\n");
    }

    public Object visit(GroupNode n, Object data) {
        StringBuilder start = new StringBuilder(n.getName()).append(": {\n");
        for(int i = 0; i < n.getNumChildren(); i++) {
            start.append(n.getChild(i).accept(this, (Integer) data + 2));
        }
        return start.append("}\n");
    }

    public Object visit(OptionNode n, Object data) {
        return new StringBuilder(n.getName()).append(": ").append(n.getEntry().getData().getStringValue()).append("\n");
    }
}
