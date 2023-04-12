package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.tree.*;

public class WriteVisitor implements Visitor{

    private static Object genericVisit(WriteVisitor self, Node n, Object data) {
        StringBuilder start = new StringBuilder();
        start.append(" ".repeat((Integer) data)).append(n.getName()).append(": {\n");
        for(int i = 0; i < n.getNumChildren(); i++) {
            start.append(n.getChild(i).accept(self, (Integer) data + 2));
        }
        return start.append(" ".repeat((Integer) data)).append("}\n");
    }

    public Object visit(ConfigTree n, Object data) {
        return WriteVisitor.genericVisit(this, n, data);
    }

    public Object visit(CategoryNode n, Object data) {
        return WriteVisitor.genericVisit(this, n, data);
    }

    public Object visit(GroupNode n, Object data) {
        return WriteVisitor.genericVisit(this, n, data);
    }

    public Object visit(OptionNode n, Object data) {
        return new StringBuilder(" ".repeat((Integer) data)).append(n.getName()).append(": ").append(n.getData().getStringValue()).append("\n");
    }
}
