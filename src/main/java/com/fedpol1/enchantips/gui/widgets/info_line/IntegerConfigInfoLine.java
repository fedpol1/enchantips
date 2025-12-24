package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.config.data.IntegerOption;
import com.fedpol1.enchantips.gui.widgets.tiny.IntegerSetter;
import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class IntegerConfigInfoLine extends ConfigInfoLine<Integer> implements Renderable, GuiEventListener {

    public IntegerConfigInfoLine(Component text, List<Component> tooltip, Data<Integer> data, Integer value) {
        super(text, tooltip, data);
        this.setter = new IntegerSetter(this.x + this.resetButton.getWidth() + this.saveButton.getWidth() + 2, this.y, this, value);
        this.setters.add(this.setter);
    }

    public int getMaximumDigits() {
        int lowerBound = ((IntegerOption) this.data).getMin();
        int upperBound = ((IntegerOption) this.data).getMax();
        lowerBound = lowerBound == 0 ? 1 : Math.abs(lowerBound);
        upperBound = upperBound == 0 ? 1 : Math.abs(upperBound);
        int lowerDigits = (int) Math.floor(Math.log10(lowerBound)) + (lowerBound < 0 ? 2 : 1);
        int upperDigits = (int) Math.floor(Math.log10(upperBound)) + (upperBound < 0 ? 2 : 1);
        return Math.max(lowerDigits, upperDigits);
    }
}
