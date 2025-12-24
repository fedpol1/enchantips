package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.tree.visitor.TreeVisitor;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class CategoryNode extends Node implements GroupParent, OptionParent {

    protected CategoryNode(String name, Node parent) {
        super(name, parent);
    }

    public GroupNode addGroup(String name) {
        return new GroupNode(name, this);
    }

    public EnchantmentGroupNode addEnchantmentGroup(ResourceKey<Enchantment> ench) {
        return new EnchantmentGroupNode(ench, this);
    }

    public EnchantmentGroupNode addEnchantmentGroup(String ench) {
        return new EnchantmentGroupNode(ench, this);
    }

    public <T> OptionNode<T> addOption(ModOption<T> meta) {
        return new OptionNode<>(meta, this);
    }

    public Object accept(TreeVisitor v, Object data) {
        return v.visit(this, data);
    }
}
