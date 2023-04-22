package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.config.tree.OptionNode;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class CategoryNodeSerializer implements JsonSerializer<CategoryNode> {

    public JsonElement serialize(CategoryNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        JsonArray children = new JsonArray();
        Gson groupGson = new GsonBuilder().registerTypeAdapter(GroupNode.class, new GroupNodeSerializer()).create();
        Gson optGson = new GsonBuilder().registerTypeAdapter(OptionNode.class, new OptionNodeSerializer()).create();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            Node child = current.getValue();
            if(child instanceof GroupNode) {
                children.add(groupGson.toJsonTree(child));
            }
            if(child instanceof OptionNode<?>) {
                children.add(optGson.toJsonTree(child));
            }
        }
        json.addProperty("name", node.getName());
        json.add("children", children);
        return json;
    }
}
