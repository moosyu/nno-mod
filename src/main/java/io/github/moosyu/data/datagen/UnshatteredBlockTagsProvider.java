package io.github.moosyu.data.datagen;

import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import org.jspecify.annotations.NonNull;

import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.blocks.UnshatteredBlocks.*;

public class UnshatteredBlockTagsProvider extends BlockTagsProvider {
    public UnshatteredBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider, MODID);
    }

    public static final TagKey<Block> COLLECTABLE_MINING_BLOCKS = BlockTags.create(UnshatteredUtils.getUnshatteredIdentifier("collectable_mining_blocks"));
    public static final TagKey<Block> COLLECTABLE_FARMING_BLOCKS = BlockTags.create(UnshatteredUtils.getUnshatteredIdentifier("collectable_farming_blocks"));
    public static final TagKey<Block> COLLECTABLE_FORAGING_BLOCKS = BlockTags.create(UnshatteredUtils.getUnshatteredIdentifier("collectable_foraging_blocks"));
    public static final TagKey<Block> MITHRIL_BLOCKS = BlockTags.create(UnshatteredUtils.getUnshatteredIdentifier("mithril_blocks"));

    @Override
    protected void addTags(HolderLookup.@NonNull Provider provider) {
        tag(BlockTags.MINEABLE_WITH_AXE).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_STONE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COBBLESTONE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COAL_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_IRON_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COPPER_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_GOLD_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_REDSTONE_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_EMERALD_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_DIAMOND_ORE_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(PURE_DIAMOND_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_OBSIDIAN_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_SOFT_MITHRIL_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_HARD_MITHRIL_BLOCK.get());
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(BREAKABLE_COBBLED_MITHRIL_BLOCK.get());
        tag(BlockTags.LOGS).add(FIG_LOG_BLOCK.get());
        tag(BlockTags.LOGS).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_STONE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COBBLESTONE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COAL_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_IRON_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COPPER_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_GOLD_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_REDSTONE_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_EMERALD_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_DIAMOND_ORE_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(PURE_DIAMOND_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_OBSIDIAN_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_SOFT_MITHRIL_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_COBBLED_MITHRIL_BLOCK.get());
        tag(COLLECTABLE_MINING_BLOCKS).add(BREAKABLE_HARD_MITHRIL_BLOCK.get());
        tag(COLLECTABLE_FORAGING_BLOCKS).add(BREAKABLE_FIG_LOG_BLOCK.get());
        tag(COLLECTABLE_FARMING_BLOCKS).add(BREAKABLE_WHEAT_BLOCK.get());
        tag(MITHRIL_BLOCKS).add(BREAKABLE_COBBLED_MITHRIL_BLOCK.get());
        tag(MITHRIL_BLOCKS).add(BREAKABLE_HARD_MITHRIL_BLOCK.get());
        tag(MITHRIL_BLOCKS).add(BREAKABLE_SOFT_MITHRIL_BLOCK.get());
    }
}
