package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class OptionNodeSerializer implements JsonSerializer<OptionNode<?>> {

    public JsonElement serialize(OptionNode<?> node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        json.addProperty("name", node.getName());
        json.addProperty("value", node.getData().getStringValue());
        return json;
    }
}
