package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.SaveAllButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

public class SaveAllInfoLine extends InfoLine implements Renderable {

    protected final SaveAllButton saveAllButton;

    public SaveAllInfoLine(Component text, InfoLineContainer target) {
        super(text);
        this.saveAllButton = new SaveAllButton(this.x + this.width - 9, this.y, this, target);
        this.setters.add(this.saveAllButton);
    }
}
