package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import java.util.List;
import java.util.Stack;
import net.minecraft.network.chat.Component;

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
                    .text(Component.translatable(option.getFullName() + ".option_tooltip." + i))
                    .text(Component.literal(""));
        }
        return desc.build();
    }

    default Component getOptionTitle(OptionNode<T> optionNode) {
        return Component.translatable(optionNode.getFullName() + ".option_title");
    }

    List<Component> getSaveTooltip(T v);

    ConfigInfoLine<T> getConfigLine(OptionNode<T> optionNode);

    default List<Component> getOptionTooltip(OptionNode<T> option) {
        Stack<Component> lines = new Stack<>();
        for(int i = 0; i < option.getNumTooltipLines(); i++) {
            lines.push(Component.translatable(option.getFullName() + ".option_tooltip." + i));
        }
        return lines;
    }

    Option<T> getYaclOption(OptionNode<T> optionNode);
}
