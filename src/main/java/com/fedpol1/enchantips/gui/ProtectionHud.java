package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ProtectionHud {

    public static final Identifier PROTECTION_EMPTY = new Identifier(EnchantipsClient.MODID, "hud/protection_empty");

    private static void renderIndividualProtectionBars(DrawContext context, ProtectionType protectionType, int protAmount, int xpos, int ypos) {
        String path = "hud/" + protectionType.getProtectionName();
        String suffix;
        int width = protectionType.getWidth();
        int height = protectionType.getHeight();
        for(int i = 0; i < Math.min(20, protAmount); i+=2) {
            suffix = "_full";
            if(i + 1 == protAmount) {
                suffix = "_half";
                width = protectionType.getReducedWidth();
            }
            context.drawGuiTexture(new Identifier(EnchantipsClient.MODID, path + suffix), xpos + 4 * i, ypos, width, height);
        }
    }

    public static void renderWholeProtectionBar(DrawContext context, int xpos, int armorHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client == null || client.player == null) { return; }

        Iterable<ItemStack> armor = client.player.getArmorItems();

        int genericProt = ProtectionHud.getProtectionLevel(armor, Enchantments.PROTECTION);
        int projProt = genericProt + ProtectionHud.getProtectionLevel(armor, Enchantments.PROJECTILE_PROTECTION) * 2;
        int fireProt = genericProt + ProtectionHud.getProtectionLevel(armor, Enchantments.FIRE_PROTECTION) * 2;
        int blastProt = genericProt + ProtectionHud.getProtectionLevel(armor, Enchantments.BLAST_PROTECTION) * 2;
        int fallProt = genericProt + ProtectionHud.getProtectionLevel(armor, Enchantments.FEATHER_FALLING) * 3;

        if(genericProt == 0 && projProt == 0 && fireProt == 0 && blastProt == 0 && fallProt == 0)
            return;

        int ypos = armorHeight - (client.player.getArmor() == 0 ? 0 : 10);

        for(int i = 0; i < 10; i++) {
                context.drawGuiTexture(PROTECTION_EMPTY, xpos + 8 * i, ypos, 9, 9);
        }
        renderIndividualProtectionBars(context, ProtectionType.PROJECTILE, projProt, xpos, ypos);
        renderIndividualProtectionBars(context, ProtectionType.FIRE, fireProt, xpos, ypos+2);
        renderIndividualProtectionBars(context, ProtectionType.BLAST, blastProt, xpos, ypos+4);
        renderIndividualProtectionBars(context, ProtectionType.FALL, fallProt, xpos, ypos+6);
        renderIndividualProtectionBars(context, ProtectionType.GENERIC, genericProt, xpos, ypos);
    }

    private static int getProtectionLevel(Iterable<ItemStack> stacks, Enchantment e) {
        int level = 0;
        for(ItemStack stack : stacks) {
            level += EnchantmentHelper.getLevel(e, stack);
        }
        return level;
    }

    enum ProtectionType {
        GENERIC("protection", 9, 9),
        PROJECTILE("projectile_protection", 9, 3),
        FIRE("fire_protection", 9, 3),
        BLAST("blast_protection", 9, 3),
        FALL("fall_protection", 9, 3);

        final String protectionName;
        final int width;
        final int height;

        ProtectionType(String s, int width, int height) {
            this.protectionName = s;
            this.width = width;
            this.height = height;
        }

        public String getProtectionName() {
            return this.protectionName;
        }

        public int getWidth() {
            return this.width;
        }

        public int getReducedWidth() {
            return (int)Math.ceil((double)this.width / 2.0);
        }

        public int getHeight() {
            return this.height;
        }
    }
}
