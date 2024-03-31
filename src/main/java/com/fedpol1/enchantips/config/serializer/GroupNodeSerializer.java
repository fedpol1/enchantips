package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class GroupNodeSerializer implements JsonSerializer<GroupNode> {

    public JsonElement serialize(GroupNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            json.add(current.getKey(), ConfigTreeSerializer.gson.toJsonTree(current.getValue()));
        }
        return json;
    }
}
