package com.fedpol1.enchantips.config.data;

public interface DataEntry<T, U> {

    int getNumTooltipLines();
    String getKey();
    T getData();
    U getValue();
}
