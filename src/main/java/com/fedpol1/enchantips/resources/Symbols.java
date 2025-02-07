package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Symbols {

    public static Map<Identifier, Text> SYMBOLS = new HashMap<>();
    public static Map<Identifier, SymbolSet> SYMBOL_SETS = new HashMap<>();

    public static Text get(String namespace, String id) {
        return Symbols.SYMBOLS.get(Identifier.of(namespace, "symbols/" + id + ".json"));
    }

    public static Text get(String id) {
        return Symbols.get(EnchantipsClient.MODID, id);
    }

    public static SymbolSet getSet(String namespace, String id) {
        return Symbols.SYMBOL_SETS.get(Identifier.of(namespace, "symbol_sets/" + id + ".json"));
    }

    public static SymbolSet getSet(String id) {
        return Symbols.getSet(EnchantipsClient.MODID, id);
    }
}
