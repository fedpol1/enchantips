package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import net.minecraft.text.Text;

import java.util.List;

public interface Data<T> {

    T getValue();

    String getStringValue();

    T getDefaultValue();

    void setValue(T v);

    boolean canSet(T v);

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

    List<Text> getTooltip(T v);

    ConfigInfoLine<T> getConfigLine(OptionNode<T> optionNode);

    Option<T> getYaclOption(OptionNode<T> optionNode);
}
