package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.BooleanConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class BooleanButton extends BaseSetter<ConfigInfoLine<Boolean>, Boolean> {

    public BooleanButton(int x, int y, BooleanConfigInfoLine line, boolean state) {
        super(x, y, line);
        this.value = state;
        this.action = () -> this.value = !this.value;
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        String path = this.value ? "config/true" : "config/false";
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id(path));
    }
}
