package com.fedpol1.enchantips.config.serializer;

import com.fedpol1.enchantips.config.tree.OptionNode;
import com.google.gson.*;

import java.lang.reflect.Type;

public class OptionNodeSerializer implements JsonSerializer<OptionNode<?>> {

    public JsonElement serialize(OptionNode<?> node, Type typeOfNode, JsonSerializationContext context) {
        return new JsonPrimitive(node.getData().getStringValue());
    }
}
