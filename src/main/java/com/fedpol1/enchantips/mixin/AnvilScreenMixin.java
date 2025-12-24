package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.widgets.AnvilSwapButton;
import com.fedpol1.enchantips.gui.widgets.AnvilSwapWarn;
import com.fedpol1.enchantips.util.EnchantmentListHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.ItemCombinerScreen;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.HashedStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ServerboundContainerClickPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ItemCombinerScreen<AnvilMenu> {

    @Unique
    private AnvilSwapWarn ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET;
    @Unique
    private AnvilSwapWarn ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET;
    @Unique
    private AnvilMenu enchantipsHandler; // dummy handler used for computations

    public AnvilScreenMixin(AnvilMenu handler, Inventory playerInventory, Component title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "<init>(Lnet/minecraft/world/inventory/AnvilMenu;Lnet/minecraft/world/entity/player/Inventory;Lnet/minecraft/network/chat/Component;)V", at = @At(value = "TAIL"))
    public void enchantips$init(AnvilMenu handler, Inventory inventory, Component title, CallbackInfo ci) {
        enchantipsHandler = new AnvilMenu(handler.containerId, inventory);
    }

    @Inject(method = "slotChanged(Lnet/minecraft/world/inventory/AbstractContainerMenu;ILnet/minecraft/world/item/ItemStack;)V", at = @At(value = "TAIL"))
    private void enchantips$addWarning(CallbackInfo ci) {
        if(!ModOption.ANVIL_SWAP_WARNING_SWITCH.getValue()) { return; }
        if(!this.enchantips$shouldSwapInputs()) {
            this.removeWidget(ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET);
            this.removeWidget(ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET);
            return;
        }
        if(ModOption.ANVIL_SWAP_BUTTON_SWITCH.getValue()) {
            this.addRenderableOnly(ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET);
        }
        else {
            this.addRenderableOnly(ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET);
        }
    }

    @Inject(method = "subInit()V", at = @At(value = "TAIL"))
    protected void enchantips$addAnvilSwapButton(CallbackInfo ci) {
        ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET = new AnvilSwapWarn(
                this.leftPos + 152, this.topPos + 33,
                16, 16,
                EnchantipsClient.id("widget/anvil_warning_small")
        );

        ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET = new AnvilSwapWarn(
                this.leftPos + 152, this.topPos + 39,
                16, 32,
                EnchantipsClient.id("widget/anvil_warning_large")
        );
        if(!ModOption.ANVIL_SWAP_BUTTON_SWITCH.getValue()) { return; }
        this.addRenderableWidget(
            new AnvilSwapButton(
                this.leftPos + 152,
                this.topPos + 47,
                (button) -> {
                    // swap items with notifying the server but only if it is favorable to do so
                    if (this.enchantips$shouldSwapInputs()) {
                        ClientPacketListener netHandler = Minecraft.getInstance().getConnection();
                        if (netHandler != null) {
                            for (short i : new short[]{0, 1, 0}) {
                                ServerboundContainerClickPacket p = new ServerboundContainerClickPacket(
                                    this.menu.containerId,
                                    this.menu.getStateId(),
                                    i,
                                    (byte) 0,
                                    ClickType.PICKUP,
                                    new Int2ObjectOpenHashMap<>(),
                                    HashedStack.create(this.menu.getCarried(), Minecraft.getInstance().getConnection().decoratedHashOpsGenenerator())
                                );
                                netHandler.send(p);
                            }
                        }
                    }
                }
            )
        );
    }

    @Unique
    private boolean enchantips$shouldSwapInputs() {
        this.enchantipsHandler.setItemName("");

        ItemStack inputStack1 = this.menu.getSlot(0).getItem();
        ItemStack inputStack2 = this.menu.getSlot(1).getItem();

        // get original output and cost
        this.enchantipsHandler.setItem(0, this.enchantipsHandler.incrementStateId(), inputStack1);
        this.enchantipsHandler.setItem(1, this.enchantipsHandler.incrementStateId(), inputStack2);
        this.enchantipsHandler.createResult();
        int originalCost = this.enchantipsHandler.getCost();
        ItemEnchantments originalEnchants = this.enchantipsHandler.getSlot(2).getItem().getEnchantments();

        // swap input items on client and evaluate what the output would be
        this.enchantipsHandler.setItem(0, this.enchantipsHandler.incrementStateId(), inputStack2);
        this.enchantipsHandler.setItem(1, this.enchantipsHandler.incrementStateId(), inputStack1);
        this.enchantipsHandler.createResult();
        int newCost = this.enchantipsHandler.getCost();
        ItemEnchantments newEnchants = this.enchantipsHandler.getSlot(2).getItem().getEnchantments();

        return newCost > 0 && originalCost > newCost && EnchantmentListHelper.sameEnchantments(originalEnchants, newEnchants, true);
    }
}
