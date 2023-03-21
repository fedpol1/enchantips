package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.ModConfigCategory;

public interface DataEntry<T, U> {

    String getKey();

    String getTitle();

    String getTooltip();

    ModConfigCategory getCategory();

    T getData();

    U getValue();
}
