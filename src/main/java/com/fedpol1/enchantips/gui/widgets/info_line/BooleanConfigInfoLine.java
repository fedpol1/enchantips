package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.gui.widgets.tiny_button.BooleanButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.text.Text;

public class BooleanConfigInfoLine extends InfoLine implements Drawable, Element {

    private final BooleanButton button;

    public BooleanConfigInfoLine(Text text, boolean state) {
        super(text);
        this.height = 0;
        this.button = new BooleanButton(this.x, this.y, state);
    }

    @Override
    public void refresh(int index) {
        super.refresh(index);
        this.button.setPosition(this.x, this.y);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if(this.y < this.nearestScrollableParent.y) { return; }
        if(this.y + InfoDelineator.LINE_HEIGHT > this.nearestScrollableParent.y + this.nearestScrollableParent.height) { return; }

        TextRenderer renderer = MinecraftClient.getInstance().textRenderer;
        this.button.render(context, mouseX, mouseY, delta);
        context.drawText(
                renderer,
                this.text,
                this.x + this.button.getWidth() + 1,
                this.y + 1,
                this.nearestScrollableParent.childColor,
                false
        );
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if(this.button.mouseClicked(mouseX, mouseY, button)) {
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
}
