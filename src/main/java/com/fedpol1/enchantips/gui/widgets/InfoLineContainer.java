package com.fedpol1.enchantips.gui.widgets;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;

public class InfoLineContainer extends InfoDelineator implements Drawable {

    private int x;
    private int y;
    private int width;
    private int height;
    private int scrollPos;
    private final ArrayList<InfoDelineator> lines;

    public InfoLineContainer(int x, int y, int width, int height, InfoDelineator... lines) {
        super(x, y, width);
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.scrollPos = 0;
        this.lines = new ArrayList<>(List.of(lines));
    }

    public void addLine(Text t) {
        this.lines.add(new InfoLine(this.x, this.y + this.lines.size() * InfoDelineator.LINE_HEIGHT, this.width, t));
    }

    public void refresh(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        for(int i = 0; i < this.lines.size(); i++) {
            this.lines.get(i).refresh(x + INDENTATION, y + i * LINE_HEIGHT, width, height);
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        for(InfoDelineator info : lines) {
            info.render(context, mouseX, mouseY, delta);
        }
    }
}
