package com.fedpol1.enchantips.config.data;

public interface DataEntry<T, U> {

    boolean hasTooltip();
    String getKey();
    T getData();
    U getValue();
}
