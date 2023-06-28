package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.data.visitor.DataVisitor;

public interface Data<T> {

        T getValue();

        String getStringValue();

        T getDefaultValue();

        void setValue(T c);

        void readStringValue(String c);

        Object accept(DataVisitor v);
}
