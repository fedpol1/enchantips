package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.*;
import com.google.gson.*;

import java.lang.reflect.Type;

public class ConfigTreeSerializer implements JsonSerializer<AbstractNode> {
    @Override
    public JsonElement serialize(AbstractNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        for(int i = 0; i < node.getNumChildren(); i++) {
            CategoryNode child = (CategoryNode) node.getChild(i);
            Gson gson = new GsonBuilder().registerTypeAdapter(CategoryNode.class, new CategoryNodeSerializer()).create();
            json.add(child.getName(), gson.toJsonTree(child));
        }
        return json;
    }
}
