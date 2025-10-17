package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.InfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public abstract class BaseScreen extends Screen {

    protected static final Identifier FRAME_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/frame");
    protected static final Identifier BACKGROUND_TEXTURE = Identifier.of(EnchantipsClient.MODID, "enchantment_info/background");

    protected int windowX;
    protected int windowY;
    protected int windowWidth;
    protected int windowHeight;
    protected ScrollableInfoLineContainer lines;
    protected final Screen parent;

    public BaseScreen(Text title, @Nullable Screen parent) {
        super(title);
        this.parent = parent;
        this.calculateDimensions();

    }

    private void calculateDimensions() {
        this.windowX = this.width/20;
        this.windowY = this.height/20;
        this.windowWidth = this.width * 18/20;
        this.windowHeight = Math.max(this.height * 18/20, InfoLine.LINE_HEIGHT + 39);
        int extra = Math.floorMod(this.windowHeight - 39, InfoLine.LINE_HEIGHT);
        this.windowY += extra/2;
        this.windowHeight -= extra;
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawWindow(context);
        this.lines.render(context, mouseX, mouseY, delta);
    }

    public void drawWindow(DrawContext context) {
        this.calculateDimensions();
        context.drawGuiTexture(
                RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                BaseScreen.BACKGROUND_TEXTURE,
                this.windowX, this.windowY, this.windowWidth, this.windowHeight
        );
        context.drawGuiTexture(
                RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                BaseScreen.FRAME_TEXTURE,
                this.windowX, this.windowY, this.windowWidth, this.windowHeight
        );
        context.drawText(this.textRenderer, this.title, this.windowX + 8, this.windowY + 6, 0xff404040, false);
    }

    @Override
    protected void init() {
        this.calculateDimensions();
        this.lines.setPosition(this.windowX + 15, this.windowY + 24);
        this.lines.setDimensions(this.windowWidth - 30, this.windowHeight - 39);
        this.lines.refresh(0);
    }

    public boolean mouseScrolled(double mouseX, double mouseY, double horizontalAmount, double verticalAmount) {
        super.mouseScrolled(mouseX, mouseY, horizontalAmount, verticalAmount);
        lines.scroll((int)verticalAmount);
        return false;
    }

    public boolean mouseClicked(Click click, boolean doubled) {
        return this.lines.mouseClicked(click, doubled);
    }

    public boolean mouseDragged(Click click, double offsetX, double offsetY) {
        return this.lines.mouseDragged(click, offsetX, offsetY);
    }

    public boolean keyPressed(KeyInput input) {
        if(super.keyPressed(input)) {
            return true;
        }
        return this.lines.keyPressed(input);
    }

    public boolean charTyped(CharInput input) {
        return this.lines.charTyped(input);
    }

    @Override
    public void close() {
        assert this.client != null;
        this.client.setScreen(this.parent);
    }
}
