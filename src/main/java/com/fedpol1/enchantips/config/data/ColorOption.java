package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;

import java.awt.Color;
import java.util.Locale;

public class ColorOption implements Data<Color> {

    private Color color;
    private final Color defaultValue;

    public ColorOption(int defaultValue) {
        this.color = new Color(defaultValue);
        this.defaultValue = new Color(defaultValue);
    }

    public Color getValue() {
        return this.color == null ? this.getDefaultValue() : this.color;
    }

    public String getStringValue() {
        return String.format(Locale.ROOT, "#%06X", this.getValue().getRGB() & 0xffffff); // discard alpha
    }

    public Color getDefaultValue() {
        // this should never be null
        return this.defaultValue;
    }

    public void setValue(Color v) {
        this.color = v;
    }

    public boolean canSet(Color v) {
        return v != null;
    }

    public void readStringValue(String s) {
        this.color = new Color(Integer.parseInt(s.substring(1), 16));
    }

    public Option<Color> getYaclOption(OptionNode<Color> optionNode) {
        return Option.<Color>createBuilder()
                .binding(this.getDefaultValue(), this::getValue, this::setValue)
                .controller(ColorControllerBuilder::create)
                .name(this.getOptionTitle(optionNode))
                .description(this.getOptionDescription(optionNode))
                .build();
    }
}
