package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.accessor.EnchantmentMenuAccess;
import com.fedpol1.enchantips.config.ModOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.core.HolderSet;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.EnchantmentTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantable;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.LegacyRandomSource;

import java.util.*;

// DO NOT MODIFY contexts from here. this is threaded.
@SuppressWarnings("OptionalUsedAsFieldOrParameterType")
public class EnchantmentSeedSearcher implements Runnable{

    private static EnchantmentSeedSearcher LATEST = null;
    private HashMap<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> context;
    private final int partialSeed;
    private int delimiter; // last value that will be searched
    private int current; // current value so searches may be paused
    private final Optional<HolderSet.Named<Enchantment>> possibleEnchantments;
    private ArrayList<Integer> candidates;
    private final boolean strict;
    private boolean finished;

    private EnchantmentSeedSearcher(int partialSeed) {
        this.context = EnchantmentSeedContext.copy();
        this.partialSeed = Short.toUnsignedInt((short) partialSeed);
        this.delimiter = Integer.MAX_VALUE;
        this.current = this.delimiter + 1;
        this.candidates = new ArrayList<>();
        this.possibleEnchantments = EnchantmentSeedSearcher.getPossibleEnchantments();
        this.strict = ModOption.ENCHANTMENT_SEED_STRICT.getValue();
        this.finished = false;
    }

    public static void startSearch(int partialSeed) {
        if(EnchantmentSeedSearcher.LATEST != null) { // if a searcher exists, it is searching, so do not interrupt
            // even if there is only one candidate, try to eliminate it.
            EnchantmentSeedSearcher latest = EnchantmentSeedSearcher.LATEST;
            if(latest.finished) {
                latest.finished = false;
                latest.current = latest.delimiter + 1;
                latest.narrowCandidates();
                latest.finished = true;
            } else {
                latest.delimiter = latest.current - 1;
            }
            return;
        }
        EnchantmentSeedSearcher searcher = new EnchantmentSeedSearcher(partialSeed);
        EnchantmentSeedSearcher.LATEST = searcher;
        Thread thread = new Thread(searcher);
        thread.setDaemon(true);
        thread.start();
    }

    public static boolean foundSeed() {
        return EnchantmentSeedSearcher.getLatestCandidates().size() == 1 && EnchantmentSeedSearcher.LATEST.finished;
    }

    public static ArrayList<Integer> getLatestCandidates() {
        if(EnchantmentSeedSearcher.LATEST == null) {
            return new ArrayList<>();
        }
        return EnchantmentSeedSearcher.LATEST.candidates;
    }

    public static void reset() {
        if(EnchantmentSeedSearcher.LATEST == null) { return; }
        EnchantmentSeedSearcher.LATEST.finished = true; // abort the old latest run
        EnchantmentSeedSearcher.LATEST = null;
    }

    public static Optional<HolderSet.Named<Enchantment>> getPossibleEnchantments() {
        Level world = Minecraft.getInstance().level;
        if(world == null) { return Optional.empty(); }
        return world.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(EnchantmentTags.IN_ENCHANTING_TABLE);
    }

    public static List<EnchantmentInstance> getEnchantments(RandomSource random, Item item, int enchantability, int offerLevel) {
        return EnchantmentSeedSearcher.getEnchantments(random, item, enchantability, offerLevel, EnchantmentSeedSearcher.getPossibleEnchantments());
    }

    public static List<EnchantmentInstance> getEnchantments(RandomSource random, Item item, int enchantability, int offerLevel, Optional<HolderSet.Named<Enchantment>> possible) {
        if(possible.isEmpty()) { return List.of(); }
        ItemStack stack = new ItemStack(item, 1);
        stack.set(DataComponents.ENCHANTABLE, new Enchantable(enchantability));
        List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(random, stack, offerLevel, possible.get().stream());
        if (stack.is(Items.BOOK) && list.size() > 1) {
            list.remove(random.nextInt(list.size()));
        }
        return list;
    }

