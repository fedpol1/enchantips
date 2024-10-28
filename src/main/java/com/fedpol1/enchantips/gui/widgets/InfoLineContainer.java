package com.fedpol1.enchantips.gui.widgets;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

import java.util.ArrayList;

public class InfoLineContainer extends InfoDelineator implements Drawable {

    protected int x;
    protected int y;
    protected int width;
    protected final ArrayList<InfoDelineator> lines;

    public InfoLineContainer() {
        super();
        this.lines = new ArrayList<>();
    }

    public void addLine(Text t) {
        this.addLine(new InfoLine(t));
    }

    public void addLine(InfoDelineator line) {
        line.parent = this;
        line.nearestScrollableParent = this.nearestScrollableParent;
        if(line instanceof CollapsibleInfoLine collapsible) {
            collapsible.lines.parent = this;
            collapsible.lines.nearestScrollableParent = this.nearestScrollableParent;
        }
        this.lines.add(line);
    }

    public int getHeight(int index) {
        int sum = 0;
        for(int i = 0; i < this.lines.size(); i++) {
            if(i >= index) { return sum; }
            sum += this.lines.get(i).getHeight(this.lines.size());
        }
        return sum;
    }

    public int getHeight() {
        return this.getHeight(this.lines.size());
    }

    public InfoDelineator getLast() {
        InfoDelineator last = this.lines.getLast();
        if(last instanceof InfoLineContainer container) {
            return container.getLast();
        }
        if(last instanceof CollapsibleInfoLine collapsible) {
            if(collapsible.collapsed) {
                return collapsible;
            }
            return collapsible.lines.getLast();
        }
        return last;
    }

    public void refresh(int index) {
        this.x = this.parent.x + InfoDelineator.INDENTATION;
        this.y = this.parent.y + this.parent.getHeight(index) + InfoDelineator.LINE_HEIGHT;
        if(this.parent == this.nearestScrollableParent) { this.y += this.nearestScrollableParent.scrollHeight; }
        this.width = this.parent.width - InfoDelineator.INDENTATION;
        this.height = this.getHeight();
        for (int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(i);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for(InfoDelineator info : lines) {
            if(info.nearestScrollableParent == null) {
                EnchantipsClient.LOGGER.info("a");
            }
            if(info.y < info.nearestScrollableParent.y) { continue; }
            if(info.y + info.height > info.nearestScrollableParent.y + info.nearestScrollableParent.height) { continue; }
            info.render(context, mouseX, mouseY, delta);
        }
        context.drawHorizontalLine(this.x, this.x + width, this.y, 0xff000000);
        context.drawHorizontalLine(this.x, this.x + width, this.y + this.height, 0xff000000);
        context.drawVerticalLine(this.x, this.y, this.y + this.height, 0xff000000);
        context.drawVerticalLine(this.x + this.width, this.y, this.y + this.height, 0xff000000);
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for(InfoDelineator line : lines) {
            line.mouseClicked(mouseX, mouseY, button);
        }
        return true;
    }
}
