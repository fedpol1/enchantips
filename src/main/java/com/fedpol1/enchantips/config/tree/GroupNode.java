package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;

import java.util.Objects;

public class GroupNode extends AbstractNode{

    private final Enchantment enchantment;

    public GroupNode(String name) {
        super(name);
        this.enchantment = null;
    }

    public GroupNode(Enchantment e) {
        super(Objects.requireNonNull(Registries.ENCHANTMENT.getId(e)).toString());
        this.enchantment = e;
    }

    public Enchantment getEnchantment() {
        return this.enchantment;
    }

    public Node addChild(AbstractNode c) {
        AbstractNode child = (AbstractNode) super.addChild(c);
        if(this.enchantment != null) {
            child.fullName = this.parent.getFullName() + "." + child.getName();
        }
        return child;
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }
}
