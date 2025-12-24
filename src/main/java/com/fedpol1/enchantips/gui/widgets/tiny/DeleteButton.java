package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModCategory;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.gui.widgets.info_line.*;
import java.util.Map;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.input.MouseButtonEvent;

public class DeleteButton extends BaseSetter<DeleteInfoLine, Object> {

    public DeleteButton(int x, int y, DeleteInfoLine line) {
        super(x, y, line);
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    @Override
    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta, EnchantipsClient.id("config/delete"));
    }

    @Override
    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return super.mouseClicked(click, doubled, () -> {
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
        });
    }
}
