package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.BooleanOption;
import com.fedpol1.enchantips.config.data.ColorOption;
import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.data.IntegerOption;
import com.fedpol1.enchantips.config.tree.visitor.TreeVisitor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.*;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.awt.*;

public class EnchantmentGroupNode extends GroupNode {

    private boolean known;
    private final String identifier;
    private Text description;
    private final OptionNode<Color> minColor;
    private final OptionNode<Color> maxColor;
    private final OptionNode<Color> overmaxColor;
    private final OptionNode<Integer> order;
    private final OptionNode<Boolean> highlight;

    public EnchantmentGroupNode(String ench, Node parent) {
        super(ench, parent);
        ModConfigData.addEnchantment(ench, this);

        this.known = false;
        this.identifier = ench;
        this.description = Text.translatable("enchantment.enchantips.unknown").setStyle(Style.EMPTY).formatted(Formatting.ITALIC);
        this.minColor = this.addOption(new ColorOption(0x9f7f7f),  ModConfigData.MIN_COLOR_KEY, 0);
        this.maxColor = this.addOption(new ColorOption(0xffdfdf),  ModConfigData.MAX_COLOR_KEY, 0);
        this.overmaxColor = this.addOption(new ColorOption(0xffdf3f),  ModConfigData.OVERMAX_COLOR_KEY, 0);
        this.order = this.addOption(new IntegerOption(0, -500000000, 500000000, 0),  ModConfigData.ORDER_KEY, 1);
        this.highlight = this.addOption(new BooleanOption(true),  ModConfigData.HIGHLIGHT_KEY, 1);
    }

    protected EnchantmentGroupNode(RegistryKey<Enchantment> key, Node parent) {
        this(key.getValue().toString(), parent);
    }

    private <T> OptionNode<T> addOption(Data<T> data, String key, int tooltipLines) {
        OptionNode<T> opt = new OptionNode<>(new ModOption<>(data, key, tooltipLines), this);
        opt.fullName = parent.getFullName() + "." + opt.getName();
        return opt;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public Text getDescription() {
        return this.description;
    }

    public void setDescription(Text description) {
        this.known = true;
        this.description = description;
    }

    public boolean isKnown() {
        return this.known;
    }

    public OptionNode<Color> getMinColor() {
        return this.minColor;
    }

    public OptionNode<Color> getMaxColor() {
        return this.maxColor;
    }

    public OptionNode<Color> getOvermaxColor() {
        return this.overmaxColor;
    }

    public OptionNode<Integer> getOrder() {
        return this.order;
    }

    public OptionNode<Boolean> getHighlight() {
        return this.highlight;
    }

    public void setMinColor(Color value) {
        this.setProperty(this.minColor, value);
    }

    public void setMaxColor(Color value) {
        this.setProperty(this.maxColor, value);
    }

    public void setOvermaxColor(Color value) {
        this.setProperty(this.overmaxColor, value);
    }

    public void copyMaxColorToOvermaxColor() {
        this.setProperty(this.overmaxColor, this.maxColor.getValue());
    }

    public void setOrder(Integer value) {
        this.setProperty(this.order, value);
    }

    public void setHighlight(Boolean value) {
        this.setProperty(this.highlight, value);
    }

    private <T> void setProperty(OptionNode<T> property, T value) {
        if(property.parent != this) {
            throw new UnsupportedOperationException("Property must be a child.");
        }
        if(value == null) {
            property.getData().setValue(property.getData().getDefaultValue());
            return;
        }
        property.getData().setValue(value);
    }

    public Object accept(TreeVisitor v, Object data) {
        return v.visit(this, data);
    }
}
