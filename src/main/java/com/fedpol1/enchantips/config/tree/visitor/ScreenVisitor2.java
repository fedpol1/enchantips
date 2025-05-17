package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.gui.widgets.info_line.*;
import net.minecraft.text.Text;

import java.util.Map;

public class ScreenVisitor2 implements TreeVisitor {

    public Object visit(ConfigTree n, Object data) {
        ScrollableInfoLineContainer lines = new ScrollableInfoLineContainer(0x404040, 6);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, lines);
        }
        return lines;
    }

    public Object visit(CategoryNode n, Object data) {
        CollapsibleInfoLine collapsible = new CollapsibleInfoLine(Text.translatable(n.getFullName()));
        ((InfoMultiLine) data).addLine(collapsible);

        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, collapsible);
        }
        return collapsible;
    }

    public Object visit(GroupNode n, Object data) {
        CollapsibleInfoLine collapsible = new CollapsibleInfoLine(Text.translatable(n.getFullName()));
        ((InfoMultiLine) data).addLine(collapsible);

        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, collapsible);
        }
        return collapsible;
    }

    public Object visit(EnchantmentGroupNode n, Object data) {
        CollapsibleInfoLine collapsible = new CollapsibleInfoLine(n.getDescription());
        ((InfoMultiLine) data).addLine(collapsible);

        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, collapsible);
        }
        return collapsible;
    }

    public Object visit(OptionNode<?> n, Object data) {
        ((InfoMultiLine) data).addLine(n.getConfigLine());
        return n.getConfigLine();
    }
}
