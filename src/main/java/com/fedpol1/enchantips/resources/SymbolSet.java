package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import com.mojang.serialization.Codec;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.util.*;

public class SymbolSet {

    public static final SymbolSet DEFAULT = new SymbolSet(List.of());
    public static RegistryKey<? extends Registry<SymbolSet>> REGISTRY = RegistryKey.ofRegistry(
            EnchantipsClient.id(SymbolSetReloadListener.DIRECTORY)
    );

    public static final Codec<SymbolSet> CODEC = SymbolSetEntry.CODEC.listOf().xmap(
            SymbolSet::new,
            SymbolSet::getEntries
    );

    public List<SymbolSetEntry> entries;

    public SymbolSet(List<SymbolSetEntry> entries) {
        this.entries = entries;
    }

    public List<SymbolSetEntry> getEntries() {
        return this.entries;
    }

    public List<Identifier> getApplicableSymbols(List<Identifier> ids, Identifier miscSymbol) {
        ArrayList<Identifier> applied = new ArrayList<>();
        Set<Identifier> miscValues = new HashSet<>(ids);
        for(SymbolSetEntry entry : this.entries) {
            List<Identifier> applicable = entry.applicable(ids);
            if(applicable.size() >= entry.getMinimumMatches()) {
                if(!applied.contains((entry.getSymbol()))) {
                    applied.add(entry.getSymbol());
                }
                applicable.forEach(miscValues::remove);
            }
        }
        if(!miscValues.isEmpty()) {
            applied.add(miscSymbol);
        }
        return applied;
    }

    public static RegistryKey<SymbolSet> of(String id) {
        return RegistryKey.of(REGISTRY, EnchantipsClient.id(id));
    }
}
