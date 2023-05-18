package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.util.EnchantmentFilterer;
import com.fedpol1.enchantips.util.EnchantmentLevelData;
import com.fedpol1.enchantips.util.TooltipHelper;
import net.minecraft.client.gui.screen.ingame.EnchantmentScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Mixin(EnchantmentScreen.class)
public abstract class EnchantmentScreenMixin implements EnchantmentAccess {

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
        if((boolean) ModOption.SHOW_MODIFIED_ENCHANTMENT_LEVEL.getData().getValue()) {
            list.add(TooltipHelper.buildModifiedLevel(absoluteLowerBound, absoluteUpperBound));
        }
        if((boolean) ModOption.SHOW_EXTRA_ENCHANTMENTS.getData().getValue() && !itemStack.isOf(Items.BOOK)) {
            ArrayList<EnchantmentLevelData> enchantmentLevelData = new ArrayList<>();
            for (Enchantment current : Registries.ENCHANTMENT) {
                if (current == enchantment || !current.canCombine(enchantment)) { continue; }
                if (!current.isAcceptableItem(itemStack)) { continue; }
                if (!current.isAvailableForRandomSelection() || current.isTreasure()) { continue; }
                for (int z = current.getMinLevel(); z <= current.getMaxLevel(); z++) {
                    EnchantmentLevelData enchLevel = EnchantmentLevelData.of(current, z);
                    if (enchLevel.getHighestModifiedLevel() >= absoluteLowerBound && enchLevel.getLowestModifiedLevel() <= absoluteUpperBound) {
                        enchantmentLevelData.add(EnchantmentLevelData.of(current, z));
                    }
                }
            }
            Collections.sort(enchantmentLevelData);
            for(EnchantmentLevelData levelData : enchantmentLevelData) {
                MutableText text = (MutableText) ((EnchantmentAccess) levelData.getEnchantment()).enchantipsGetName(levelData.getLevel(), itemStack.isOf(Items.ENCHANTED_BOOK));
                if((boolean) ModOption.SHOW_MODIFIED_LEVEL_FOR_ENCHANTMENT.getData().getValue()) {
                    text.append(" ").append(
                            TooltipHelper.buildModifiedLevelForEnchantment(levelData.getLowestModifiedLevel(), levelData.getHighestModifiedLevel())
                    );
                }
                list.add(text);
            }
        }
    }
}
