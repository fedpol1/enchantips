package com.fedpol1.enchantips.resources;

import com.mojang.serialization.Codec;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class SymbolSet {

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

    public List<Text> getApplicableSymbols(List<Identifier> ids, Text miscSymbol) {
        ArrayList<Text> applied = new ArrayList<>();
        Set<Identifier> miscValues = new HashSet<>(ids);
        for(SymbolSetEntry entry : this.entries) {
            List<Identifier> applicable = entry.applicable(ids);
            if(!applicable.isEmpty()) {
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
}