    private boolean checkSeed(int seed, EnchantmentSeedContext.FilterContextKey key) {
        // small optimization: check enchantment power bounds before generating enchantment lists
        LegacyRandomSource random = new LegacyRandomSource(seed + key.slot());
        IntBounds bounds = EnchantmentBoundsContext.getBounds(key.offerLevel(), key.enchantability()/4, key.slot());
        if(bounds != null) {
            int modifiedEnchantability = key.enchantability()/4 + 1;
            int power = key.offerLevel() + 1 + random.nextInt(modifiedEnchantability) + random.nextInt(modifiedEnchantability);
            float randomSpan = (random.nextFloat() + random.nextFloat() - 1.0F) * 0.15F;
            power = Math.max(Math.round((float)power + (float)power * randomSpan), 1);
            if(power < bounds.lower() || power > bounds.upper()) { return false; }
        }

        random = new LegacyRandomSource(seed + key.slot());
        List<EnchantmentInstance> list = EnchantmentSeedSearcher.getEnchantments(random, key.item(), key.enchantability(), key.offerLevel(), this.possibleEnchantments);
        if(list.isEmpty()) { return false; }
        EnchantmentInstance actual = list.get(random.nextInt(list.size()));
        EnchantmentLevel desired = this.context.get(key);
        return actual.enchantment().value() == desired.getEnchantment() && actual.level() == desired.getLevel();
    }

    private void initCandidates() {
        int realSeed;
        for(int i = 0; i < 65536; i++) {
            if(this.finished) { return; }
            if(EnchantmentSeedContext.searcherUpdate) {
                this.context = EnchantmentSeedContext.copy();
                EnchantmentSeedContext.searcherUpdate = false;
            }
            realSeed = Integer.MIN_VALUE + this.partialSeed + 65536 * i;
            boolean add = true;
            for(Map.Entry<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> entry : this.context.entrySet()) {
                add = this.checkSeed(realSeed, entry.getKey());
                if(!add) { break; }
            }
            if(add) { this.candidates.add(realSeed); }
        }
    }

    private void initCandidatesStrict() {
        for(long i = Integer.MIN_VALUE; i <= Integer.MAX_VALUE; i++) {
            if(this.finished) { return; }
            if(EnchantmentSeedContext.searcherUpdate) {
                this.context = EnchantmentSeedContext.copy();
                EnchantmentSeedContext.searcherUpdate = false;
            }
            boolean add = true;
            for(Map.Entry<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> entry : this.context.entrySet()) {
                add = this.checkSeed((int) i, entry.getKey());
                if(!add) { break; }
            }
            if(add) { this.candidates.add((int) i); }
        }
    }

    @SuppressWarnings("ConditionalBreakInInfiniteLoop")
    private void narrowCandidates() {
        if(this.candidates.isEmpty()) { return; }
        ArrayList<Integer> newCandidates = new ArrayList<>();
        int nextSeed = this.candidates.getFirst();
        int currentIndex = 0;

        while(true) {
            if(this.finished) { return; }
            if(EnchantmentSeedContext.searcherUpdate) {
                this.context = EnchantmentSeedContext.copy();
                EnchantmentSeedContext.searcherUpdate = false;
            }
            this.current = nextSeed;

            boolean add = true;
            for(Map.Entry<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> entry : this.context.entrySet()) {
                add = add && this.checkSeed(this.current, entry.getKey());
            }
            if(add) { newCandidates.add(this.current); }

            currentIndex = (currentIndex + 1) % this.candidates.size();
            nextSeed = this.candidates.get(currentIndex);
            if(nextSeed > this.delimiter || nextSeed <= this.current) { break; }
        }
        this.candidates = newCandidates;
    }

    @Override
    public void run() {
        if(this.finished) { return; }
        if(EnchantmentSeedSearcher.getLatestCandidates().isEmpty()) {
            if(this.strict) { this.initCandidatesStrict(); }
            else { this.initCandidates(); }
        } else {
            this.candidates = EnchantmentSeedSearcher.getLatestCandidates();
        }
        this.narrowCandidates();
        this.finished = true;
        if(EnchantmentSeedSearcher.LATEST == null || EnchantmentSeedSearcher.LATEST.candidates.size() > this.candidates.size()) {
            Screen screen = Minecraft.getInstance().gui.screen();
            if(screen instanceof EnchantmentScreen enchScreen) {
                EnchantmentMenuAccess menuAccess = (EnchantmentMenuAccess)enchScreen.getMenu();
                menuAccess.enchantips$updateFullEnchantmentHint(0);
                menuAccess.enchantips$updateFullEnchantmentHint(1);
                menuAccess.enchantips$updateFullEnchantmentHint(2);
            }
        }
        this.finished = true;
    }
}
