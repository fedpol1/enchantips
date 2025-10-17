package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.BooleanConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class BooleanButton extends BaseSetter<ConfigInfoLine<Boolean>, Boolean> {

    public BooleanButton(int x, int y, BooleanConfigInfoLine line, boolean state) {
        super(x, y, line);
        this.value = state;
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        String path = this.value ? "config/true" : "config/false";
        super.render(context, mouseX, mouseY, delta, Identifier.of(EnchantipsClient.MODID, path));
    }

    @Override
    public boolean mouseClicked(Click click, boolean doubled) {
        return super.mouseClicked(click, doubled, () -> this.value = !this.value);
    }
}
