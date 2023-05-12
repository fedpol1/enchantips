package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.*;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.Objects;

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
        Enchantment ench = n.getEnchantment();

        OptionGroup.Builder groupBuilder = OptionGroup.createBuilder()
                .name(Text.translatable(ench == null ? n.getFullName() : ench.getTranslationKey()))
                .tooltip(Text.translatable(ench == null ? "" : Objects.requireNonNull(Registries.ENCHANTMENT.getId(ench)).toString()))
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
