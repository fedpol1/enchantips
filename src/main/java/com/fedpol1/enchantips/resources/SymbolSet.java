package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class SymbolSet {

    public static final SymbolSet DEFAULT = new SymbolSet(
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            Optional.empty(),
            List.of()
    );
    public static ResourceKey<? extends Registry<SymbolSet>> REGISTRY = ResourceKey.createRegistryKey(
            EnchantipsClient.id(SymbolSetReloadListener.DIRECTORY)
    );

    public static final Codec<SymbolSet> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.optionalFieldOf("all").forGetter(ss -> ss.allSymbol),
            Identifier.CODEC.optionalFieldOf("none").forGetter(ss -> ss.noneSymbol),
            Identifier.CODEC.optionalFieldOf("miscellaneous").forGetter(ss -> ss.miscSymbol),
            Identifier.CODEC.optionalFieldOf("unknown").forGetter(ss -> ss.noneSymbol),
            SymbolSetEntry.CODEC.listOf().fieldOf("entries").forGetter(SymbolSet::getEntries)
    ).apply(instance, SymbolSet::new));

    public Optional<Identifier> allSymbol;
    public Optional<Identifier> noneSymbol;
    public Optional<Identifier> miscSymbol;
    public Optional<Identifier> unknownSymbol;
    public List<SymbolSetEntry> entries;

    public SymbolSet(
            Optional<Identifier> allSymbol,
            Optional<Identifier> noneSymbol,
            Optional<Identifier> miscSymbol,
            Optional<Identifier> unknownSymbol,
            List<SymbolSetEntry> entries
    ) {
        this.allSymbol = allSymbol;
        this.noneSymbol = noneSymbol;
        this.miscSymbol = miscSymbol;
        this.unknownSymbol = unknownSymbol;
        this.entries = entries;
    }

    public List<SymbolSetEntry> getEntries() {
        return this.entries;
    }

    public List<Identifier> getApplicableSymbols(List<Identifier> ids, Optional<Identifier> miscSymbol) {
        ArrayList<Identifier> applied = new ArrayList<>();
        Set<Identifier> miscValues = new HashSet<>(ids);
        for(SymbolSetEntry entry : this.entries) {
            List<Identifier> applicable = entry.applicable(ids);
            if(applicable.size() >= entry.minimumMatches()) {
                if(!applied.contains((entry.symbol()))) {
                    applied.add(entry.symbol());
                }
                applicable.forEach(miscValues::remove);
            }
        }
        if(!miscValues.isEmpty() && miscSymbol.isPresent()) {
            applied.add(miscSymbol.get());
        }
        return applied;
    }

    public static ResourceKey<SymbolSet> of(String id) {
        return ResourceKey.create(REGISTRY, EnchantipsClient.id(id));
    }
}
