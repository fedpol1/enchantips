package com.fedpol1.enchantips.config.data.visitor;

import com.fedpol1.enchantips.config.data.BooleanDataEntry;
import com.fedpol1.enchantips.config.data.ColorDataEntry;
import com.fedpol1.enchantips.config.data.IntegerDataEntry;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.controller.*;
import dev.isxander.yacl3.gui.controllers.ColorController;
import dev.isxander.yacl3.gui.controllers.TickBoxController;
import dev.isxander.yacl3.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl3.gui.controllers.string.number.IntegerFieldController;

import java.awt.*;

public class OptionVisitor implements DataVisitor {

    public Option.Builder<Boolean> visit(BooleanDataEntry.BooleanData data) {
        return Option.createBuilder(Boolean.class)
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(TickBoxControllerBuilder::create);
    }

    public Object visit(IntegerDataEntry.IntegerData data) {
        return Option.createBuilder(Integer.class)
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

    public Object visit(ColorDataEntry.ColorData data) {
        return Option.createBuilder(Color.class)
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(ColorControllerBuilder::create);
    }
}
