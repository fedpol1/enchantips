package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

// handles all node types except OptionNode
public class ConfigTreeSerializer implements JsonSerializer<Node> {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(CategoryNode.class, new ConfigTreeSerializer())
            .registerTypeAdapter(GroupNode.class, new ConfigTreeSerializer())
            .registerTypeAdapter(EnchantmentGroupNode.class, new ConfigTreeSerializer())
            .registerTypeAdapter(OptionNode.class, new OptionNodeSerializer())
            .create();

    public JsonElement serialize(Node node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            json.add(current.getKey(), ConfigTreeSerializer.gson.toJsonTree(current.getValue()));
        }
        return json;
    }
}
