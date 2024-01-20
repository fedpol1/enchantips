package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import net.minecraft.text.Text;

public interface Data<T> {

    T getValue();

    String getStringValue();

    T getDefaultValue();

    void setValue(T c);

    void readStringValue(String c);

    default OptionDescription getOptionDescription(OptionNode<T> option) {
        OptionDescription.Builder desc = OptionDescription.createBuilder();
        for(int i = 0; i < option.getNumTooltipLines(); i++) {
            desc = desc
                    .text(Text.translatable(option.getFullName() + ".option_tooltip." + i))
                    .text(Text.literal(""));
        }
        return desc.build();
    }

    default Text getOptionTitle(OptionNode<T> optionNode) {
        return Text.translatable(optionNode.getFullName() + ".option_title");
    }

    Option<T> getYaclOption(OptionNode<T> optionNode);
}
