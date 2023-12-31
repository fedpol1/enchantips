package com.fedpol1.enchantips.gui.widgets;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class AnvilSwapButton extends TexturedButtonWidget {

    public AnvilSwapButton(int x, int y, PressAction onPress) {
        super(x, y, 16, 16,
                new ButtonTextures(Icon.DEFAULT.texture, Icon.HOVER.texture),
                onPress,
                Text.translatable("narrator.button.enchantips.anvil_swap"));
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
