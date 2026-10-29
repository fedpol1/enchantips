package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.*;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class SaveAllButton extends BaseSetter<InfoLine, Object> {

    protected InfoLineContainer target;

    public SaveAllButton(int x, int y, SaveAllInfoLine line, InfoLineContainer target) {
        super(x, y, line);
        this.target = target;
        this.action = () -> this.target.iterator().forEachRemaining((savableLine) -> {
            if(savableLine instanceof ConfigInfoLine<?> configLine) {
                configLine.save();
            }
        });
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id("config/save_all"));
    }
}
