package com.fedpol1.enchantips.config.data.visitor;

import com.fedpol1.enchantips.config.data.BooleanDataEntry;
import com.fedpol1.enchantips.config.data.ColorDataEntry;
import com.fedpol1.enchantips.config.data.IntegerDataEntry;
import dev.isxander.yacl.api.Option;
import dev.isxander.yacl.gui.controllers.ColorController;
import dev.isxander.yacl.gui.controllers.TickBoxController;
import dev.isxander.yacl.gui.controllers.slider.IntegerSliderController;
import dev.isxander.yacl.gui.controllers.string.number.IntegerFieldController;

import java.awt.*;

public class OptionVisitor implements DataVisitor {

    public Object visit(BooleanDataEntry.BooleanData data) {
        return Option.createBuilder(Boolean.class)
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(TickBoxController::new);
    }

    public Object visit(IntegerDataEntry.IntegerData data) {
        return Option.createBuilder(Integer.class)
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(o -> data.getStep() == 0 ? new IntegerFieldController(o, data.getMin(), data.getMax()) : new IntegerSliderController(o, data.getMin(), data.getMax(), data.getStep()));
    }

    public Object visit(ColorDataEntry.ColorData data) {
        return Option.createBuilder(Color.class)
                .binding(data.getDefaultValue(), data::getValue, data::setValue)
                .controller(ColorController::new);
    }
}
