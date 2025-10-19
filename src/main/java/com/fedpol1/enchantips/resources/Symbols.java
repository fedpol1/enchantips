package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Style;
import net.minecraft.text.StyleSpriteSource;
import net.minecraft.text.Text;
import net.minecraft.text.object.AtlasTextObjectContents;
import net.minecraft.util.Identifier;

public class Symbols {

    public static final String DIRECTORY = "symbols";
    public static Text SPACE = Text.literal(" ").setStyle(
            Style.EMPTY.withFont(new StyleSpriteSource.Font(EnchantipsClient.id("symbols")))
    );

    public static Text get(Identifier id) {
        return Text.object(
                new AtlasTextObjectContents(
                        Identifier.of("gui"), Identifier.of(id.getNamespace(), DIRECTORY + "/" + id.getPath())
                )
        );
    }

    public static SymbolSet getSet(String namespace, String id) {
        return EnchantipsClient.symbolSetReloadListener.get(RegistryKey.of(SymbolSet.REGISTRY, Identifier.of(namespace, id)));
    }

    public static SymbolSet getSet(String id) {
        return Symbols.getSet(EnchantipsClient.MODID, id);
    }
}
