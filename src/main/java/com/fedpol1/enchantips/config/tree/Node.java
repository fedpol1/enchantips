package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

public interface Node {

    int getNumChildren();
    Node getChild(int i);
    Node addChild(AbstractNode c);
    String getName() ;
    String getFullName() ;
    Object accept(ScreenVisitor v, Object data);
}
