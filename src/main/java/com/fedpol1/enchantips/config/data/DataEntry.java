package com.fedpol1.enchantips.config.data;

public interface DataEntry<T, U> {

    String getKey();

    T getData();

    U getValue();
}
