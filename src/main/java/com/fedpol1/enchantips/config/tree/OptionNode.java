package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.tree.visitor.TreeVisitor;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import dev.isxander.yacl3.api.Option;

public class OptionNode<T> extends Node {

    private final Data<T> data;
    private final int tooltipLines;

    protected OptionNode(ModOption<T> meta, Node parent) {
        super(meta.getKey(), parent);
        this.data = meta.getData();
        this.tooltipLines = meta.getNumTooltipLines();
    }

    public Data<T> getData() {
        return this.data;
    }

    public T getValue() {
        return this.data.getValue();
    }

    public int getNumTooltipLines() {
        return this.tooltipLines;
    }

    public ConfigInfoLine<?> getConfigLine() {
        return this.getData().getConfigLine(this);
    }

    public Option<?> getYaclOption() {
        return this.getData().getYaclOption(this);
    }

    public Object accept(TreeVisitor v, Object data) {
        return v.visit(this, data);
    }
}
