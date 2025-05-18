package com.fedpol1.enchantips.gui.widgets.info_line;

import com.fedpol1.enchantips.accessor.EnchantmentAccess;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.text.Text;

import java.util.List;
import java.util.Optional;

public class EnchantmentInfoLine extends CollapsibleInfoLine {

    public EnchantmentInfoLine(RegistryKey<Enchantment> enchantmentKey) {
        super(Text.literal(enchantmentKey.getValue().toString()));
    }

    public void populateMeta(Enchantment enchantment) {
        lines.addLine(new InfoLine(enchantment.description())); // description
        lines.addLine(new InfoLine(Text.translatable("enchantips.gui.enchantment_info.max_level", enchantment.getMaxLevel())));
        lines.addLine(new InfoLine(Text.translatable("enchantips.gui.enchantment_info.weight", enchantment.getWeight())));
        lines.addLine(new InfoLine(Text.translatable("enchantips.gui.enchantment_info.anvil_cost", enchantment.getAnvilCost())));
    }

    public void populatePowers(Enchantment enchantment) {
        CollapsibleInfoLine powers = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.power"));
        for(int i = 1; i < enchantment.getMaxLevel() + 1; i++) {
            powers.addLine(Text.translatable(
                    "enchantips.gui.enchantment_info.per_power",
                    Text.translatable("enchantment.level." + i),
                    enchantment.getMinPower(i),
                    enchantment.getMaxPower(i)
            ));
        }
        lines.addLine(powers);
    }

    public void populateExclusiveSet(Enchantment enchantment) {
        CollapsibleInfoLine exclusive = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.exclusive_set"));
        List<String> exclusiveEnchs = enchantment.exclusiveSet().stream().map(RegistryEntry::getIdAsString).sorted().toList();
        if(!exclusiveEnchs.isEmpty()) { lines.addLine(exclusive); }
        exclusiveEnchs.forEach(e -> exclusive.addLine(Text.literal(e)));
    }

    public void populateItems(Enchantment enchantment) {
        CollapsibleInfoLine primary = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.primary_items"));
        CollapsibleInfoLine secondary = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.secondary_items"));
        List<String> primaryItems = ((EnchantmentAccess)(Object) enchantment).enchantips$getPrimaryItems()
                .stream().map(e -> Registries.ITEM.getId(e.value()).toString())
                .sorted().toList();
        List<String> secondaryItems = ((EnchantmentAccess)(Object) enchantment).enchantips$getSecondaryItems()
                .stream().map(e -> Registries.ITEM.getId(e.value()).toString())
                .filter(e -> !primaryItems.contains(e))
                .sorted().toList();
        if(!primaryItems.isEmpty()) {
            primaryItems.forEach(e -> primary.addLine(Text.literal(e)));
            lines.addLine(primary);
        }
        if(!secondaryItems.isEmpty()) {
            secondaryItems.forEach(e -> secondary.addLine(Text.literal(e)));
            lines.addLine(secondary);
        }
    }

    public void populateTags(RegistryKey<Enchantment> enchantmentKey, ClientWorld world) {
        Optional<RegistryEntry.Reference<Enchantment>> enchantmentReference = world
                .getRegistryManager()
                .getOrThrow(RegistryKeys.ENCHANTMENT)
                .getEntry(enchantmentKey.getValue());
        if(enchantmentReference.isEmpty()) { return; }
        List<TagKey<Enchantment>> tagList = enchantmentReference.get().streamTags().toList();
        if(tagList.isEmpty()) { return; }
        CollapsibleInfoLine tags = new CollapsibleInfoLine(Text.translatable("enchantips.gui.enchantment_info.tags"));
        lines.addLine(tags);
        for (TagKey<Enchantment> tag : tagList) {
            tags.addLine(Text.literal("#").append(Text.literal(tag.id().toString())));
        }
    }
}
