package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.resource.*;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.stream.Collectors;

public class SymbolSetReloadListener extends JsonDataLoader<SymbolSet> {

    public static final String DIRECTORY = "symbol_sets";
    private static final ResourceFinder FINDER = ResourceFinder.json(DIRECTORY);
    private Map<RegistryKey<SymbolSet>, SymbolSet> registry = Map.of();

    public SymbolSetReloadListener() {
        super(SymbolSet.CODEC, FINDER);
    }

    protected void apply(Map<Identifier, SymbolSet> map, ResourceManager resourceManager, Profiler profiler) {
        this.registry = map.entrySet().stream().collect(
                Collectors.toUnmodifiableMap(
                        entry -> RegistryKey.of(SymbolSet.REGISTRY, entry.getKey()), Map.Entry::getValue
                )
        );
    }

    public SymbolSet get(RegistryKey<SymbolSet> key) {
        return this.registry.getOrDefault(key, SymbolSet.DEFAULT);
    }
}