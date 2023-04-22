package com.fedpol1.enchantips.config.deserializer;

import com.fedpol1.enchantips.config.ModConfigData;
import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfigTreeDeserializer implements JsonDeserializer<ConfigTree> {

    public ConfigTree deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        for(JsonElement current : json.getAsJsonArray()) {
            ConfigTreeDeserializer.deserializeCategory((JsonObject) current);
        }
        return null;
    }

    private static void deserializeCategory(JsonObject json) {
        CategoryNode cat = ModConfigData.getCategory(json.getAsJsonPrimitive("name").getAsString());
        for(JsonElement current : json.getAsJsonArray("children")) {
            JsonObject childObject = (JsonObject) current;
            if(childObject.has("value")) {
                ConfigTreeDeserializer.deserializeOption(childObject, cat);
            }
            if(childObject.has("children")) {
                ConfigTreeDeserializer.deserializeGroup(childObject, cat);
            }
        }
    }

    private static void deserializeGroup(JsonObject json, CategoryNode parent) {
        GroupNode group = (GroupNode) parent.getChild(json.getAsJsonPrimitive("name").getAsString());
        for (JsonElement current : json.getAsJsonArray("children")) {
            ConfigTreeDeserializer.deserializeOption((JsonObject) current, group);
        }
    }

    private static void deserializeOption(JsonObject json, AbstractNode parent) {
        OptionNode<?> opt = (OptionNode<?>) parent.getChild(json.getAsJsonPrimitive("name").getAsString());
        opt.getData().readStringValue(json.getAsJsonPrimitive("value").getAsString());
    }
}
