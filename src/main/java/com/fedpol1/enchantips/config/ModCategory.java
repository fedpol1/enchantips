package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.tree.*;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKey;

public enum ModCategory {

    TOOLTIPS(ConfigTree.root.addCategory("tooltips")),
    TOOLTIPS_SWATCHES(TOOLTIPS.addGroup("swatches")),
    TOOLTIPS_GLINT_OVERRIDE(TOOLTIPS.addGroup("glint_override")),
    TOOLTIPS_REPAIR_COST(TOOLTIPS.addGroup("repair_cost")),
    TOOLTIPS_ENCHANTABILITY(TOOLTIPS.addGroup("enchantability")),
    TOOLTIPS_ANVIL_COST(TOOLTIPS.addGroup("anvil_cost")),
    TOOLTIPS_MAXIMUM_ENCHANTMENT_LEVEL(TOOLTIPS.addGroup("maximum_enchantment_level")),
    TOOLTIPS_ENCHANTMENT_TARGETS(TOOLTIPS.addGroup("enchantment_targets")),
    TOOLTIPS_MODIFIED_ENCHANTING_POWER(TOOLTIPS.addGroup("modified_enchanting_power")),
    TOOLTIPS_EXTRA_ENCHANTMENTS(TOOLTIPS.addGroup("extra_enchantments")),
    TOOLTIPS_MISCELLANEOUS(TOOLTIPS.addGroup("miscellaneous")),
    USER_INTERFACE(ConfigTree.root.addCategory("user_interface")),
    HIGHLIGHTS(USER_INTERFACE.addGroup("highlights")),
    WIDGETS(USER_INTERFACE.addGroup("widgets")),
    HEADS_UP_DISPLAY(USER_INTERFACE.addGroup("heads_up_display")),
    MISCELLANEOUS(USER_INTERFACE.addGroup("miscellaneous")),
    BULK_UPDATE(USER_INTERFACE.addGroup("bulk_update")),
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

    public EnchantmentGroupNode addGroup(RegistryKey<Enchantment> ench) {
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
