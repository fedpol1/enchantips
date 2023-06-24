package com.fedpol1.enchantips.config.data.visitor;

import com.fedpol1.enchantips.config.data.BooleanDataEntry;
import com.fedpol1.enchantips.config.data.ColorDataEntry;
import com.fedpol1.enchantips.config.data.IntegerDataEntry;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.*;
import java.awt.Color;

public class OptionVisitor implements DataVisitor {

    public Option.Builder<Boolean> visit(BooleanDataEntry.BooleanData data) {
        return Option.<Boolean>createBuilder()
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(TickBoxControllerBuilder::create);
    }

    public Option.Builder<Integer> visit(IntegerDataEntry.IntegerData data) {
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

    public Option.Builder<Color> visit(ColorDataEntry.ColorData data) {
        return Option.<Color>createBuilder()
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(ColorControllerBuilder::create);
    }
}
