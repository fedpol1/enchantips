package com.fedpol1.enchantips.gui.widgets.tiny;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import com.google.common.base.CharMatcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.input.CharInput;
import net.minecraft.client.input.KeyInput;
import net.minecraft.client.util.SelectionManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TextField extends BaseSetter<ConfigInfoLine<?>, String> {

    protected String text;
    protected SelectionManager selectionManager;
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
        this.selectionManager = new SelectionManager(
                this::getText,
                this::setText,
                SelectionManager.makeClipboardGetter(MinecraftClient.getInstance()),
                SelectionManager.makeClipboardSetter(MinecraftClient.getInstance()),
                s -> CharMatcher.anyOf(this.allowedCharacters).matchesAllOf(s)
        );
        this.maximumLength = maximumLength;
        this.allowedCharacters = allowedCharacters;
        this.focused = false;
    }

    public int getWidth() {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;
        int maxWidth = 0;
        for(int c : this.allowedCharacters.toCharArray()) {
            maxWidth = Math.max(maxWidth, textRenderer.getWidth(Text.literal(Character.toString(c))));
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

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

        int textWidth = textRenderer.getWidth(Text.literal(this.text));
        // positions measured from right edge
        int sectionStartPos = textRenderer.getWidth(Text.literal(this.text.substring(this.selectionManager.getSelectionStart())));
        int sectionEndPos = textRenderer.getWidth(Text.literal(this.text.substring(this.selectionManager.getSelectionEnd())));
        String selectionPath = sectionStartPos < sectionEndPos ? "selection" : "selection_alt";
        int width = this.getWidth();

        if(this.focused) {
            context.drawGuiTexture(
                    RenderPipelines.GUI_TEXTURED_PREMULTIPLIED_ALPHA,
                    Identifier.of(EnchantipsClient.MODID, selectionPath),
                    this.x - 1 + width - Math.max(sectionStartPos, sectionEndPos), this.y,
                    1 + Math.abs(sectionEndPos - sectionStartPos), 9
            );
        }
        context.drawText(
                MinecraftClient.getInstance().textRenderer,
                Text.literal(this.text),
                this.x + width - textWidth, this.y + 1,
                0xffffffff, false
        );
    }

    public boolean mouseClicked(Click click, boolean doubled) {
        return false;
    }

    public boolean keyPressed(KeyInput input) {
        if(!this.focused) { return false; }
        if(this.selectionManager.handleSpecialKey(input)) {
            this.shiftText();
            return true;
        }
        return false;
    }

    public boolean charTyped(CharInput input) {
        if(!this.focused) { return false; }
        if(this.selectionManager.insert(input)) {
            this.shiftText();
            return true;
        }
        return false;
    }

    private void shiftText() {
        int deletePosition = 0;
        int deleteLength = Math.max(0, this.text.length() - this.maximumLength);
        if(this.selectionManager.getSelectionStart() < this.text.length()) {
            deletePosition = this.selectionManager.getSelectionStart();
        }
        int deleteLengthAtStart = Math.max(0, deletePosition + deleteLength - this.text.length());
        deleteLength -= deleteLengthAtStart;

        if(this.text.length() > this.maximumLength) {
            this.text = this.text.substring(deleteLengthAtStart, deletePosition) +
                    this.text.substring(deletePosition + deleteLength);
            this.selectionManager.setSelection(
                    Math.min(this.text.length(), this.selectionManager.getSelectionStart()),
                    Math.min(this.text.length(), this.selectionManager.getSelectionEnd())
            );
        }
    }
}
