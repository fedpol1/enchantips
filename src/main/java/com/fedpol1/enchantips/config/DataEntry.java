package com.fedpol1.enchantips.config;

import me.shedaniel.clothconfig2.api.ConfigCategory;

public interface DataEntry {

    String getKey();

    String getTitle();

    String getTooltip();

    ModConfigCategory getCategory();
}
