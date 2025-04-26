package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.widgets.info_line.BooleanConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ColorConfigInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class ConfigScreen extends BaseScreen {

    public ConfigScreen(Text title, @Nullable Screen parent) {
        super(title, parent);
        this.lines = new ScrollableInfoLineContainer(0x404040, 6);

        this.lines.addLine(new BooleanConfigInfoLine(Text.literal("among us"), ModOption.ENCHANTABILITY_SWITCH_WHEN_ENCHANTED, true));
        this.lines.addLine(new BooleanConfigInfoLine(Text.literal("sussy"), ModOption.ANVIL_COST_SWITCH, true));
        this.lines.addLine(new BooleanConfigInfoLine(Text.literal("among us sussy"), ModOption.ENCHANTMENT_TARGETS_SWITCH, false));
        this.lines.addLine(new ColorConfigInfoLine(Text.literal("impostor"), ModOption.ENCHANTABILITY_COLOR, new Color(0xff0000)));
        this.lines.addLine(new ColorConfigInfoLine(Text.literal("crewmate"), ModOption.ENCHANTABILITY_VALUE_COLOR, new Color(0x00ff00)));
    }
}
