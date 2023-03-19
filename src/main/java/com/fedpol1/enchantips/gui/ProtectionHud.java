package com.fedpol1.enchantips.gui;

import com.fedpol1.enchantips.EnchantipsClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class ProtectionHud {

    public static final Identifier ICONS = new Identifier(EnchantipsClient.MODID, "textures/gui/icons.png");
    public static final int ICONS_WIDTH = 32;
    public static final int ICONS_HEIGHT = 32;

    private static void renderIndividualProtectionBars(MatrixStack matrixStack, int protAmount, int texHeight, int xpos, int ypos, int v) {
        int u = 0;
        for(int i = 0; i < Math.min(20, protAmount); i+=2) {
            if(i + 1 == protAmount) {
                u = 9;
            }
            DrawableHelper.drawTexture(matrixStack, xpos + 4 * i, ypos, u, v, 9, texHeight, ICONS_WIDTH, ICONS_HEIGHT);
        }
    }

    public static void renderWholeProtectionBar(MatrixStack matrixStack, int xpos, int armorHeight) {
        MinecraftClient client = MinecraftClient.getInstance();
        if(client == null)
            return;
        assert client.player != null;

        Iterable<ItemStack> armor = client.player.getArmorItems();
        if(MinecraftClient.getInstance().player == null) { return; }
        DamageSources sources = MinecraftClient.getInstance().player.getDamageSources();
        int genericProt = EnchantmentHelper.getProtectionAmount(armor, sources.generic());
        int projProt = EnchantmentHelper.getProtectionAmount(armor, sources.arrow(null, null));
        int fireProt = EnchantmentHelper.getProtectionAmount(armor, sources.onFire());
        int blastProt = EnchantmentHelper.getProtectionAmount(armor, sources.explosion(null));
        int fallProt = EnchantmentHelper.getProtectionAmount(armor, sources.fall());

        if(genericProt == 0 && projProt == 0 && fireProt == 0 && blastProt == 0 && fallProt == 0)
            return;

        int ypos = armorHeight - (client.player.getArmor() == 0 ? 0 : 10);

        for(int i = 0; i < 10; i++) {
                DrawableHelper.drawTexture(matrixStack, xpos + 8 * i, ypos, 18, 0, 9, 9, ICONS_WIDTH, ICONS_HEIGHT);
        }
        renderIndividualProtectionBars(matrixStack, projProt, 3, xpos, ypos, 9);
        renderIndividualProtectionBars(matrixStack, fireProt, 3, xpos, ypos+2, 12);
        renderIndividualProtectionBars(matrixStack, blastProt, 3, xpos, ypos+4, 15);
        renderIndividualProtectionBars(matrixStack, fallProt, 3, xpos, ypos+6, 18);
        renderIndividualProtectionBars(matrixStack, genericProt, 9, xpos, ypos, 0);
    }
}
