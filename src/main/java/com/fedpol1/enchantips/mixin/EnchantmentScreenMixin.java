package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantmentMixinAccess;
import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.TooltipBuilder;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.registry.Registry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin implements EnchantmentMixinAccess {

    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V",
            slice = @Slice(from = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/EnchantmentScreen;isPointWithinBounds(IIIIDD)Z")),
            at = @At(value = "INVOKE", target = "Ljava/util/List;add(Ljava/lang/Object;)Z", ordinal = 0, shift = At.Shift.AFTER),
            locals = LocalCapture.CAPTURE_FAILHARD)
    public void enchantipsInjectRenderExtraEnchantments(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci, boolean bl, int i, int j, int k, Enchantment enchantment, int l, int m, List<Text> list) {
        EnchantmentScreen t = (EnchantmentScreen) (Object)this;
        int tableLevel = t.getScreenHandler().enchantmentPower[j];
        ItemStack itemStack = t.getScreenHandler().getSlot(0).getStack();
        int absoluteLowerBound = EnchantmentFilterer.getLowerBound(enchantment, l, itemStack, tableLevel);
        int absoluteUpperBound = EnchantmentFilterer.getUpperBound(enchantment, l, itemStack, tableLevel);
        if(ModConfig.SHOW_MODIFIED_ENCHANTMENT_LEVEL.getValue()) {
            list.add(TooltipBuilder.buildModifiedLevel(absoluteLowerBound, absoluteUpperBound));
        }
        if(ModConfig.SHOW_EXTRA_ENCHANTMENTS.getValue() && !itemStack.isOf(Items.BOOK)) {
            for (Enchantment current : Registry.ENCHANTMENT) {
                if (current == enchantment || !current.canCombine(enchantment)) {
                    continue;
                }
                if (!current.type.isAcceptableItem(itemStack.getItem())) {
                    continue;
                }
                if (!current.isAvailableForRandomSelection() || current.isTreasure()) {
                    continue;
                }
                for (int z = current.getMinLevel(); z <= current.getMaxLevel(); z++) {
                    int currentLowerBound = EnchantmentFilterer.getLowerBoundForEnchantment(current, z);
                    int currentUpperBound = EnchantmentFilterer.getUpperBoundForEnchantment(current, z);
                    if (currentUpperBound >= absoluteLowerBound && currentLowerBound <= absoluteUpperBound) {
                        MutableText text = (MutableText) ((EnchantmentMixinAccess) current).enchantipsGetName(z, itemStack.isOf(Items.ENCHANTED_BOOK));
                        if(ModConfig.SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT.getValue()) {
                            text.append(" ").append(TooltipBuilder.buildModifiedLevelForEnchantment(currentLowerBound, currentUpperBound));
                        }
                        list.add(text);
                    }
                }
            }
        }
    }
}
