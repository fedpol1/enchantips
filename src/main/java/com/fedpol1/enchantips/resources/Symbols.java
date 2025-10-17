package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class Symbols {

    public static RegistryKey<? extends Registry<Text>> REGISTRY = RegistryKey.ofRegistry(
            Identifier.of(EnchantipsClient.MODID, SymbolReloadListener.DIRECTORY)
    );

    public static Text get(String namespace, String id) {
        return EnchantipsClient.symbolReloadListener.get(RegistryKey.of(Symbols.REGISTRY, Identifier.of(namespace, id)));
    }

    public static Text get(String id) {
        return Symbols.get(EnchantipsClient.MODID, id);
    }

    public static SymbolSet getSet(String namespace, String id) {
        return EnchantipsClient.symbolSetReloadListener.get(RegistryKey.of(SymbolSet.REGISTRY, Identifier.of(namespace, id)));
    }

    public static SymbolSet getSet(String id) {
        return Symbols.getSet(EnchantipsClient.MODID, id);
    }

    public static RegistryKey<Text> symbolRegistryKey(String s) {
        return RegistryKey.of(Symbols.REGISTRY, Identifier.of(EnchantipsClient.MODID, s));
    }
}
