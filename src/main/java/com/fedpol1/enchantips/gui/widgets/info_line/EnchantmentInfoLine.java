package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.enchantment.Enchantment;

public class EnchantmentInfoLine extends CollapsibleInfoLine {

    public EnchantmentInfoLine(ResourceKey<Enchantment> enchantmentKey) {
        super(Component.literal(enchantmentKey.identifier().toString()));
    }

    public void populateMeta(Enchantment enchantment) {
        lines.addLine(new InfoLine(enchantment.description())); // description
        lines.addLine(new InfoLine(Component.translatable("enchantips.gui.enchantment_info.max_level", enchantment.getMaxLevel())));
        lines.addLine(new InfoLine(Component.translatable("enchantips.gui.enchantment_info.weight", enchantment.getWeight())));
        lines.addLine(new InfoLine(Component.translatable("enchantips.gui.enchantment_info.anvil_cost", enchantment.getAnvilCost())));
    }

    public void populatePowers(Enchantment enchantment) {
        CollapsibleInfoLine powers = new CollapsibleInfoLine(Component.translatable("enchantips.gui.enchantment_info.power"));
        for(int i = 1; i < enchantment.getMaxLevel() + 1; i++) {
            powers.addLine(Component.translatable(
                    "enchantips.gui.enchantment_info.per_power",
                    Component.translatable("enchantment.level." + i),
                    enchantment.getMinCost(i),
                    enchantment.getMaxCost(i)
            ));
        }
        lines.addLine(powers);
    }

    public void populateExclusiveSet(Enchantment enchantment) {
        CollapsibleInfoLine exclusive = new CollapsibleInfoLine(Component.translatable("enchantips.gui.enchantment_info.exclusive_set"));
        List<String> exclusiveEnchs = enchantment.exclusiveSet().stream().map(Holder::getRegisteredName).sorted().toList();
        if(!exclusiveEnchs.isEmpty()) { lines.addLine(exclusive); }
        exclusiveEnchs.forEach(e -> exclusive.addLine(Component.literal(e)));
    }

    public void populateItems(Enchantment enchantment) {
        CollapsibleInfoLine primary = new CollapsibleInfoLine(Component.translatable("enchantips.gui.enchantment_info.primary_items"));
        CollapsibleInfoLine secondary = new CollapsibleInfoLine(Component.translatable("enchantips.gui.enchantment_info.secondary_items"));
        List<String> primaryItems = ((EnchantmentAccess)(Object) enchantment).enchantips$getPrimaryItems()
                .stream().map(e -> BuiltInRegistries.ITEM.getKey(e.value()).toString())
                .sorted().toList();
        List<String> secondaryItems = ((EnchantmentAccess)(Object) enchantment).enchantips$getSecondaryItems()
                .stream().map(e -> BuiltInRegistries.ITEM.getKey(e.value()).toString())
                .filter(e -> !primaryItems.contains(e))
                .sorted().toList();
        if(!primaryItems.isEmpty()) {
            primaryItems.forEach(e -> primary.addLine(Component.literal(e)));
            lines.addLine(primary);
        }
        if(!secondaryItems.isEmpty()) {
            secondaryItems.forEach(e -> secondary.addLine(Component.literal(e)));
            lines.addLine(secondary);
        }
    }

    public void populateTags(ResourceKey<Enchantment> enchantmentKey, ClientLevel world) {
        Optional<Holder.Reference<Enchantment>> enchantmentReference = world
                .registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(enchantmentKey.identifier());
        if(enchantmentReference.isEmpty()) { return; }
        List<TagKey<Enchantment>> tagList = enchantmentReference.get().tags().toList();
        if(tagList.isEmpty()) { return; }
        CollapsibleInfoLine tags = new CollapsibleInfoLine(Component.translatable("enchantips.gui.enchantment_info.tags"));
        lines.addLine(tags);
        for (TagKey<Enchantment> tag : tagList) {
            tags.addLine(Component.literal("#").append(Component.literal(tag.location().toString())));
        }
    }
}
