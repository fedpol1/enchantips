package com.fedpol1.enchantips.config.data;

import com.fedpol1.enchantips.config.tree.OptionNode;
import dev.isxander.yacl3.api.ButtonOption;
import dev.isxander.yacl3.gui.YACLScreen;

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

    public void readStringValue(String s) {
        // empty
    }

    public void run (YACLScreen screen, ButtonOption button) {
        this.action.accept(screen, button);
        screen.close();
    }

    public ButtonOption getYaclOption(OptionNode<BiConsumer<YACLScreen, ButtonOption>> optionNode) {
        return ButtonOption.createBuilder()
                .action(this::run)
                .name(this.getOptionTitle(optionNode))
                .description(this.getOptionDescription(optionNode))
                .build();
    }
}
