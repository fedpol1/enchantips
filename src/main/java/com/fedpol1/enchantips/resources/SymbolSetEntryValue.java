package com.fedpol1.enchantips.resources;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Identifier;

public class SymbolSetEntryValue {

    protected static final Codec<Entry> ENTRY_OBJ_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("id").forGetter(Entry::identifier),
            Codec.BOOL.fieldOf("expected").forGetter(Entry::expected)
    ).apply(instance, Entry::new));

    protected static final Codec<EntryId> ENTRY_ID_CODEC = Identifier.CODEC.xmap(
            EntryId::new,
            EntryId::identifier
    );

    public static final Codec<Entry> ENTRY_CODEC = Codec.either(ENTRY_ID_CODEC, ENTRY_OBJ_CODEC).xmap(
            either -> either.map(
                    id -> new Entry(id.identifier, true),
                    entry -> entry
            ),
            entry -> entry.expected ? Either.left(new EntryId(entry.identifier)) : Either.right(entry)
    );

    public record Entry(Identifier identifier, boolean expected) {
    }

    public record EntryId(Identifier identifier) {
    }
}
