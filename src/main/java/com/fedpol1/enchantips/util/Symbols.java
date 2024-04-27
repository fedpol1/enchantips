package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public enum Symbols {

    SWATCH("#"),
    ALL("O"),
    ANVIL("A"),
    BOOK("B"),
    MISCELLANEOUS("M"),
    UNKNOWN("U"),
    BOOTS("b"),
    LEGGINGS("l"),
    CHESTPLATE("c"),
    HELMET("h"),
    SKULL("S"),
    ELYTRA("E"),
    SWORD("w"),
    AXE("a"),
    PICKAXE("p"),
    SHOVEL("s"),
    HOE("e"),
    SHEARS("L"),
    TRIDENT("T"),
    MACE("m"),
    SHIELD("D"),
    BOW("W"),
    CROSSBOW("C"),
    FISHING_ROD("r"),
    BAITED_ROD("t"),
    BRUSH("R"),
    FIRESTARTER("F");

    private final String s;

    Symbols(String str) {
        this.s = str;
    }

    public static MutableText mergeAndDecorate(List<Symbols> symbols) {
        StringBuilder acc = new StringBuilder();
        for(Symbols s : symbols) {
            if(s == null) { continue; }
            acc.append(s.s);
        }
        return Text.literal(acc.toString())
                .setStyle(Style.EMPTY
                        .withFont(EnchantipsClient.SYMBOL_FONT)
                );
    }

    public MutableText decorate() {
        return Text.literal(this.s).setStyle(Style.EMPTY.withFont(EnchantipsClient.SYMBOL_FONT));
    }

    public MutableText decorate(int rgb) {
        return Text.literal(this.s)
                .setStyle(Style.EMPTY
                        .withFont(EnchantipsClient.SYMBOL_FONT)
                        .withColor(rgb)
                );
    }
}
