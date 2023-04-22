package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigTreeSerializer implements JsonSerializer<AbstractNode> {

    public JsonElement serialize(AbstractNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonArray children = new JsonArray();
        Gson gson = new GsonBuilder().registerTypeAdapter(CategoryNode.class, new CategoryNodeSerializer()).create();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            children.add(gson.toJsonTree(current.getValue()));
        }
        return children;
    }
}
