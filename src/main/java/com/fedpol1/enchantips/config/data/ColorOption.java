package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ColorConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.ColorControllerBuilder;
import net.minecraft.text.Text;

import java.awt.Color;
import java.util.List;
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

    public List<Text> getSaveTooltip(Color v) {
        if(this.canSet(v)) {
            return null;
        }
        return List.of(Text.translatable("enchantips.gui.setter.color.error.invalid"));
    }

    public ConfigInfoLine<Color> getConfigLine(OptionNode<Color> optionNode) {
        return new ColorConfigInfoLine(
                this.getOptionTitle(optionNode),
                this.getOptionTooltip(optionNode),
                this,
                this.color
        );
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
