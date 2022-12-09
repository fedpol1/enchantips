package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModConfig;
import com.fedpol1.enchantips.gui.AnvilScreenSwapItemButton;
import com.fedpol1.enchantips.util.EnchantmentListUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "setup()V", at = @At(value = "TAIL"))
    protected void enchantipsSetupAddAnvilSwapButton(CallbackInfo ci) {
        if(ModConfig.SHOW_ANVIL_ITEM_SWAP_BUTTON.getValue()) {
            this.addDrawableChild(new TexturedButtonWidget(
                    this.x + 152,
                    this.height / 2 - 36,
                    AnvilScreenSwapItemButton.ANVIL_SWAP_ITEM_BUTTON_WIDTH,
                    AnvilScreenSwapItemButton.ANVIL_SWAP_ITEM_BUTTON_HEIGHT,
                    0,
                    0,
                    AnvilScreenSwapItemButton.ANVIL_SWAP_ITEM_BUTTON_HEIGHT,
                    AnvilScreenSwapItemButton.ANVIL_SWAP_ITEM_BUTTON,
                    AnvilScreenSwapItemButton.ANVIL_SWAP_ITEM_BUTTON_TEXTURE_WIDTH,
                    AnvilScreenSwapItemButton.ANVIL_SWAP_ITEM_BUTTON_TEXTURE_HEIGHT,
                    (button) -> {
                int originalCost = this.handler.getLevelCost();
                NbtList originalEnchants = this.handler.getSlot(2).getStack().getEnchantments();

                // swap input items and evaluate what the output would be
                ItemStack inputStack1 = this.handler.getSlot(0).getStack();
                ItemStack inputStack2 = this.handler.getSlot(1).getStack();
                this.handler.setStackInSlot(0, this.handler.nextRevision(), inputStack2);
                this.handler.setStackInSlot(1, this.handler.nextRevision(), inputStack1);
                this.handler.updateResult();

                int newCost = this.handler.getLevelCost();
                NbtList newEnchants = this.handler.getSlot(2).getStack().getEnchantments();

                // swap items back
                this.handler.setStackInSlot(0, this.handler.nextRevision(), inputStack1);
                this.handler.setStackInSlot(1, this.handler.nextRevision(), inputStack2);
                this.handler.updateResult();

                // swap items again, this time notifying the server, and only if it is favorable to do so
                if (newCost > 0 && originalCost > newCost && EnchantmentListUtil.sameEnchantments(originalEnchants, newEnchants, true)) {
                    if (MinecraftClient.getInstance().getNetworkHandler() != null) {
                        for (int i : new int[]{0, 1, 0}) {
                            ClickSlotC2SPacket p = new ClickSlotC2SPacket(
                                    this.handler.syncId,
                                    this.handler.getRevision(),
                                    i,
                                    0,
                                    SlotActionType.PICKUP,
                                    ItemStack.EMPTY,
                                    new Int2ObjectArrayMap<>()
                            );
                            MinecraftClient.getInstance().getNetworkHandler().sendPacket(p);
                        }
                    }
                }
            }));
        }
    }
}
