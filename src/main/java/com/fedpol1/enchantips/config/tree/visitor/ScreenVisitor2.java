package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.tree.*;
import com.fedpol1.enchantips.gui.widgets.info_line.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class ScreenVisitor2 implements TreeVisitor {

    public Object visit(ConfigTree n, Object data) {
        ScrollableInfoLineContainer lines = new ScrollableInfoLineContainer(0xff404040, 6);
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
        EnchantmentConfigInfoLine collapsible = new EnchantmentConfigInfoLine(n);
        ((InfoMultiLine) data).addLine(collapsible);

        World world = MinecraftClient.getInstance().world;
        if(world != null) {
            Optional<RegistryEntry.Reference<Enchantment>> enchantmentReference = world
                    .getRegistryManager()
                    .getOrThrow(RegistryKeys.ENCHANTMENT)
                    .getEntry(Identifier.of(n.getIdentifier()));
            if(enchantmentReference.isPresent()) {
                collapsible.addLine(n.getDescription());
                CollapsibleInfoLine tagLine = new CollapsibleInfoLine(
                        Text.translatable("enchantips.config.individual_enchantments.option_tooltip.0")
                );
                collapsible.addLine(tagLine);
                for (TagKey<Enchantment> tag : enchantmentReference.get().streamTags().toList()) {
                    tagLine.addLine(Text.literal("#").append(Text.literal(tag.id().toString())));
                }
            } else {
                collapsible.addLine(
                        new DeleteInfoLine(Text.translatable("enchantips.config.individual_enchantments.delete.option_title"))
                );
            }
        } else {
            collapsible.addLine(
                    new DeleteInfoLine(Text.translatable("enchantips.config.individual_enchantments.delete.option_title"))
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
