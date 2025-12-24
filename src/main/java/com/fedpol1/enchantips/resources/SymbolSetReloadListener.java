package com.fedpol1.enchantips.resources;

import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import java.util.Map;
import java.util.stream.Collectors;

public class SymbolSetReloadListener extends SimpleJsonResourceReloadListener<SymbolSet> {

    public static final String DIRECTORY = "symbol_sets";
    private static final FileToIdConverter FINDER = FileToIdConverter.json(DIRECTORY);
    private Map<ResourceKey<SymbolSet>, SymbolSet> registry = Map.of();

    public SymbolSetReloadListener() {
        super(SymbolSet.CODEC, FINDER);
    }

    protected void apply(Map<Identifier, SymbolSet> map, ResourceManager resourceManager, ProfilerFiller profiler) {
        this.registry = map.entrySet().stream().collect(
                Collectors.toUnmodifiableMap(
                        entry -> ResourceKey.create(SymbolSet.REGISTRY, entry.getKey()), Map.Entry::getValue
                )
        );
    }

    public SymbolSet get(ResourceKey<SymbolSet> key) {
        return this.registry.getOrDefault(key, SymbolSet.DEFAULT);
    }
}