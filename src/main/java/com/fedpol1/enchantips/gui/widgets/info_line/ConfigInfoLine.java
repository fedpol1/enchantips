package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.widgets.tiny.ResetButton;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public abstract class ConfigInfoLine<T> extends InfoLine implements Drawable, Element {

    protected final ModOption<T> option;
    protected final ResetButton resetButton;

    public ConfigInfoLine(Text text, ModOption<T> option) {
        super(text);
        this.option = option;
        this.resetButton = new ResetButton(this.x, this.y, this);
    }

    @Override
    public void refresh(int index) {
        super.refresh(index);
        this.resetButton.setPosition(this.x, this.y);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!this.shouldRender(context, mouseX, mouseY, delta)) { return; }
        this.resetButton.render(context, mouseX, mouseY, delta);
    }

    public abstract void reset();
}
