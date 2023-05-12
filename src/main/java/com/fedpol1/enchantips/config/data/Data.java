package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.data.visitor.DataVisitor;

public interface Data<T> {

        DataEntry getEntry();

        void setValueToDefault();

        T getValue();

        String getStringValue();

        T getDefaultValue();

        void setValue(T c);

        void readStringValue(String c);

        Object accept(DataVisitor v);
}
