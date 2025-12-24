package com.fedpol1.enchantips.resources;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;

public record SymbolSetEntry(Map<Identifier, Boolean> values, int minimumMatches, Identifier symbol) {

    public static final Codec<SymbolSetEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.unboundedMap(Identifier.CODEC, Codec.BOOL).fieldOf("values").forGetter(SymbolSetEntry::values),
            ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("minimum_matches", 1).forGetter(SymbolSetEntry::minimumMatches),
            Identifier.CODEC.fieldOf("symbol").forGetter(SymbolSetEntry::symbol)
    ).apply(instance, SymbolSetEntry::new));

    public SymbolSetEntry(Map<Identifier, Boolean> values, int minimumMatches, Identifier symbol) {
        this.values = Map.copyOf(values);
        this.minimumMatches = minimumMatches;
        this.symbol = symbol;
    }

    public List<Identifier> applicable(List<Identifier> ids) {
        ArrayList<Identifier> applicable = new ArrayList<>();
        for (Map.Entry<Identifier, Boolean> entry : this.values.entrySet()) {
            if (ids.contains(entry.getKey()) ^ !entry.getValue()) {
                applicable.add(entry.getKey());
            }
        }
        return applicable;
    }
}
