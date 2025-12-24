package com.fedpol1.enchantips.config.deserializer;

import com.fedpol1.enchantips.config.ModCategory;
import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.TreeMap;

public class ConfigTreeDeserializer implements JsonDeserializer<ConfigTree> {

    public ConfigTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ConfigTreeDeserializer.deserializeImpl(json, ConfigTree.root);
        return null;
    }

    private static void deserializeImpl(JsonElement json, Node node) {
        for(Map.Entry<String, JsonElement> current : json.getAsJsonObject().asMap().entrySet()) {
            Node child = node.getChild(current.getKey());
            if(child == null) { continue; }
            if(child == ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode()) {
                ConfigTreeDeserializer.deserializeEnchantments(current.getValue(), child);
            } else if(child instanceof OptionNode<?>) {
                ((OptionNode<?>) child).getData().readStringValue(current.getValue().getAsString());
            } else {
                ConfigTreeDeserializer.deserializeImpl(current.getValue(), child);
            }
        }
    }

    private static void deserializeEnchantments(JsonElement json, Node node) {
        if(node != ModCategory.INDIVIDUAL_ENCHANTMENTS.getNode()) { throw new IllegalStateException("Found enchantment outside of individual_enchantments."); }
        TreeMap<String, JsonElement> enchantments = new TreeMap<>(json.getAsJsonObject().asMap());
        for(Map.Entry<String, JsonElement> current : enchantments.entrySet()) {
            EnchantmentGroupNode group = ((CategoryNode) node).addEnchantmentGroup(current.getKey());
            ConfigTreeDeserializer.deserializeImpl(current.getValue(), group);
        }
    }
}
