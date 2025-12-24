package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import com.google.common.base.CharMatcher;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.font.TextFieldHelper;
import net.minecraft.client.input.CharacterEvent;
import net.minecraft.client.input.KeyEvent;
import net.minecraft.client.input.MouseButtonEvent;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;

public class TextField extends BaseSetter<ConfigInfoLine<?>, String> {

    protected String text;
    protected TextFieldHelper selectionManager;
    protected int maximumLength;
    protected String allowedCharacters;
    protected boolean focused;

    public TextField(
            int x,
            int y,
            ConfigInfoLine<?> line,
            int maximumLength,
            String allowedCharacters
    ) {
        super(x, y, line);
        this.text = "";
        this.selectionManager = new TextFieldHelper(
                this::getText,
                this::setText,
                TextFieldHelper.createClipboardGetter(Minecraft.getInstance()),
                TextFieldHelper.createClipboardSetter(Minecraft.getInstance()),
                s -> CharMatcher.anyOf(this.allowedCharacters).matchesAllOf(s)
        );
        this.maximumLength = maximumLength;
        this.allowedCharacters = allowedCharacters;
        this.focused = false;
    }

    public int getWidth() {
        Font textRenderer = Minecraft.getInstance().font;
        int maxWidth = 0;
        for(int c : this.allowedCharacters.toCharArray()) {
            maxWidth = Math.max(maxWidth, textRenderer.width(Component.literal(Character.toString(c))));
        }
        return maxWidth * this.maximumLength + 1;
    }

    public int getHeight() {
        return 9;
    }

    @Override
    public boolean canTrigger() {
        return true;
    }

    public String getText() {
        return this.text;
    }

    public void setText(String s) {
        this.text = s;
    }

    public void render(GuiGraphics context, int mouseX, int mouseY, float delta) {
        Font textRenderer = Minecraft.getInstance().font;

        int textWidth = textRenderer.width(Component.literal(this.text));
        // positions measured from right edge
        int sectionStartPos = textRenderer.width(Component.literal(this.text.substring(this.selectionManager.getCursorPos())));
        int sectionEndPos = textRenderer.width(Component.literal(this.text.substring(this.selectionManager.getSelectionPos())));
        String selectionPath = sectionStartPos < sectionEndPos ? "selection" : "selection_alt";
        int width = this.getWidth();

        if(this.focused) {
            context.blitSprite(
                    RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                    EnchantipsClient.id(selectionPath),
                    this.x - 1 + width - Math.max(sectionStartPos, sectionEndPos), this.y,
                    1 + Math.abs(sectionEndPos - sectionStartPos), 9
            );
        }
        context.drawString(
                Minecraft.getInstance().font,
                Component.literal(this.text),
                this.x + width - textWidth, this.y + 1,
                0xffffffff, false
        );
    }

    public boolean mouseClicked(MouseButtonEvent click, boolean doubled) {
        return false;
    }

    public boolean keyPressed(KeyEvent input) {
        if(!this.focused) { return false; }
        if(this.selectionManager.keyPressed(input)) {
            this.shiftText();
            return true;
        }
        return false;
    }

    public boolean charTyped(CharacterEvent input) {
        if(!this.focused) { return false; }
        if(this.selectionManager.charTyped(input)) {
            this.shiftText();
            return true;
        }
        return false;
    }

    private void shiftText() {
        int deletePosition = 0;
        int deleteLength = Math.max(0, this.text.length() - this.maximumLength);
        if(this.selectionManager.getCursorPos() < this.text.length()) {
            deletePosition = this.selectionManager.getCursorPos();
        }
        int deleteLengthAtStart = Math.max(0, deletePosition + deleteLength - this.text.length());
        deleteLength -= deleteLengthAtStart;

        if(this.text.length() > this.maximumLength) {
            this.text = this.text.substring(deleteLengthAtStart, deletePosition) +
                    this.text.substring(deletePosition + deleteLength);
            this.selectionManager.setSelectionRange(
                    Math.min(this.text.length(), this.selectionManager.getCursorPos()),
                    Math.min(this.text.length(), this.selectionManager.getSelectionPos())
            );
        }
    }
}
