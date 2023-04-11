package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ReadVisitor;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import com.fedpol1.enchantips.config.tree.visitor.WriteVisitor;

public interface Node {

    int getNumChildren();

    Node getChild(int i);

    String getName() ;

    Node addChild(Node c);

    Object accept(ScreenVisitor v, Object data);
    Object accept(ReadVisitor v, Object data);
    Object accept(WriteVisitor v, Object data);
}
