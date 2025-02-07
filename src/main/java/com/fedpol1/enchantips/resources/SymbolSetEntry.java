package com.fedpol1.enchantips.resources;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class SymbolSetEntry {

    public static final Codec<SymbolSetEntry> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            SymbolSetEntryValue.ENTRY_CODEC.listOf().fieldOf("values").forGetter(SymbolSetEntry::getValues),
            TextCodecs.CODEC.fieldOf("symbol").forGetter(SymbolSetEntry::getSymbol)
    ).apply(instance, SymbolSetEntry::new));

    private final ImmutableList<SymbolSetEntryValue.Entry> values;
    private final Text symbol;

    public SymbolSetEntry(List<SymbolSetEntryValue.Entry> values, Text symbol) {
        this.values = ImmutableList.copyOf(values);
        this.symbol = symbol;
    }

    public ImmutableList<SymbolSetEntryValue.Entry> getValues() {
        return this.values;
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
