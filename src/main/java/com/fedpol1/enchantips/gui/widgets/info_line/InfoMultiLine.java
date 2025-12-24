package com.fedpol1.enchantips.gui.widgets.info_line;

import net.minecraft.network.chat.Component;

public interface InfoMultiLine {

    void addLine(Component line);

    void addLine(InfoLine line);
}
