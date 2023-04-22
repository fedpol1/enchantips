package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.tree.*;

public interface Visitor {

    Object visit(ConfigTree n, Object data);
    Object visit(CategoryNode n, Object data);
    Object visit(GroupNode n, Object data);
    Object visit(OptionNode<?> n, Object data);
}
