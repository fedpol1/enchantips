package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.tree.EnchantmentGroupNode;
import com.fedpol1.enchantips.config.tree.Node;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.Map;

public class EnchantmentGroupNodeSerializer implements JsonSerializer<EnchantmentGroupNode> {

    public JsonElement serialize(EnchantmentGroupNode node, Type typeOfNode, JsonSerializationContext context) {
        EnchantipsClient.LOGGER.info("! " + node.getFullName());
        JsonObject json = new JsonObject();
        JsonArray children = new JsonArray();
        for(Map.Entry<String, Node> current : node.getChildren()) {
            children.add(ConfigTreeSerializer.gson.toJsonTree(current.getValue()));
        }
        json.addProperty("name", node.getName());
        json.add("children", children);
        return json;
    }
}
