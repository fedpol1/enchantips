package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.*;
import dev.isxander.yacl3.api.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;

public class ScreenVisitor implements TreeVisitor {

    public Object visit(ConfigTree n, Object data) {
        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
                .title(Text.translatable(n.getFullName()))
                .save(ModConfig::writeConfig);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            yaclBuilder = (YetAnotherConfigLib.Builder) current.getValue().accept(this, yaclBuilder);
        }
        return yaclBuilder.build();
    }

    public Object visit(CategoryNode n, Object data) {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
                .name(Text.translatable(n.getFullName()));
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
                .name(Text.translatable(n.getFullName()))
                .description(OptionDescription.createBuilder()
                        .text(Text.literal(""))
                        .build())
                .collapsed(true);
        for(Map.Entry<String, Node> current : n.getChildren()) {
            groupBuilder = groupBuilder.option((Option<?>) current.getValue().accept(this, groupBuilder));
        }
        return ((ConfigCategory.Builder)data).group(groupBuilder.build());
    }

    public Object visit(EnchantmentGroupNode n, Object data) {
        OptionDescription.Builder description = OptionDescription.createBuilder()
                .text(Text.translatable(n.getIdentifier()))
                .text(Text.literal(""))
                .text(Text.translatable("enchantips.config.individual_enchantments.option_tooltip.0"));
        World world = MinecraftClient.getInstance().world;

        boolean tagsKnown = world != null;
        if(tagsKnown) {
            Optional<RegistryEntry.Reference<Enchantment>> enchantmentReference = world
                    .getRegistryManager()
                    .getOrThrow(RegistryKeys.ENCHANTMENT)
                    .getEntry(Identifier.of(n.getIdentifier()));
            if(enchantmentReference.isPresent()) {
                for (TagKey<Enchantment> tag : enchantmentReference.get().streamTags().toList()) {
                    description.text(Text.literal("#").append(Text.literal(tag.id().toString())));
                }
            }
            else { tagsKnown = false; }
        }
        if(!tagsKnown) {
            description.text(Text
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
