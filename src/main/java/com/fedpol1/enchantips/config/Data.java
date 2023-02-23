package com.fedpol1.enchantips.config;

public interface Data<T> {

        void setValueToDefault();

        T getValue();

        T getDefaultValue();

        String getTitle();

        String getTooltip();

        void setValue(T c);
}
