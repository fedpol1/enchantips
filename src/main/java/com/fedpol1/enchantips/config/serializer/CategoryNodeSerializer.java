package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.CategoryNode;
import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.config.tree.OptionNode;
import com.google.gson.*;

import java.lang.reflect.Type;

public class CategoryNodeSerializer implements JsonSerializer<CategoryNode> {

    public JsonElement serialize(CategoryNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        Node child;
        for(int i = 0; i < node.getNumChildren(); i++) {
            child = node.getChild(i);
            if(child instanceof OptionNode<?> opt) {
                json.addProperty(opt.getName(), opt.getData().getStringValue());
                continue;
            }
            if(child instanceof GroupNode group) {
                Gson gson = new GsonBuilder().registerTypeAdapter(GroupNode.class, new GroupNodeSerializer()).create();
                json.add(group.getName(), gson.toJsonTree(group));
            }
        }
        return json;
    }
}
