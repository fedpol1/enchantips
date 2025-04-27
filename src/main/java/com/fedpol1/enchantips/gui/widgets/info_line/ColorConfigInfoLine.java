package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.widgets.tiny.ColorSetter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

import java.awt.*;

public class ColorConfigInfoLine extends ConfigInfoLine<Color> implements Drawable, Element {

    private final ColorSetter setter;

    public ColorConfigInfoLine(Text text, ModOption<Color> option, Color state) {
        super(text, option);
        this.height = 0;
        this.setter = new ColorSetter(this.x + this.resetButton.getWidth() + 1, this.y, state) {
        };
    }

    @Override
    public void refresh(int index) {
        super.refresh(index);
        this.setter.setPosition(this.x + this.resetButton.getWidth() + 1, this.y);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(!this.shouldRender(context, mouseX, mouseY, delta)) { return; }
        super.render(context, mouseX, mouseY, delta);

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        this.setter.render(context, mouseX, mouseY, delta);
        context.drawText(
                renderer,
                this.text,
                this.x + this.resetButton.getWidth() + this.setter.getWidth() + 2,
                this.y + 1,
                this.nearestScrollableParent.childColor,
                false
        );
    }

    public void reset() {
        this.setter.setColor(this.option.getData().getDefaultValue());
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.setter.mouseClicked(mouseX, mouseY, button)) {
            for (int i = 0; i < this.parent.lines.size(); i++) {
                if (this.parent.lines.get(i) == this) {
                    this.refresh(0);
                    this.nearestScrollableParent.refresh(0);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.setter.keyPressed(keyCode, scanCode, modifiers);
    }

    public boolean charTyped(char chr, int modifiers) {
        return this.setter.charTyped(chr, modifiers);
    }
}
