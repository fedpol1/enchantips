package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.tree.*;
import net.minecraft.enchantment.Enchantment;

public enum ModCategory {

    TOOLTIPS(ConfigTree.root.addCategory("tooltips")),
    TOOLTIP_TOGGLES(TOOLTIPS.addGroup("toggles")),
    TOOLTIP_COLORS(TOOLTIPS.addGroup("colors")),
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

    public GroupNode addGroup(String name) {
        if(!(this.node instanceof GroupParent)) {
            throw new UnsupportedOperationException("Groups can only be added to categories.");
        }
        return ((CategoryNode)this.node).addGroup(name);
    }

    public EnchantmentGroupNode addGroup(Enchantment ench) {
        if(!(this.node instanceof GroupParent)) {
            throw new UnsupportedOperationException(this.node.getClass().getName() + " does not support groups.");
        }
        return ((CategoryNode)this.node).addGroup(ench);
    }

    public <T> ModOption<T> addOption(Data<T> meta, String key, int tooltipLines) {
        if(!(this.node instanceof OptionParent)) {
            throw new UnsupportedOperationException(this.node.getClass().getName() + " does not support options.");
        }
        ModOption<T> option = new ModOption<>(meta, key, tooltipLines);
        ((OptionParent)this.node).addOption(option);
        return option;
    }

    public static void init() {}
}
