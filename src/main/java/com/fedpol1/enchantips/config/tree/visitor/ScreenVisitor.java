package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.*;
import dev.isxander.yacl3.api.*;
import java.util.Map;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.Level;

public class ScreenVisitor implements TreeVisitor {

    public Object visit(ConfigTree n, Object data) {
        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
                .title(Component.translatable(n.getFullName()))
                .save(ModConfig::writeConfig);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            yaclBuilder = (YetAnotherConfigLib.Builder) current.getValue().accept(this, yaclBuilder);
        }
        return yaclBuilder.build();
    }

    public Object visit(CategoryNode n, Object data) {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
                .name(Component.translatable(n.getFullName()));
        Object child;
        for(Map.Entry<String, Node> current : n.getChildren()) {
            child = current.getValue().accept(this, categoryBuilder);
            if(child instanceof Option<?> childOpt) { categoryBuilder = categoryBuilder.option(childOpt); }
            if(child instanceof OptionGroup childOptGroup) { categoryBuilder = categoryBuilder.group(childOptGroup); }
        }
        return ((YetAnotherConfigLib.Builder)data).category(categoryBuilder.build());
    }

    public Object visit(GroupNode n, Object data) {
        OptionGroup.Builder groupBuilder = OptionGroup.createBuilder()
                .name(Component.translatable(n.getFullName()))
                .description(OptionDescription.createBuilder()
                        .text(Component.literal(""))
                        .build())
                .collapsed(true);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            groupBuilder = groupBuilder.option((Option<?>) current.getValue().accept(this, groupBuilder));
        }
        return ((ConfigCategory.Builder)data).group(groupBuilder.build());
    }

    public Object visit(EnchantmentGroupNode n, Object data) {
        OptionDescription.Builder description = OptionDescription.createBuilder()
                .text(Component.translatable(n.getIdentifier()))
                .text(Component.literal(""))
                .text(Component.translatable("enchantips.config.individual_enchantments.option_tooltip.0"));
        Level world = Minecraft.getInstance().level;

        boolean tagsKnown = world != null;
        if(tagsKnown) {
            Optional<Holder.Reference<Enchantment>> enchantmentReference = world
                    .registryAccess()
                    .lookupOrThrow(Registries.ENCHANTMENT)
                    .get(Identifier.parse(n.getIdentifier()));
            if(enchantmentReference.isPresent()) {
                for (TagKey<Enchantment> tag : enchantmentReference.get().tags().toList()) {
                    description.text(Component.literal("#").append(Component.literal(tag.location().toString())));
                }
            }
            else { tagsKnown = false; }
        }
        if(!tagsKnown) {
            description.text(Component
                    .translatable("enchantips.config.individual_enchantments.option_tooltip.1")
                    .setStyle(Style.EMPTY.withItalic(true))
            );
        }

        OptionGroup.Builder groupBuilder = OptionGroup.createBuilder()
                .name(n.getDescription())
                .description(description.build())
                .collapsed(true);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            groupBuilder = groupBuilder.option((Option<?>) current.getValue().accept(this, groupBuilder));
        }
        return ((ConfigCategory.Builder)data).group(groupBuilder.build());
    }

    public Object visit(OptionNode<?> n, Object data) {
        return n.getYaclOption();
    }
}
