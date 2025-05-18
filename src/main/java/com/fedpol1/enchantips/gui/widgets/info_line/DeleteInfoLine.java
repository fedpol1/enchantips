package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny.DeleteButton;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class DeleteInfoLine extends CollapsibleInfoLine implements Drawable, Element {

    protected final DeleteButton deleteButton;

    public DeleteInfoLine(Text text) {
        super(text);
        this.deleteButton = new DeleteButton(this.x, this.y, this);
        this.setters.add(this.deleteButton);
    }
}
