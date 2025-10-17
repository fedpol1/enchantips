package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.*;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.stream.Collectors;

public class SymbolReloadListener extends JsonDataLoader<Text> {

    public static final String DIRECTORY = "symbols";
    private static final ResourceFinder FINDER = ResourceFinder.json(DIRECTORY);
    private Map<RegistryKey<Text>, Text> symbols = Map.of();

    public SymbolReloadListener() {
        super(TextCodecs.CODEC, FINDER);
    }

    protected void apply(Map<Identifier, Text> map, ResourceManager resourceManager, Profiler profiler) {
        this.symbols = map.entrySet().stream().collect(
                Collectors.toUnmodifiableMap(
                        entry -> RegistryKey.of(Symbols.REGISTRY, entry.getKey()), Map.Entry::getValue
                )
        );
    }

    public Text get(RegistryKey<Text> key) {
        return this.symbols.get(key);
    }
}