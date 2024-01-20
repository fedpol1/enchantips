package com.fedpol1.enchantips.config.tree;

import com.fedpol1.enchantips.config.ModOption;

public interface OptionParent {

    <T> OptionNode<T> addOption(ModOption<T> meta);
}
