package com.fedpol1.enchantips.gui.widgets;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AnvilSwapButton extends ButtonWidget {
    public AnvilSwapButton(int x, int y, PressAction onPress) {
        super(x, y, 16, 16, Text.translatable("narrator.button.enchantips.anvil_swap"), onPress, DEFAULT_NARRATION_SUPPLIER);
    }

    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        AnvilSwapButton.Icon icon = this.isSelected() ? Icon.HOVER : Icon.DEFAULT;
        context.drawGuiTexture(icon.texture, this.getX(), this.getY(), this.width, this.height);
    }

    enum Icon {
        DEFAULT(new Identifier(EnchantipsClient.MODID, "widget/anvil_swap_button_default")),
        HOVER(new Identifier(EnchantipsClient.MODID, "widget/anvil_swap_button_hover"));

        final Identifier texture;

        Icon(Identifier texture) {
            this.texture = texture;
        }
    }
}
