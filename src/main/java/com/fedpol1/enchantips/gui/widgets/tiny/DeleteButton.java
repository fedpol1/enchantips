package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModCategory;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.gui.widgets.info_line.*;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphicsExtractor;

public class DeleteButton extends BaseSetter<DeleteInfoLine, Object> {

    public DeleteButton(int x, int y, DeleteInfoLine line) {
        super(x, y, line);
        this.action = () -> {
            EnchantmentConfigInfoLine parent = (EnchantmentConfigInfoLine) this.line.getParent().getParent();
            InfoLineContainer grandparent = parent.getParent();
            grandparent.removeLine(parent);

            String toRemove = null;
            for(Map.Entry<String, Node> entry : ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().getChildren()) {
                if(entry.getValue() instanceof EnchantmentGroupNode ench && ench == parent.getNode()) {
                    toRemove = entry.getKey();
                    break;
                }
            }
            ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode().removeChild(toRemove);
        };
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta, EnchantipsClient.id("config/delete"));
    }
}
