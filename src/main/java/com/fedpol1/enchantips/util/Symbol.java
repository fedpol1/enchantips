package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;

import java.util.List;

public class Symbol {

    public static final Symbol SWATCH = new Symbol("#");
    public static final Symbol ALL = new Symbol("O");
    public static final Symbol ANVIL = new Symbol("A");
    public static final Symbol BOOK = new Symbol("B");
    public static final Symbol MISCELLANEOUS = new Symbol("M");
    public static final Symbol NONE = new Symbol("-");
    public static final Symbol UNKNOWN = new Symbol("U");
    public static final Symbol BOOTS = new Symbol("b");
    public static final Symbol LEGGINGS = new Symbol("l");
    public static final Symbol CHESTPLATE = new Symbol("c");
    public static final Symbol HELMET = new Symbol("h");
    public static final Symbol SKULL = new Symbol("S");
    public static final Symbol PUMPKIN = new Symbol("P");
    public static final Symbol ELYTRA = new Symbol("E");
    public static final Symbol SWORD = new Symbol("w");
    public static final Symbol AXE = new Symbol("a");
    public static final Symbol PICKAXE = new Symbol("p");
    public static final Symbol SHOVEL = new Symbol("s");
    public static final Symbol HOE = new Symbol("e");
    public static final Symbol SHEARS = new Symbol("L");
    public static final Symbol TRIDENT = new Symbol("T");
    public static final Symbol MACE = new Symbol("m");
    public static final Symbol SHIELD = new Symbol("D");
    public static final Symbol BOW = new Symbol("W");
    public static final Symbol CROSSBOW = new Symbol("C");
    public static final Symbol FISHING_ROD = new Symbol("r");
    public static final Symbol BAITED_ROD = new Symbol("t");
    public static final Symbol BRUSH = new Symbol("R");
    public static final Symbol FIRESTARTER = new Symbol("F");
    public static final Symbol COMPASS = new Symbol("o");

    private final String s;

    public Symbol(String str) {
        this.s = str;
    }

    public static MutableText mergeAndDecorate(List<Symbol> symbols) {
        StringBuilder acc = new StringBuilder();
        for(Symbol s : symbols) {
            if(s == null) { continue; }
            acc.append(s.s);
        }
        return Text.literal(acc.toString())
                .setStyle(Style.EMPTY
                        .withFont(EnchantipsClient.SYMBOL_FONT)
                );
    }

    public MutableText decorate(int rgb) {
        return Text.literal(this.s)
                .setStyle(Style.EMPTY
                        .withFont(EnchantipsClient.SYMBOL_FONT)
                        .withColor(rgb)
                );
    }
}
