package com.fedpol1.enchantips.config.tree.visitor;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.*;
import dev.isxander.yacl.api.ConfigCategory;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.api.OptionGroup;
import dev.isxander.yacl.api.YetAnotherConfigLib;
import net.minecraft.text.Text;

public class ScreenVisitor implements Visitor {

    public Object visit(ConfigTree n, Object data) {
        YetAnotherConfigLib.Builder yaclBuilder = YetAnotherConfigLib.createBuilder()
                .title(Text.translatable(n.getName()))
                .save(ModConfig::writeConfig);
        for(int i = 0; i < n.getNumChildren(); i++) {
            yaclBuilder = (YetAnotherConfigLib.Builder) n.getChild(i).accept(this, yaclBuilder);
        }
        return yaclBuilder.build();
    }

    public Object visit(CategoryNode n, Object data) {
        ConfigCategory.Builder categoryBuilder = ConfigCategory.createBuilder()
                .name(Text.translatable(EnchantipsClient.MODID + ".config.title.category." + n.getName()));
        Object child;
        for(int i = 0; i < n.getNumChildren(); i++) {
            child = n.getChild(i).accept(this, categoryBuilder);
            if(child instanceof Option<?> childOpt) { categoryBuilder = categoryBuilder.option(childOpt); }
            if(child instanceof OptionGroup childOptGroup) { categoryBuilder = categoryBuilder.group(childOptGroup); }
        }
        return ((YetAnotherConfigLib.Builder)data).category(categoryBuilder.build());
    }

    public Object visit(GroupNode n, Object data) {
        OptionGroup.Builder groupBuilder = OptionGroup.createBuilder()
                .name(Text.translatable(n.getName()))
                .collapsed(true);
        for(int i = 0; i < n.getNumChildren(); i++) {
            groupBuilder = groupBuilder.option((Option<?>) n.getChild(i).accept(this, groupBuilder));
        }
        return ((ConfigCategory.Builder)data).group(groupBuilder.build());
    }

    public Object visit(OptionNode n, Object data) {
        return n.getYaclOption();
    }
}
