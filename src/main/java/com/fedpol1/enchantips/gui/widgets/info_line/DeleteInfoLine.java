package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.DeleteButton;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;

public class DeleteInfoLine extends CollapsibleInfoLine implements Renderable, GuiEventListener {

    protected final DeleteButton deleteButton;

    public DeleteInfoLine(Component text) {
        super(text);
        this.deleteButton = new DeleteButton(this.x, this.y, this);
        this.setters.add(this.deleteButton);
    }
}
