package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.ColorSetter;
import java.awt.*;
import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class ColorConfigInfoLine extends ConfigInfoLine<Color> implements Renderable, GuiEventListener {

    public ColorConfigInfoLine(Component text, List<Component> tooltip, Data<Color> data, Color value) {
        super(text, tooltip, data);
        this.setter = new ColorSetter(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, this, value);
        this.setters.add(this.setter);
    }
}
