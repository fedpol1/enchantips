package com.fedpol1.enchantips.config.data.visitor;

import com.fedpol1.enchantips.config.data.*;

public interface DataVisitor {

    Object visit(BooleanOption data);
    Object visit(IntegerOption data);
    Object visit(ColorOption data);
}
