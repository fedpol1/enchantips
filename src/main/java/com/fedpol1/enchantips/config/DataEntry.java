package com.fedpol1.enchantips.config;

public interface DataEntry<T, U> {

    String getKey();

    String getTitle();

    String getTooltip();

    ModConfigCategory getCategory();

    T getData();

    U getValue();
}
