package com.fedpol1.enchantips.config.deserializer;

import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigTreeDeserializer implements JsonDeserializer<ConfigTree> {

    public ConfigTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        ConfigTreeDeserializer.deserializeImpl(json, ConfigTree.root);
        return null;
    }

    private static void deserializeImpl(JsonElement json, Node node) {
        for(Map.Entry<String, JsonElement> current : json.getAsJsonObject().asMap().entrySet()) {
            Node child = node.getChild(current.getKey());
            if(child instanceof OptionNode<?>) {
                ((OptionNode<?>) child).getData().readStringValue(current.getValue().getAsString());
            }
            else {
                ConfigTreeDeserializer.deserializeImpl(current.getValue(), child);
            }
        }
    }
}
