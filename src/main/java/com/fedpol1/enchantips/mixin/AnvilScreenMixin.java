package com.fedpol1.enchantips.mixin;

import com.fedpol1.enchantips.config.ModOption;
import com.fedpol1.enchantips.gui.AnvilScreenWidgets;
import com.fedpol1.enchantips.util.EnchantmentListHelper;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.IconWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    @Shadow private TextFieldWidget nameField;

    @Shadow protected abstract void onRenamed(String name);

    private boolean enchantipsDisplayWarning;
    private IconWidget ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET;

    private IconWidget ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET;

    private AnvilScreenHandler enchantipsHandler; // dummy handler used for computations

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(method = "<init>(Lnet/minecraft/screen/AnvilScreenHandler;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;)V", at = @At(value = "TAIL"))
    public void enchantipsInit(AnvilScreenHandler handler, PlayerInventory inventory, Text title, CallbackInfo ci) {
        ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET = new IconWidget(
                152,
                33,
                AnvilScreenWidgets.ANVIL_WARNING_SMALL_WIDTH,
                AnvilScreenWidgets.ANVIL_WARNING_SMALL_HEIGHT,
                AnvilScreenWidgets.ANVIL_WARNING_SMALL
        );
        ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET = new IconWidget(
                152,
                39,
                AnvilScreenWidgets.ANVIL_WARNING_LARGE_WIDTH,
                AnvilScreenWidgets.ANVIL_WARNING_LARGE_HEIGHT,
                AnvilScreenWidgets.ANVIL_WARNING_LARGE
        );
        enchantipsHandler = new AnvilScreenHandler(handler.syncId, inventory);
    }

    @Inject(method = "drawForeground(Lnet/minecraft/client/util/math/MatrixStack;II)V", at = @At(value = "RETURN"))
    private void enchantipsDrawWarning(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
        if(!(boolean) ModOption.SHOW_ANVIL_WARNING.getData().getValue() || !this.enchantipsDisplayWarning) { return; }
        if((boolean) ModOption.SHOW_ANVIL_ITEM_SWAP_BUTTON.getData().getValue()) {
            ENCHANTIPS_ANVIL_WARNING_SMALL_WIDGET.renderButton(matrices, mouseX, mouseY, 0.0f);
        }
        else {
            ENCHANTIPS_ANVIL_WARNING_LARGE_WIDGET.renderButton(matrices, mouseX, mouseY, 0.0f);
        }
    }

    @Inject(method = "onSlotUpdate(Lnet/minecraft/screen/ScreenHandler;ILnet/minecraft/item/ItemStack;)V", at = @At(value = "TAIL"))
    private void enchantipsAddWarning(CallbackInfo ci) {
        if(!(boolean) ModOption.SHOW_ANVIL_WARNING.getData().getValue()) { return; }
        this.enchantipsDisplayWarning = this.enchantipsShouldSwapInputs();
    }

    @Inject(method = "setup()V", at = @At(value = "TAIL"))
    protected void enchantipsAddAnvilSwapButton(CallbackInfo ci) {
        if(!(boolean) ModOption.SHOW_ANVIL_ITEM_SWAP_BUTTON.getData().getValue()) { return; }
        this.addDrawableChild(new TexturedButtonWidget(
                this.x + 152,
                this.height / 2 - 36,
                AnvilScreenWidgets.ANVIL_SWAP_ITEM_BUTTON_WIDTH,
                AnvilScreenWidgets.ANVIL_SWAP_ITEM_BUTTON_HEIGHT,
                0,
                0,
                AnvilScreenWidgets.ANVIL_SWAP_ITEM_BUTTON_HEIGHT,
                AnvilScreenWidgets.ANVIL_SWAP_ITEM_BUTTON,
                AnvilScreenWidgets.ANVIL_SWAP_ITEM_BUTTON_TEXTURE_WIDTH,
                AnvilScreenWidgets.ANVIL_SWAP_ITEM_BUTTON_TEXTURE_HEIGHT,
                (button) -> {
            // swap items with notifying the server but only if it is favorable to do so
            if (this.enchantipsShouldSwapInputs()) {
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
        }));
    }

    private boolean enchantipsShouldSwapInputs() {
        this.enchantipsHandler.setNewItemName("");
        int originalCost = this.enchantipsHandler.getLevelCost();
        NbtList originalEnchants = this.enchantipsHandler.getSlot(2).getStack().getEnchantments();

        // swap input items on client and evaluate what the output would be
        ItemStack inputStack1 = this.handler.getSlot(0).getStack();
        ItemStack inputStack2 = this.handler.getSlot(1).getStack();
        this.enchantipsHandler.setStackInSlot(0, this.enchantipsHandler.nextRevision(), inputStack2);
        this.enchantipsHandler.setStackInSlot(1, this.enchantipsHandler.nextRevision(), inputStack1);
        this.enchantipsHandler.updateResult();

        int newCost = this.enchantipsHandler.getLevelCost();
        NbtList newEnchants = this.enchantipsHandler.getSlot(2).getStack().getEnchantments();

        // swap items back
        this.enchantipsHandler.setStackInSlot(0, this.enchantipsHandler.nextRevision(), inputStack1);
        this.enchantipsHandler.setStackInSlot(1, this.enchantipsHandler.nextRevision(), inputStack2);
        this.enchantipsHandler.updateResult();

        return newCost > 0 && originalCost > newCost && EnchantmentListHelper.sameEnchantments(originalEnchants, newEnchants, true);
    }
}
