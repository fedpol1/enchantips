package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.EnchantmentInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.enchantment.Enchantment;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public class EnchantmentInfoScreen extends BaseScreen {

    public EnchantmentInfoScreen(@Nullable Screen parent) {
        super(Component.translatable(EnchantipsClient.MODID + ".gui.enchantment_info"), parent);
        this.lines = new ScrollableInfoLineContainer(0xff404040, 6);

        ClientLevel world = Minecraft.getInstance().level;
        if(world == null) { return; }
        Optional<Registry<Enchantment>> optionalWrapper = world.registryAccess().lookup(Registries.ENCHANTMENT);
        if(optionalWrapper.isEmpty()) { return; }
        HolderLookup.RegistryLookup<Enchantment> wrapper = optionalWrapper.get();

        for(ResourceKey<Enchantment> key : wrapper.listElementIds().sorted(Comparator.comparing(ResourceKey::toString)).toList()) {
            Enchantment enchantment = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getValue(key);
            if(enchantment == null) { continue; }
            EnchantmentInfoLine enchMeta = new EnchantmentInfoLine(key);
            this.lines.addLine(enchMeta);
            enchMeta.populateMeta(enchantment);
            enchMeta.populatePowers(enchantment);
            enchMeta.populateExclusiveSet(enchantment);
            enchMeta.populateItems(enchantment);
            enchMeta.populateTags(key, world);
        }
    }
}
