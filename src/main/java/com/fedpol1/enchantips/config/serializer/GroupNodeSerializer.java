package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.config.tree.OptionNode;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class GroupNodeSerializer implements JsonSerializer<GroupNode> {

    public JsonElement serialize(GroupNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        JsonArray children = new JsonArray();
        Gson gson = new GsonBuilder().registerTypeAdapter(OptionNode.class, new OptionNodeSerializer()).create();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            children.add(gson.toJsonTree(current.getValue()));
        }
        json.addProperty("name", node.getName());
        json.add("children", children);
        return json;
    }
}
