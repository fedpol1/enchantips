package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.data.visitor.OptionVisitor;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import net.minecraft.text.Text;

public class OptionNode<T> extends AbstractNode{

    private final ModOption<T> meta;

    public OptionNode(ModOption<T> meta) {
        super(meta.getKey());
        this.meta = meta;
    }

    public Data<T> getData() {
        return this.meta.getData();
    }

    public T getValue() {
        return this.meta.getData().getValue();
    }

    public Option<?> getYaclOption() {
        OptionDescription.Builder desc = OptionDescription.createBuilder();
        for(int i = 0; i < this.meta.getNumTooltipLines(); i++) {
            desc = desc
                    .text(Text.translatable(this.getFullName() + ".option_tooltip." + i))
                    .text(Text.literal(""));
        }

        Option.Builder<?> b = (Option.Builder<?>) this.meta.getData().accept(new OptionVisitor());
        return b.name(Text.translatable(this.getFullName() + ".option_title"))
                .description(desc.build())
                .build();
    }

    public Object accept(ScreenVisitor v, Object data) {
        return v.visit(this, data);
    }
}
