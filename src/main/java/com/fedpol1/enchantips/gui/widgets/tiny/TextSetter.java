package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.gui.Click;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;

public abstract class TextSetter<T> extends BaseSetter<ConfigInfoLine<T>, T> {

    protected boolean focused;
    protected TextField textField;

    public TextSetter(int x, int y, ConfigInfoLine<T> line, T value) {
        super(x, y, line);
        this.value = value;
        this.focused = false;
    }

    public abstract void readStringValue(String s);

    public abstract String getStringValue();

    public void setValue(T c) {
        this.value = c;
        this.textField.setText(this.getStringValue());
        this.textField.selectionManager.setSelection(
                this.textField.selectionManager.getSelectionStart(),
                this.textField.selectionManager.getSelectionEnd()
        );
    }

    public boolean mouseClicked(Click click, boolean doubled) {
        boolean isWithinBounds = super.mouseClicked(click, doubled, () -> this.textField.selectionManager.selectAll());
        this.focused = isWithinBounds;
        this.textField.focused = isWithinBounds;
        return isWithinBounds;
    }

    public boolean keyPressed(KeyInput input) {
        if(!this.focused) { return false; }
        if(this.textField.keyPressed(input)) {
            this.readStringValue(this.textField.text);
            return true;
        }
        return false;
    }

    public boolean charTyped(CharInput input) {
        if(!this.focused) { return false; }
        if(this.textField.charTyped(input)) {
            this.readStringValue(this.textField.text);
            return true;
        }
        return false;
    }
}
