package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class CategoryNodeSerializer implements JsonSerializer<CategoryNode> {

    public JsonElement serialize(CategoryNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            json.add(current.getKey(), ConfigTreeSerializer.gson.toJsonTree(current.getValue()));
        }
        return json;
    }
}
