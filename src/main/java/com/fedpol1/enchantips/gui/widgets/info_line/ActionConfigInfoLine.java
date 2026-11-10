package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.data.Data;
import com.fedpol1.enchantips.gui.widgets.tiny.ActionButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.network.chat.Component;

import java.util.List;

public class ActionConfigInfoLine extends ConfigInfoLine<Runnable> implements Renderable {

    public ActionConfigInfoLine(Component text, List<Component> tooltip, Data<Runnable> data, Runnable value) {
        super(text, tooltip, data);
        this.setters.remove(this.resetButton);
        this.setters.remove(this.saveButton);
        this.setter = new ActionButton(this.x, this.y, this, value);
        this.setters.add(this.setter);
    }

    @Override
    public void reset() {}

    @Override
    public void undo() {}

    @Override
    public boolean isDefault() {
        return true;
    }

    @Override
    public boolean canSave() {
        return false;
    }

    @Override
    public boolean isSaved() {
        return true;
    }

    @Override
    public void save() {}

    public void cascadeRefresh() {
        this.nearestScrollableParent.refresh(0);
    }
}
