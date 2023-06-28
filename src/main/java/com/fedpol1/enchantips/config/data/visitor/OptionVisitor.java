package com.fedpol1.enchantips.config.data.visitor;

import com.fedpol1.enchantips.config.data.*;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.*;
import java.awt.Color;

public class OptionVisitor implements DataVisitor {

    public Option.Builder<Boolean> visit(BooleanOption data) {
        return Option.<Boolean>createBuilder()
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(TickBoxControllerBuilder::create);
    }

    public Option.Builder<Integer> visit(IntegerOption data) {
        return Option.<Integer>createBuilder()
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(o -> data.getStep() == 0 ?
                        IntegerFieldControllerBuilder.create(o)
                                .min(data.getMin())
                                .max(data.getMax()) :
                        IntegerSliderControllerBuilder.create(o)
                                .range(data.getMin(), data.getMax())
                                .step(data.getStep())
                );
    }

    public Option.Builder<Color> visit(ColorOption data) {
        return Option.<Color>createBuilder()
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(ColorControllerBuilder::create);
    }
}
