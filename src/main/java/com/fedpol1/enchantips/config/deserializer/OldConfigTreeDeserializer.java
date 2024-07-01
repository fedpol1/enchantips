package com.fedpol1.enchantips.config.deserializer;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.HashMap;

public class OldConfigTreeDeserializer implements JsonDeserializer<ConfigTree> {

    private static final HashMap<String, ModOption<?>> OLD_TO_NEW = new HashMap<>();

    static {
        OLD_TO_NEW.put("tooltips.toggles.repair_cost", ModOption.REPAIR_COST_SWITCH);
        OLD_TO_NEW.put("tooltips.toggles.enchantability", ModOption.ENCHANTABILITY_SWITCH);
        OLD_TO_NEW.put("tooltips.toggles.enchantability.when_enchanted", ModOption.ENCHANTABILITY_SWITCH_WHEN_ENCHANTED);
        OLD_TO_NEW.put("tooltips.toggles.rarity", ModOption.ANVIL_COST_SWITCH);
        OLD_TO_NEW.put("tooltips.toggles.modified_level", ModOption.MODIFIED_ENCHANTING_POWER_SWITCH);
        OLD_TO_NEW.put("tooltips.toggles.extra_enchantments", ModOption.EXTRA_ENCHANTMENTS_SWITCH);
        OLD_TO_NEW.put("tooltips.colors.repair_cost", ModOption.REPAIR_COST_COLOR);
        OLD_TO_NEW.put("tooltips.colors.repair_cost.value", ModOption.REPAIR_COST_VALUE_COLOR);
        OLD_TO_NEW.put("tooltips.colors.enchantability", ModOption.ENCHANTABILITY_COLOR);
        OLD_TO_NEW.put("tooltips.colors.enchantability.value", ModOption.ENCHANTABILITY_VALUE_COLOR);
        OLD_TO_NEW.put("tooltips.colors.decoration", ModOption.DECORATION);
        OLD_TO_NEW.put("tooltips.colors.modified_level", ModOption.MODIFIED_ENCHANTING_POWER_COLOR);
        OLD_TO_NEW.put("tooltips.colors.modified_level.value", ModOption.MODIFIED_ENCHANTING_POWER_VALUE_COLOR);
        OLD_TO_NEW.put("highlights.show", ModOption.HIGHLIGHTS_SWITCH);
        OLD_TO_NEW.put("highlights.respect_hideflags", ModOption.HIGHLIGHTS_SWITCH_OVERRIDE);
        OLD_TO_NEW.put("highlights.limit", ModOption.HIGHLIGHTS_LIMIT);
        OLD_TO_NEW.put("highlights.hotbar_alpha", ModOption.HIGHLIGHTS_ALPHA_HOTBAR);
        OLD_TO_NEW.put("highlights.trading_alpha", ModOption.HIGHLIGHTS_ALPHA_TRADING);
        OLD_TO_NEW.put("miscellaneous.actions.action_color", ModOption.ACTION_COLOR);
        OLD_TO_NEW.put("miscellaneous.show_anvil_swap_button", ModOption.ANVIL_SWAP_BUTTON_SWITCH);
        OLD_TO_NEW.put("miscellaneous.show_anvil_warning", ModOption.ANVIL_SWAP_WARNING_SWITCH);
        OLD_TO_NEW.put("miscellaneous.unbreakable_color", ModOption.UNBREAKABLE_COLOR);
        OLD_TO_NEW.put("miscellaneous.highlight_unbreakable", ModOption.UNBREAKABLE_HIGHLIGHT);
        OLD_TO_NEW.put("miscellaneous.prioritize_overmax_enchantments", ModOption.PRIORITIZE_OVERLEVELLED_ENCHANTMENTS);
    }

    public ConfigTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        for(JsonElement current : json.getAsJsonArray()) {
            OldConfigTreeDeserializer.deserializeCategory((JsonObject) current);
        }
        return null;
    }

    private static void deserializeCategory(JsonObject json) {
        CategoryNode cat = (CategoryNode) ConfigTree.root.getChild(json.getAsJsonPrimitive("name").getAsString());
        String p = json.getAsJsonPrimitive("name").getAsString();
        for(JsonElement current : json.getAsJsonArray("children")) {
            JsonObject childObject = (JsonObject) current;
            if(childObject.has("value")) {
                OldConfigTreeDeserializer.deserializeOption(childObject, cat, p);
            }
            if(childObject.has("children")) {
                OldConfigTreeDeserializer.deserializeGroup(childObject, cat, p);
            }
        }
    }

    private static void deserializeGroup(JsonObject json, CategoryNode parent, String path) {
        GroupNode group = parent == null ? null : (GroupNode) parent.getChild(json.getAsJsonPrimitive("name").getAsString());
        String p = path + "." + json.getAsJsonPrimitive("name").getAsString();
        for (JsonElement current : json.getAsJsonArray("children")) {
            OldConfigTreeDeserializer.deserializeOption((JsonObject) current, group, p);
        }
    }

    private static void deserializeOption(JsonObject json, Node parent, String path) {
        OptionNode<?> opt = parent == null ? null : (OptionNode<?>) parent.getChild(json.getAsJsonPrimitive("name").getAsString());
        if(opt != null) {
            opt.getData().readStringValue(json.getAsJsonPrimitive("value").getAsString());
        }
        String p = path + "." + json.getAsJsonPrimitive("name").getAsString();
        String val = json.getAsJsonPrimitive("value").getAsString();
        if(OLD_TO_NEW.containsKey(p)) {
            OLD_TO_NEW.get(p).getData().readStringValue(val);
        }
    }
}
