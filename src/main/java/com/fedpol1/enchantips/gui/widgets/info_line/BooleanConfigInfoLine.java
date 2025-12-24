package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.BooleanButton;
import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class BooleanConfigInfoLine extends ConfigInfoLine<Boolean> implements Renderable, GuiEventListener {

    public BooleanConfigInfoLine(Component text, List<Component> tooltip, Data<Boolean> data, boolean value) {
        super(text, tooltip, data);
        this.setter = new BooleanButton(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, this, value);
        this.setters.add(this.setter);
    }
}
