package com.fedpol1.enchantips.gui.screen;

import com.fedpol1.enchantips.EnchantipsClient;
import com.fedpol1.enchantips.gui.widgets.info_line.EnchantmentInfoLine;
import com.fedpol1.enchantips.gui.widgets.info_line.ScrollableInfoLineContainer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.*;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Optional;

public class EnchantmentInfoScreen extends BaseScreen {

    public EnchantmentInfoScreen(@Nullable Screen parent) {
        super(Text.translatable(EnchantipsClient.MODID + ".gui.enchantment_info"), parent);
        this.lines = new ScrollableInfoLineContainer(0x404040, 6);

        ClientWorld world = MinecraftClient.getInstance().world;
        if(world == null) { return; }
        Optional<Registry<Enchantment>> optionalWrapper = world.getRegistryManager().getOptional(RegistryKeys.ENCHANTMENT);
        if(optionalWrapper.isEmpty()) { return; }
        RegistryWrapper.Impl<Enchantment> wrapper = optionalWrapper.get();

        for(RegistryKey<Enchantment> key : wrapper.streamKeys().sorted(Comparator.comparing(RegistryKey::toString)).toList()) {
            Enchantment enchantment = world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).get(key);
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
