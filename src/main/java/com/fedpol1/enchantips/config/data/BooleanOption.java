package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.BooleanConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.text.Text;

import java.util.List;

public class BooleanOption implements Data<Boolean> {

    private boolean value;
    private final boolean defaultValue;

    public BooleanOption(boolean defaultValue) {
        this.value = defaultValue;
        this.defaultValue = defaultValue;
    }

    public Boolean getValue() {
        return this.value;
    }

    public String getStringValue() {
        return Boolean.toString(this.getValue());
    }

    public Boolean getDefaultValue() {
        return this.defaultValue;
    }

    public void setValue(Boolean v) {
        this.value = v;
    }

    public boolean canSet(Boolean v) {
        return v != null;
    }

    public void readStringValue(String s) {
        this.value = Boolean.parseBoolean(s);
    }

    public List<Text> getTooltip(Boolean v) {
        if(this.canSet(v)) {
            return null;
        }
        return List.of(Text.translatable("enchantips.gui.setter.boolean.error.invalid"));
    }

    public ConfigInfoLine<Boolean> getConfigLine(OptionNode<Boolean> optionNode) {
        return new BooleanConfigInfoLine(
                this.getOptionTitle(optionNode),
                this,
                this.value
        );
    }

    public Option<Boolean> getYaclOption(OptionNode<Boolean> optionNode) {
        return Option.<Boolean>createBuilder()
                .binding(this.getDefaultValue(), this::getValue, this::setValue)
                .controller(TickBoxControllerBuilder::create)
                .name(this.getOptionTitle(optionNode))
                .description(this.getOptionDescription(optionNode))
                .build();
    }
}
