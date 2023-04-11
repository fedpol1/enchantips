package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.data.AbstractDataEntry;
import com.fedpol1.enchantips.config.data.DataEntry;
import com.fedpol1.enchantips.config.tree.visitor.ReadVisitor;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import com.fedpol1.enchantips.config.tree.visitor.WriteVisitor;
import dev.isxander.yacl.api.Option;

public class OptionNode<T> extends AbstractNode{

    private final AbstractDataEntry<T> entry;

    public OptionNode(AbstractDataEntry<T> entry) {
        super(entry.getKey());
        this.entry = entry;
    }

    public AbstractDataEntry<T> getEntry() {
        return this.entry;
    }

    public T getValue() {
        return this.entry.getData().getValue();
    }
    public Option<T> getYaclOption() {
        return this.entry.getData().getOption();
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }

    public Object accept(ReadVisitor v, Object data) {
        return v.visit(this, data);
    }

    public Object accept(WriteVisitor v, Object data) {
        return v.visit(this, data);
    }
}
