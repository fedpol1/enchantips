package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.text.MutableText;
import net.minecraft.text.PlainTextContent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class ScrollableTooltipSection {

    private static ScrollableTooltipSection activeSection; // there can only be one; handled screens set this to null every frame
    private static ScrollableTooltipSection previousNonEmpty;
    public static final ScrollableTooltipSection EMPTY = new ScrollableTooltipSection(new ArrayList<>());
    private static final int MAX_LINES_SHOWN = 7;

    private final List<Text> text;
    private int position;

    public ScrollableTooltipSection(List<Text> text) {
        this.text = text;
        this.position = 0;
    }

    public static void setActiveSection(ScrollableTooltipSection s) {
        if(s == null) {
            throw new IllegalStateException("Active scrollable tooltip section cannot be null.");
        }
        if(ScrollableTooltipSection.activeSection != ScrollableTooltipSection.EMPTY) {
            ScrollableTooltipSection.previousNonEmpty = ScrollableTooltipSection.activeSection;
        }
        if(s != ScrollableTooltipSection.previousNonEmpty) { // if active section actually changes, reset position
            s.position = 0;
        }
        ScrollableTooltipSection.activeSection = s;
    }

    public static ScrollableTooltipSection getActiveSection() {
        return ScrollableTooltipSection.activeSection;
    }

    public void scroll(int step) {
        this.position = Math.min(Math.max(0, this.text.size() - MAX_LINES_SHOWN), Math.max(0, this.position + step));
    }

    public List<Text> getShownText() {
        return this.text.subList(this.position, Math.min(this.text.size(), this.position + MAX_LINES_SHOWN));
    }

    public List<Text> getShownTextAll() {
        List<Text> ret = new ArrayList<>();
        if(this.text.size() > MAX_LINES_SHOWN) { ret.add(this.startLine()); }
        ret.addAll(this.getShownText());
        if(this.text.size() > MAX_LINES_SHOWN) { ret.add(this.endLine()); }
        return ret;
    }

    private Text startLine() {
        MutableText valueText = MutableText.of(new PlainTextContent.Literal(Integer.toString(this.position)));
        return Text.translatable(TooltipHelper.SCROLLABLE_TOOLTIP_START, valueText).setStyle(Style.EMPTY.withColor(ModOption.DECORATION.getValue().getRGB()));
    }

    private Text endLine() {
        MutableText valueText = MutableText.of(new PlainTextContent.Literal(Integer.toString(Math.max(0, this.text.size() - MAX_LINES_SHOWN - this.position))));
        return Text.translatable(TooltipHelper.SCROLLABLE_TOOLTIP_END, valueText).setStyle(Style.EMPTY.withColor(ModOption.DECORATION.getValue().getRGB()));
    }
}
