package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;

public abstract class TextSetter<T> extends BaseSetter<ConfigInfoLine<T>, T> {

    protected TextField textField;

    public TextSetter(int x, int y, ConfigInfoLine<T> line, T value) {
        super(x, y, line);
        this.value = value;
        this.action = () -> this.textField.setEditing(!this.textField.editing);
    }

    public abstract void readStringValue(String s);

    public abstract String getStringValue();

    public void setValue(T c) {
        this.value = c;
        this.textField.setText(this.getStringValue());
        this.textField.selectionManager.setSelectionRange(
                this.textField.selectionManager.getCursorPos(),
                this.textField.selectionManager.getSelectionPos()
        );
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    public void onUnfocus() {
        this.textField.setEditing(false);
    }

    public boolean keyPressed(KeyEvent input) {
        if(!this.isFocused()) { return false; }
        if(this.textField.keyPressed(input)) {
            this.readStringValue(this.textField.text);
            return true;
        }
        return false;
    }

    public boolean charTyped(CharacterEvent input) {
        if(!this.isFocused()) { return false; }
        if(this.textField.charTyped(input)) {
            this.readStringValue(this.textField.text);
            return true;
        }
        return false;
    }
}
