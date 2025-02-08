package com.fedpol1.enchantips.resources;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;

public class SymbolSetEntry {

    public static final Codec<SymbolSetEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SymbolSetEntryValue.ENTRY_CODEC.listOf().fieldOf("values").forGetter(SymbolSetEntry::getValues),
            Codecs.NON_NEGATIVE_INT.optionalFieldOf("minimum_matches", 1).forGetter(SymbolSetEntry::getMinimumMatches),
            Codec.either(Identifier.CODEC, TextCodecs.CODEC).flatXmap(
                    either -> either.map(
                            id -> {
                                    Text symbol = Symbols.get(id.getNamespace(), id.getPath());
                                    return symbol == null ?
                                            DataResult.error(() -> "Not a valid resource location: " + id) :
                                            DataResult.success(symbol);
                            },
                            DataResult::success
                    ),
                    symbol -> DataResult.success(Either.right(symbol))
            ).fieldOf("symbol").forGetter(SymbolSetEntry::getSymbol)
    ).apply(instance, SymbolSetEntry::new));

    private final ImmutableList<SymbolSetEntryValue.Entry> values;
    private final int minimumMatches;
    private final Text symbol;

    public SymbolSetEntry(List<SymbolSetEntryValue.Entry> values, int minimumMatches, Text symbol) {
        this.values = ImmutableList.copyOf(values);
        this.minimumMatches = minimumMatches;
        this.symbol = symbol;
    }

    public ImmutableList<SymbolSetEntryValue.Entry> getValues() {
        return this.values;
    }

    public int getMinimumMatches() {
        return this.minimumMatches;
    }

    public Text getSymbol() {
        return this.symbol;
    }

    public List<Identifier> applicable(List<Identifier> ids) {
        ArrayList<Identifier> applicable = new ArrayList<>();
        for(SymbolSetEntryValue.Entry entry : this.values) {
            if(ids.contains(entry.identifier()) ^ !entry.expected()) {
                applicable.add(entry.identifier());
            }
        }
        return applicable;
    }
}
