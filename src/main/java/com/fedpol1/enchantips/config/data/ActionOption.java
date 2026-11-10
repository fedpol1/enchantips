package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ActionConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import java.util.List;

import net.minecraft.network.chat.Component;

public class ActionOption implements Data<Runnable> {

    private Runnable action;

    public ActionOption(Runnable action) {
        this.action = action;
    }

    public Runnable getValue() {
        return this.getDefaultValue();
    }

    public String getStringValue() {
        return "";
    }

    public Runnable getDefaultValue() {
        // this should never be null
        return this.action;
    }

    public void setValue(Runnable action) {
        this.action = action;
    }

    public boolean canSet(Runnable action) {
        return false;
    }

    public void readStringValue(String s) {
        // empty
    }

    public List<Component> getSaveTooltip(Runnable v) {
        return null;
    }

    public ConfigInfoLine<Runnable> getConfigLine(OptionNode<Runnable> optionNode) {
        return new ActionConfigInfoLine(
                this.getOptionTitle(optionNode),
                this.getOptionTooltip(optionNode),
                this,
                this.action
        );
    }
}
