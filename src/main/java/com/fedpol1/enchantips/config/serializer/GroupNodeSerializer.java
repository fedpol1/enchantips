package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.GroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.fedpol1.enchantips.config.tree.OptionNode;
import com.google.gson.*;

import java.lang.reflect.Type;

public class GroupNodeSerializer implements JsonSerializer<GroupNode> {

    public JsonElement serialize(GroupNode node, Type typeOfNode, JsonSerializationContext context) {
        JsonObject json = new JsonObject();
        OptionNode<?> child;
        for(int i = 0; i < node.getNumChildren(); i++) {
            child = (OptionNode<?>) node.getChild(i);
            json.addProperty(child.getName(), child.getData().getStringValue());
        }
        return json;
    }
}
