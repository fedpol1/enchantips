package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.fedpol1.enchantips.gui.widgets.info_line.ConfigInfoLine;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.gui.YACLScreen;
import net.minecraft.text.Text;

import java.util.List;
import java.util.function.BiConsumer;

public class ActionOption implements Data<BiConsumer<YACLScreen, ButtonOption>> {

    private BiConsumer<YACLScreen, ButtonOption> action;

    public ActionOption(BiConsumer<YACLScreen, ButtonOption> action) {
        this.action = action;
    }

    public BiConsumer<YACLScreen, ButtonOption> getValue() {
        return this.getDefaultValue();
    }

    public String getStringValue() {
        return "";
    }

    public BiConsumer<YACLScreen, ButtonOption> getDefaultValue() {
        // this should never be null
        return this.action;
    }

    public void setValue(BiConsumer<YACLScreen, ButtonOption> action) {
        this.action = action;
    }

    public boolean canSet(BiConsumer<YACLScreen, ButtonOption> action) {
        return false;
    }

    public void readStringValue(String s) {
        // empty
    }

    public void run (YACLScreen screen, ButtonOption button) {
        this.action.accept(screen, button);
        screen.close();
    }

    public List<Text> getSaveTooltip(BiConsumer<YACLScreen, ButtonOption> v) {
        return null;
    }

    public ConfigInfoLine<BiConsumer<YACLScreen, ButtonOption>> getConfigLine(OptionNode<BiConsumer<YACLScreen, ButtonOption>> optionNode) {
        return null;
    }

    public ButtonOption getYaclOption(OptionNode<BiConsumer<YACLScreen, ButtonOption>> optionNode) {
        return ButtonOption.createBuilder()
                .action(this::run)
                .name(this.getOptionTitle(optionNode))
                .description(this.getOptionDescription(optionNode))
                .build();
    }
}
