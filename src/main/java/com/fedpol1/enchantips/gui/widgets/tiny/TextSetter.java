package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;

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

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean isWithinBounds = super.mouseClicked(mouseX, mouseY, button, () -> this.textField.selectionManager.selectAll());
        this.focused = isWithinBounds;
        this.textField.focused = isWithinBounds;
        return isWithinBounds;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if(!this.focused) { return false; }
        if(this.textField.keyPressed(keyCode, scanCode, modifiers)) {
            this.readStringValue(this.textField.text);
            return true;
        }
        return false;
    }

    public boolean charTyped(char chr, int modifiers) {
        if(!this.focused) { return false; }
        if(this.textField.charTyped(chr, modifiers)) {
            this.readStringValue(this.textField.text);
            return true;
        }
        return false;
    }
}
