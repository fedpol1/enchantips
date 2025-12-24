package com.fedpol1.enchantips.resources;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FontDescription;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.contents.objects.AtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

public class Symbols {

    public static final String DIRECTORY = "symbols";
    public static Component SPACE = Component.literal(" ").setStyle(
            Style.EMPTY.withFont(new FontDescription.Resource(EnchantipsClient.id("symbols")))
    );

    public static Component get(Identifier id) {
        return Component.object(
                new AtlasSprite(
                        Identifier.parse("gui"), Identifier.fromNamespaceAndPath(id.getNamespace(), DIRECTORY + "/" + id.getPath())
                )
        );
    }

    public static SymbolSet getSet(String namespace, String id) {
        return EnchantipsClient.symbolSetReloadListener.get(ResourceKey.create(SymbolSet.REGISTRY, Identifier.fromNamespaceAndPath(namespace, id)));
    }

    public static SymbolSet getSet(String id) {
        return Symbols.getSet(EnchantipsClient.MODID, id);
    }
}
