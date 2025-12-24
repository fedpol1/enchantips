package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.gui.widgets.info_line.*;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class ScreenVisitor2 implements TreeVisitor {

    public Object visit(ConfigTree n, Object data) {
        ScrollableInfoLineContainer lines = new ScrollableInfoLineContainer(0xff404040, 6);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, lines);
        }
        return lines;
    }

    public Object visit(CategoryNode n, Object data) {
        CollapsibleInfoLine collapsible = new CollapsibleInfoLine(Component.translatable(n.getFullName()));
        ((InfoMultiLine) data).addLine(collapsible);

        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, collapsible);
        }
        return collapsible;
    }

    public Object visit(GroupNode n, Object data) {
        CollapsibleInfoLine collapsible = new CollapsibleInfoLine(Component.translatable(n.getFullName()));
        ((InfoMultiLine) data).addLine(collapsible);

        for(Map.Entry<String, Node> current : n.getChildren()) {
            current.getValue().accept(this, collapsible);
        }
        return collapsible;
    }

    public Object visit(EnchantmentGroupNode n, Object data) {
        EnchantmentConfigInfoLine collapsible = new EnchantmentConfigInfoLine(n);
        ((InfoMultiLine) data).addLine(collapsible);

        Level world = Minecraft.getInstance().level;
        if(world != null) {
            Optional<Holder.Reference<Enchantment>> enchantmentReference = world
                    .registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .get(Identifier.parse(n.getIdentifier()));
            if(enchantmentReference.isPresent()) {
                collapsible.addLine(n.getDescription());
                CollapsibleInfoLine tagLine = new CollapsibleInfoLine(
                        Component.translatable("enchantips.config.individual_enchantments.option_tooltip.0")
                );
                collapsible.addLine(tagLine);
                for (TagKey<Enchantment> tag : enchantmentReference.get().tags().toList()) {
                    tagLine.addLine(Component.literal("#").append(Component.literal(tag.location().toString())));
                }
            } else {
                collapsible.addLine(
                        new DeleteInfoLine(Component.translatable("enchantips.config.individual_enchantments.delete.option_title"))
                );
            }
        } else {
            collapsible.addLine(
                    new DeleteInfoLine(Component.translatable("enchantips.config.individual_enchantments.delete.option_title"))
            );
        }

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
