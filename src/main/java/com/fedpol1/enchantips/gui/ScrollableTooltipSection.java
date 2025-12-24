package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.TooltipHelper;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.PlainTextContents;

public class ScrollableTooltipSection {

    private static ScrollableTooltipSection activeSection; // there can only be one; handled screens set this to null every frame
    private static ScrollableTooltipSection previousNonEmpty;
    public static final ScrollableTooltipSection EMPTY = new ScrollableTooltipSection(new ArrayList<>(), 1);

    private final List<Component> text;
    private final int lineLimit;
    private int position;

    public ScrollableTooltipSection(List<Component> text, int limit) {
        this.text = text;
        this.position = 0;
        this.lineLimit = limit;
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
        this.position = Math.min(Math.max(0, this.text.size() - this.lineLimit), Math.max(0, this.position + step));
    }

    public List<Component> getShownText() {
        return this.text.subList(this.position, Math.min(this.text.size(), this.position + this.lineLimit));
    }

    public List<Component> getShownTextAll() {
        List<Component> ret = new ArrayList<>();
        if(this.text.size() > this.lineLimit) { ret.add(this.startLine()); }
        ret.addAll(this.getShownText());
        if(this.text.size() > this.lineLimit) { ret.add(this.endLine()); }
        return ret;
    }

    private Component startLine() {
        MutableComponent valueText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(this.position)));
        return Component.translatable(TooltipHelper.SCROLLABLE_TOOLTIP_START, valueText).setStyle(Style.EMPTY.withColor(ModOption.DECORATION.getValue().getRGB()));
    }

    private Component endLine() {
        MutableComponent valueText = MutableComponent.create(new PlainTextContents.LiteralContents(Integer.toString(Math.max(0, this.text.size() - this.lineLimit - this.position))));
        return Component.translatable(TooltipHelper.SCROLLABLE_TOOLTIP_END, valueText).setStyle(Style.EMPTY.withColor(ModOption.DECORATION.getValue().getRGB()));
    }
}
