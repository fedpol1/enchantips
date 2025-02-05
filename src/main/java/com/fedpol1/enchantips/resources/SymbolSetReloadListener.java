package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import com.google.gson.*;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;
import net.minecraft.resource.ResourceManager;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.LinkedHashMap;

public class SymbolSetReloadListener implements SimpleSynchronousResourceReloadListener {

    @Override
    public Identifier getFabricId() {
        return Identifier.of(EnchantipsClient.MODID, "symbol_set");
    }

    @Override
    public void reload(ResourceManager manager) {
        try {
            BufferedReader reader = manager.openAsReader(Identifier.of(EnchantipsClient.MODID, "symbol_set.json"));
            JsonObject json = JsonParser.parseReader(reader).getAsJsonObject();
            reader.close();
            this.populate(json);
        } catch (IOException | JsonSyntaxException e) {
            EnchantipsClient.LOGGER.warn("Failed to load Enchantips symbol set.{}", e.getMessage());
        }
    }

    private void populate(JsonObject json) {
        Symbols.ALL_SYMBOL = deserializeText(json.getAsJsonObject("all"));
        Symbols.NONE_SYMBOL = deserializeText(json.getAsJsonObject("none"));
        Symbols.MISCELLANEOUS_SYMBOL = deserializeText(json.getAsJsonObject("miscellaneous"));
        Symbols.UNKNOWN_SYMBOL = deserializeText(json.getAsJsonObject("unknown"));
        Symbols.ANVIL_SYMBOL = deserializeText(json.getAsJsonObject("anvil"));
        Symbols.SWATCH_SYMBOL = deserializeText(json.getAsJsonObject("swatch"));

        Symbols.ITEM_SYMBOLS = new LinkedHashMap<>();
        for(JsonElement itemsSymbols : json.getAsJsonArray("items")) {
            Text symbol = deserializeText(((JsonObject) itemsSymbols).getAsJsonObject("symbol"));
            for(JsonElement item : ((JsonObject) itemsSymbols).getAsJsonArray("items")) {
                Symbols.ITEM_SYMBOLS.put(
                        Identifier.tryParse(item.getAsString()), symbol
                );
            }
        }
    }

    private static Text deserializeText(JsonObject json) {
        Gson gson = new Gson();
        return TextCodecs.CODEC
                .decode(JsonOps.INSTANCE, gson.fromJson(json, JsonElement.class))
                .getOrThrow()
                .getFirst();
    }
}