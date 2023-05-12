package com.fedpol1.enchantips.config.data.visitor;

import com.fedpol1.enchantips.config.data.BooleanDataEntry;
import com.fedpol1.enchantips.config.data.ColorDataEntry;
import com.fedpol1.enchantips.config.data.IntegerDataEntry;

public interface DataVisitor {

    Object visit(BooleanDataEntry.BooleanData data);
    Object visit(IntegerDataEntry.IntegerData data);
    Object visit(ColorDataEntry.ColorData data);
}
