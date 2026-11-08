package com.fedpol1.enchantips.util;

import com.fedpol1.enchantips.accessor.EnchantmentMenuAccess;
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

public class EnchantmentSeedSearcher implements Runnable{

    private static EnchantmentSeedSearcher LATEST = null;
    private static HashMap<Thread, EnchantmentSeedSearcher> THREADS = new HashMap<>();
    private final HashMap<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> context;
    private final int partialSeed;
    private ArrayList<Integer> candidates;
    private boolean finished;
    private Thread spawnThread;

    private EnchantmentSeedSearcher(int partialSeed) {
        this.context = EnchantmentSeedContext.copy();
        this.partialSeed = Short.toUnsignedInt((short) partialSeed);
        this.candidates = new ArrayList<>();
        this.finished = false;
        this.spawnThread = null;
    }

    public static void startSearch(int partialSeed) {
        if(!EnchantmentSeedSearcher.shouldSearch()) { return; }
        EnchantmentSeedSearcher searcher = new EnchantmentSeedSearcher(partialSeed);
        Thread thread = new Thread(searcher);
        searcher.spawnThread = thread;
        thread.setDaemon(true);
        thread.start();
        EnchantmentSeedSearcher.THREADS.put(thread, searcher);
    }

    private static void stopAllSearches() {
        for(EnchantmentSeedSearcher searcher : EnchantmentSeedSearcher.THREADS.values()) {
            searcher.stop();
        }
        EnchantmentSeedSearcher.THREADS = new HashMap<>();
    }

    public static boolean shouldSearch() {
        return EnchantmentSeedSearcher.getLatestCandidates().size() != 1;
    }

    public static ArrayList<Integer> getLatestCandidates() {
        if(EnchantmentSeedSearcher.LATEST == null) {
            return new ArrayList<>();
        }
        return EnchantmentSeedSearcher.LATEST.candidates;
    }

    protected static void reset() {
        EnchantmentSeedSearcher.stopAllSearches();
        EnchantmentSeedSearcher.LATEST = null;
    }

    public void stop() {
        this.finished = true;
    }

    public static List<EnchantmentInstance> getEnchantments(RandomSource random, Item item, int enchantability, int offerLevel) {
        Level world = Minecraft.getInstance().level;
        if(world == null) { return List.of(); }
        Optional<HolderSet.Named<Enchantment>> possible = world.registryAccess()
                .lookupOrThrow(Registries.ENCHANTMENT)
                .get(EnchantmentTags.IN_ENCHANTING_TABLE);
        if(possible.isEmpty()) { return List.of(); }

        ItemStack stack = new ItemStack(item, 1);
        stack.set(DataComponents.ENCHANTABLE, new Enchantable(enchantability));
        List<EnchantmentInstance> list = EnchantmentHelper.selectEnchantment(random, stack, offerLevel, possible.get().stream());
        if (stack.is(Items.BOOK) && list.size() > 1) {
            list.remove(random.nextInt(list.size()));
        }
        return list;
    }

    private boolean checkSeed(long seed, EnchantmentSeedContext.FilterContextKey key) {
        LegacyRandomSource random = new LegacyRandomSource(seed + key.slot());
        List<EnchantmentInstance> list = EnchantmentSeedSearcher.getEnchantments(random, key.item(), key.enchantability(), key.offerLevel());

        EnchantmentInstance actual = list.get(random.nextInt(list.size()));
        EnchantmentLevel desired = this.context.get(key);
        return actual.enchantment().value() == desired.getEnchantment() && actual.level() == desired.getLevel();
    }

    private void initCandidates() {
        int realSeed;
        for(int i = 0; i < 65536; i++) {
            if(this.finished) { return; }
            realSeed = Integer.MIN_VALUE + this.partialSeed + 65536 * i;
            boolean add = true;
            for(Map.Entry<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> entry : this.context.entrySet()) {
                add = add && this.checkSeed(realSeed, entry.getKey());
            }
            if(add) { this.candidates.add(realSeed); }
        }
    }

    private void narrowCandidates() {
        ArrayList<Integer> newCandidates = new ArrayList<>();
        for(Integer seed : this.candidates) {
            if(this.finished) { return; }
            if(seed == null) { continue; }
            boolean add = true;
            for(Map.Entry<EnchantmentSeedContext.FilterContextKey, EnchantmentLevel> entry : this.context.entrySet()) {
                add = add && this.checkSeed(seed, entry.getKey());
            }
            if(add) { newCandidates.add(seed); }
        }
        this.candidates = newCandidates;
    }

    @Override
    public void run() {
        if(!EnchantmentSeedSearcher.shouldSearch()) { return; }
        if(this.finished) { return; }
        if(EnchantmentSeedSearcher.getLatestCandidates().isEmpty()) {
            this.initCandidates();
        } else {
            this.candidates = EnchantmentSeedSearcher.getLatestCandidates();
        }
        this.narrowCandidates();
        this.finished = true;
        EnchantmentSeedSearcher.THREADS.remove(this.spawnThread);
        this.spawnThread = null;
        if(EnchantmentSeedSearcher.LATEST == null || EnchantmentSeedSearcher.LATEST.candidates.size() > this.candidates.size()) {
            EnchantmentSeedSearcher.LATEST = this;
            Screen screen = Minecraft.getInstance().gui.screen();
            if(screen instanceof EnchantmentScreen enchScreen) {
                EnchantmentMenuAccess menuAccess = (EnchantmentMenuAccess)enchScreen.getMenu();
                menuAccess.enchantips$updateFullEnchantmentHint(0);
                menuAccess.enchantips$updateFullEnchantmentHint(1);
                menuAccess.enchantips$updateFullEnchantmentHint(2);
            }
        }
    }
}
