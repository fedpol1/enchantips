package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;

public class SaveButton extends BaseSetter<ConfigInfoLine<?>, Object> {

    public SaveButton(int x, int y, ConfigInfoLine<?> line) {
        super(x, y, line);
        this.action = this.line::save;
    }

    @Override
    public boolean canTrigger() {
        return this.line.canSave();
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id("config/save"));
    }

    protected List<Component> getTooltip() {
        return this.line.getSaveButtonTooltip();
    }
}
