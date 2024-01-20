package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class ConfigTreeSerializer implements JsonSerializer<Node> {

    public static Gson gson = new GsonBuilder()
            .registerTypeAdapter(CategoryNode.class, new CategoryNodeSerializer())
            .registerTypeAdapter(GroupNode.class, new GroupNodeSerializer())
            .registerTypeAdapter(EnchantmentGroupNode.class, new EnchantmentGroupNodeSerializer())
            .registerTypeAdapter(OptionNode.class, new OptionNodeSerializer())
            .create();

    public JsonElement serialize(Node node, Type typeOfNode, JsonSerializationContext context) {
        JsonArray children = new JsonArray();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            children.add(ConfigTreeSerializer.gson.toJsonTree(current.getValue()));
        }
        return children;
    }
}
