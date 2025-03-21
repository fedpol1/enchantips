package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class SymbolSetReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return Identifier.of(EnchantipsClient.MODID, "symbol_set");
    }

    @Override
    public void reload(ResourceManager manager) {
        Symbols.SYMBOL_SETS = new HashMap<>();

        Map<Identifier, Resource> resources = manager.findResources(
                "symbol_sets",
                path -> path.toString().endsWith(".json")
        );

        for(Map.Entry<Identifier, Resource> entry : resources.entrySet()) {
            try(BufferedReader reader = entry.getValue().getReader()) {
                Optional<String> contents = reader.lines().reduce((a, b) -> a + b);
                Gson gson = new Gson();
                if(contents.isEmpty()) continue;
                Symbols.SYMBOL_SETS.put(
                        entry.getKey(),
                        SymbolSet.CODEC
                                .decode(JsonOps.INSTANCE, gson.fromJson(contents.get(), JsonElement.class))
                                .getOrThrow().getFirst()
                );
            } catch(Exception e) {
                EnchantipsClient.LOGGER.error("Error occurred while loading resource json {}", entry.getKey().toString(), e);
            }
        }
    }
}