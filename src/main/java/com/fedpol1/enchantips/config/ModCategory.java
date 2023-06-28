package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.tree.*;

public enum ModCategory {

    TOOLTIPS(ConfigTree.root.addCategory("tooltips")),
    TOOLTIP_TOGGLES(TOOLTIPS.addChild(new GroupNode("toggles"))),
    TOOLTIP_COLORS(TOOLTIPS.addChild(new GroupNode("colors"))),
    HIGHLIGHTS(ConfigTree.root.addCategory("highlights")),
    MISCELLANEOUS(ConfigTree.root.addCategory("miscellaneous")),
    INDIVIDUAL_ENCHANTMENTS(ConfigTree.root.addCategory("individual_enchantments"));

    private final Node node;

    ModCategory(Node cat) {
        this.node = cat;
    }

    public Node getNode() {
        return this.node;
    }

    public Node addChild(AbstractNode c) {
        return this.node.addChild(c);
    }

    public <T> ModOption<T> addChild(ModOption<T> c) {
        OptionNode<T> o = new OptionNode<>(c);
        this.node.addChild(o);
        return c;
    }

    public static void init() {}
}
