package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.widgets.AnvilSwapButton;
import com.fedpol1.enchantips.gui.widgets.AnvilSwapWarn;
import com.fedpol1.enchantips.util.EnchantmentListHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    @Unique
    private AnvilSwapWarn ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET;
    @Unique
    private AnvilSwapWarn ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET;
    @Unique
    private AnvilScreenHandler enchantipsHandler; // dummy handler used for computations

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "<init>(Lnet/minecraft/screen/AnvilScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;)V", at = @At(value = "TAIL"))
    public void enchantips$init(AnvilScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        enchantipsHandler = new AnvilScreenHandler(handler.syncId, inventory);
    }

    @Inject(method = "onSlotUpdate(Lnet/minecraft/screen/ScreenHandler;ILnet/minecraft/item/ItemStack;)V", at = @At(value = "TAIL"))
    private void enchantips$addWarning(CallbackInfo ci) {
        if(!ModOption.ANVIL_SWAP_WARNING_SWITCH.getValue()) { return; }
        if(!this.enchantips$shouldSwapInputs()) {
            this.remove(ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET);
            this.remove(ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET);
            return;
        }
        if(ModOption.ANVIL_SWAP_BUTTON_SWITCH.getValue()) {
            this.addDrawable(ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET);
        }
        else {
            this.addDrawable(ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET);
        }
    }

    @Inject(method = "setup()V", at = @At(value = "TAIL"))
    protected void enchantips$addAnvilSwapButton(CallbackInfo ci) {
        ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET = new AnvilSwapWarn(
                this.x + 152, this.y + 33,
                16, 16,
                new Identifier(EnchantipsClient.MODID, "widget/anvil_warning_small")
        );

        ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET = new AnvilSwapWarn(
                this.x + 152, this.y + 39,
                16, 32,
                new Identifier(EnchantipsClient.MODID, "widget/anvil_warning_large")
        );
        if(!ModOption.ANVIL_SWAP_BUTTON_SWITCH.getValue()) { return; }
        this.addDrawableChild(
            new AnvilSwapButton(
                this.x + 152,
                this.y + 47,
                (button) -> {
                    // swap items with notifying the server but only if it is favorable to do so
                    if (this.enchantips$shouldSwapInputs()) {
                        ClientPlayNetworkHandler netHandler = MinecraftClient.getInstance().getNetworkHandler();
                        if (netHandler != null) {
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
                                netHandler.sendPacket(p);
                            }
                        }
                    }
                }
            )
        );
    }

    @Unique
    private boolean enchantips$shouldSwapInputs() {
        this.enchantipsHandler.setNewItemName("");

        ItemStack inputStack1 = this.handler.getSlot(0).getStack();
        ItemStack inputStack2 = this.handler.getSlot(1).getStack();

        // get original output and cost
        this.enchantipsHandler.setStackInSlot(0, this.enchantipsHandler.nextRevision(), inputStack1);
        this.enchantipsHandler.setStackInSlot(1, this.enchantipsHandler.nextRevision(), inputStack2);
        this.enchantipsHandler.updateResult();
        int originalCost = this.enchantipsHandler.getLevelCost();
        ItemEnchantmentsComponent originalEnchants = this.enchantipsHandler.getSlot(2).getStack().getEnchantments();

        // swap input items on client and evaluate what the output would be
        this.enchantipsHandler.setStackInSlot(0, this.enchantipsHandler.nextRevision(), inputStack2);
        this.enchantipsHandler.setStackInSlot(1, this.enchantipsHandler.nextRevision(), inputStack1);
        this.enchantipsHandler.updateResult();
        int newCost = this.enchantipsHandler.getLevelCost();
        ItemEnchantmentsComponent newEnchants = this.enchantipsHandler.getSlot(2).getStack().getEnchantments();

        return newCost > 0 && originalCost > newCost && EnchantmentListHelper.sameEnchantments(originalEnchants, newEnchants, true);
    }
}
