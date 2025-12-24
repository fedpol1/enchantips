package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import net.minecraft.network.chat.Component;

public class EnchantmentConfigInfoLine extends CollapsibleInfoLine {

    private EnchantmentGroupNode node;

    public EnchantmentConfigInfoLine(EnchantmentGroupNode node) {
        super(Component.literal(node.getIdentifier()));
        this.node = node;
    }

    public EnchantmentGroupNode getNode() {
        return this.node;
    }
}
