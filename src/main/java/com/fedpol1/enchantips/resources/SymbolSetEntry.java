package com.fedpol1.enchantips.resources;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.ArrayList;
import java.util.List;

public class SymbolSetEntry {

    public static final Codec<SymbolSetEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SymbolSetEntryValue.ENTRY_CODEC.listOf().fieldOf("values").forGetter(SymbolSetEntry::getValues),
            Codecs.NON_NEGATIVE_INT.optionalFieldOf("minimum_matches", 1).forGetter(SymbolSetEntry::getMinimumMatches),
            Identifier.CODEC.fieldOf("symbol").forGetter(SymbolSetEntry::getSymbol)
    ).apply(instance, SymbolSetEntry::new));

    private final ImmutableList<SymbolSetEntryValue.Entry> values;
    private final int minimumMatches;
    private final Identifier symbol;

    public SymbolSetEntry(List<SymbolSetEntryValue.Entry> values, int minimumMatches, Identifier symbol) {
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

    public Identifier getSymbol() {
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
