package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;

import java.util.Map;
import java.util.Set;

public interface Node {

    Node getChild(String s);
    Set<Map.Entry<String, Node>> getChildren();
    Node addChild(AbstractNode c);
    String getName() ;
    String getFullName() ;
    Object accept(ScreenVisitor v, Object data);
}
