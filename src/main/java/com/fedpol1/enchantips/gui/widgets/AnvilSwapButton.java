package com.fedpol1.enchantips.gui.widgets;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;

public class AnvilSwapButton extends ImageButton {

    public AnvilSwapButton(int x, int y, OnPress onPress) {
        super(x, y, 16, 16,
                new WidgetSprites(Icon.DEFAULT.texture, Icon.HOVER.texture),
                onPress,
                Component.translatable("narrator.button.enchantips.anvil_swap"));
    }

    enum Icon {
        DEFAULT(EnchantipsClient.id("widget/anvil_swap_button_default")),
        HOVER(EnchantipsClient.id("widget/anvil_swap_button_hover"));

        final Identifier texture;

        Icon(Identifier texture) {
            this.texture = texture;
        }
    }
}
