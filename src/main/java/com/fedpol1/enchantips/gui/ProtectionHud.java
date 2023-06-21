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

    public static final Identifier ICONS = new Identifier(EnchantipsClient.MODID, "textures/gui/icons.png");
    public static final int ICONS_WIDTH = 32;
    public static final int ICONS_HEIGHT = 32;

    private static void renderIndividualProtectionBars(DrawContext context, int protAmount, int texHeight, int xpos, int ypos, int v) {
        int u = 0;
        for(int i = 0; i < Math.min(20, protAmount); i+=2) {
            if(i + 1 == protAmount) {
                u = 9;
            }
            context.drawTexture(ICONS, xpos + 4 * i, ypos, u, v, 9, texHeight, ICONS_WIDTH, ICONS_HEIGHT);
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
                context.drawTexture(ICONS, xpos + 8 * i, ypos, 18, 0, 9, 9, ICONS_WIDTH, ICONS_HEIGHT);
        }
        renderIndividualProtectionBars(context, projProt, 3, xpos, ypos, 9);
        renderIndividualProtectionBars(context, fireProt, 3, xpos, ypos+2, 12);
        renderIndividualProtectionBars(context, blastProt, 3, xpos, ypos+4, 15);
        renderIndividualProtectionBars(context, fallProt, 3, xpos, ypos+6, 18);
        renderIndividualProtectionBars(context, genericProt, 9, xpos, ypos, 0);
    }

    private static int getProtectionLevel(Iterable<ItemStack> stacks, Enchantment e) {
        int level = 0;
        for(ItemStack stack : stacks) {
            level += EnchantmentHelper.getLevel(e, stack);
        }
        return level;
    }
}
