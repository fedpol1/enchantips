package com.fedpol1.enchantips.config;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.tree.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public enum ModCategory {

    PER_ENCHANTMENT(ConfigTree.root.addCategory("per_enchantment")),
    PER_ENCHANTMENT_SWATCHES(PER_ENCHANTMENT.addGroup("swatches")),
    PER_ENCHANTMENT_ANVIL_COST(PER_ENCHANTMENT.addGroup("anvil_cost")),
    PER_ENCHANTMENT_WEIGHT(PER_ENCHANTMENT.addGroup("weight")),
    PER_ENCHANTMENT_MAXIMUM_LEVEL(PER_ENCHANTMENT.addGroup("maximum_level")),
    PER_ENCHANTMENT_TARGETS(PER_ENCHANTMENT.addGroup("targets")),
    PER_ENCHANTMENT_TAGS(PER_ENCHANTMENT.addGroup("tags")),
    PER_ITEMSTACK(ConfigTree.root.addCategory("per_itemstack")),
    PER_ITEMSTACK_GLINT_OVERRIDE(PER_ITEMSTACK.addGroup("glint_override")),
    PER_ITEMSTACK_REPAIR_COST(PER_ITEMSTACK.addGroup("repair_cost")),
    PER_ITEMSTACK_ENCHANTABILITY(PER_ITEMSTACK.addGroup("enchantability")),
    PER_ITEMSTACK_HIGHLIGHTS(PER_ITEMSTACK.addGroup("highlights")),
    ENCHANTING_HELPERS(ConfigTree.root.addCategory("enchanting_helpers")),
    MISCELLANEOUS(ConfigTree.root.addCategory("miscellaneous")),
    MISCELLANEOUS_WIDGETS(MISCELLANEOUS.addGroup("widgets")),
    MISCELLANEOUS_UNBREAKABLE(MISCELLANEOUS.addGroup("unbreakable")),
    MISCELLANEOUS_BULK_UPDATE(MISCELLANEOUS.addGroup("bulk_update")),
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

    public EnchantmentGroupNode addEnchantmentGroup(ResourceKey<Enchantment> ench) {
        if(!(this.node instanceof GroupParent)) {
            throw new UnsupportedOperationException(this.node.getClass().getName() + " does not support groups.");
        }
        return ((CategoryNode)this.node).addEnchantmentGroup(ench);
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
