package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.config.tree.ConfigTree;
import com.fedpol1.enchantips.config.tree.visitor.ScreenVisitor2;
import com.fedpol1.enchantips.gui.widgets.info_line.InfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.SaveAllInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class ConfigScreen extends BaseScreen {

    private final SaveAllInfoLine saveAllLine;
    private final ScrollableInfoLineContainer saveAllLineContainer;

    public ConfigScreen(@Nullable Screen parent) {
        super(Component.translatable(EnchantipsClient.MODID + ".gui.config"), parent);
        this.lines = (ScrollableInfoLineContainer) ConfigTree.root.accept(new ScreenVisitor2(), null);
        this.saveAllLineContainer = new ScrollableInfoLineContainer(0xff404040, 0, true);
        this.saveAllLine = new SaveAllInfoLine(Component.translatable("enchantips.gui.save_all"), this.lines);
        this.saveAllLineContainer.addLine(this.saveAllLine);
    }

    public void extractRenderState(GuiGraphicsExtractor extractor, int mouseX, int mouseY, float delta) {
        super.extractRenderState(extractor, mouseX, mouseY, delta);
        this.saveAllLineContainer.extractRenderState(extractor, mouseX, mouseY, delta);
    }

    @Override
    protected void init() {
        super.init();
        this.saveAllLine.refresh(0);
        this.saveAllLineContainer.setPosition(this.windowX + 8, this.windowY + 5);
        this.saveAllLineContainer.setDimensions(this.windowWidth - 16, InfoLine.LINE_HEIGHT);
        this.saveAllLineContainer.refresh(0);
        this.saveAllLineContainer.refresh(0);
    }

    // no mouseScrolled override for the saveAllLineContainer

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        if(super.mouseClicked(click, doubled)) {
            return true;
        }
        return this.saveAllLineContainer.mouseClicked(click, doubled);
    }

    public boolean mouseDragged(MouseButtonEvent click, double offsetX, double offsetY) {
        if(super.mouseDragged(click, offsetX, offsetY)) {
            return true;
        }
        return this.saveAllLineContainer.mouseDragged(click, offsetX, offsetY);
    }

    public boolean keyPressed(KeyEvent input) {
        if(super.keyPressed(input)) {
            return true;
        }
        return this.saveAllLineContainer.keyPressed(input);
    }

    public boolean charTyped(CharacterEvent input) {
        if(super.charTyped(input)) {
            return true;
        }
        return this.saveAllLineContainer.charTyped(input);
    }

    @Override
    public void onClose() {
        super.onClose();
        ModConfig.writeConfig();
    }
}
