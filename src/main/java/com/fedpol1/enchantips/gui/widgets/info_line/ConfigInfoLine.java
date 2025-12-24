package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.BaseSetter;
import com.fedpol1.enchantips.gui.widgets.tiny.ResetButton;
import com.fedpol1.enchantips.gui.widgets.tiny.SaveButton;
import java.util.List;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public abstract class ConfigInfoLine<T> extends CollapsibleInfoLine implements Renderable, GuiEventListener {

    protected final Data<T> data;
    protected final ResetButton resetButton;
    protected final SaveButton saveButton;
    protected BaseSetter<ConfigInfoLine<T>, T> setter;

    public ConfigInfoLine(Component text, List<Component> tooltip, Data<T> data) {
        super(text);
        for(Component tooltipLine : tooltip) {
            this.lines.addLine(tooltipLine);
        }
        this.data = data;
        this.resetButton = new ResetButton(this.x + this.expandButton.getWidth() + 1, this.y, this);
        this.saveButton = new SaveButton(this.x + this.expandButton.getWidth() + this.resetButton.getWidth() + 2, this.y, this);
        this.setters.add(this.resetButton);
        this.setters.add(this.saveButton);
    }

    public void reset() {
        this.setter.setValue(this.data.getDefaultValue());
    }

    public boolean isDefault() {
        return this.data.getDefaultValue().equals(this.setter.getValue());
    }

    public boolean canSave() {
        return this.data.canSet(this.setter.getValue()) && !this.data.getValue().equals(this.setter.getValue());
    }

    public void save() {
        if(this.canSave()) {
            this.data.setValue(this.setter.getValue());
        }
    }

    public List<Component> getSaveButtonTooltip() {
        return this.data.getSaveTooltip(this.setter.getValue());
    }
}
