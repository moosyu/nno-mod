package io.github.moosyu.data.datagen;

import io.github.moosyu.blocks.BlockDropData;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.drops.MobItemDropData;
import io.github.moosyu.data.drops.MobRewardData;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.UnshatteredItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import org.jspecify.annotations.NonNull;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.data.UnshatteredDataMaps.*;

public class UnshatteredDataMapProvider extends DataMapProvider {
    public UnshatteredDataMapProvider(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(packOutput, lookupProvider);
    }

    @Override
    protected void gather(HolderLookup.@NonNull Provider provider) {
        Builder<List<BlockDropData>, Block> breakableDropsBuilder = this.builder(BREAKABLE_DROPS_DATA);

        this.builder(HARVESTABLE_BLOCKS_EXP_DATA)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get()), 15.0f, false)
                .add(BlockTags.FLOWERS, 1.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.OAK_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.SPRUCE_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BIRCH_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.JUNGLE_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.ACACIA_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.DARK_OAK_LOG), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.POTATOES), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.CARROTS), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.PUMPKIN), 4.5f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.MELON), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.SUGAR_CANE), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.MUSHROOM_STEM), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BROWN_MUSHROOM_BLOCK), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.RED_MUSHROOM_BLOCK), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.BROWN_MUSHROOM), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.RED_MUSHROOM), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.CACTUS), 2.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.COCOA), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get()), 1.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get()), 1.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get()), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK.get()), 4.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK.get()), 5.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK.get()), 5.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK.get()), 6.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK.get()), 7.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK.get()), 9.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK.get()), 10.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.PURE_DIAMOND_BLOCK.get()), 20.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK.get()), 20.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.get()), 25.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK.get()), 35.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.get()), 45.0f, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(Blocks.NETHER_WART), 4.0f, false);

        this.builder(FISHABLE_ITEMS_EXP_DATA)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.COD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SALMON), 0.7f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PUFFERFISH), 1.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.TROPICAL_FISH), 2.0f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_SHARD), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.PRISMARINE_CRYSTALS), 0.5f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.CLAY_BALL), 0.1f, false)
                .add(BuiltInRegistries.ITEM.wrapAsHolder(Items.SPONGE), 4.0f, false);

        this.builder(FISHABLE_MOBS_EXP_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), 25.0f, false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), 90.0f, false);

        this.builder(COMBATABLE_MOBS_LOOT_DATA)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ZOMBIE), new MobRewardData(
                        List.of(new MobItemDropData(Items.ROTTEN_FLESH, 1.0f, false, 1),
                                new MobItemDropData(Items.POISONOUS_POTATO, 0.02f, true, 1),
                                new MobItemDropData(Items.POTATO, 0.01f, true, 1),
                                new MobItemDropData(Items.CARROT, 0.01f, true, 1)
                        ),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        6.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SKELETON), new MobRewardData(
                        List.of(new MobItemDropData(Items.BONE, 1.0f, false, 1, 2)),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        6.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SLIME), new MobRewardData(
                        List.of(new MobItemDropData(Items.SLIME_BALL, 1.0f, false, 1)),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        6.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SPIDER), new MobRewardData(
                        List.of(new MobItemDropData(Items.STRING, 1.0f, false, 1),
                                new MobItemDropData(Items.SPIDER_EYE, 0.5f, false, 1)
                        ),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        8.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CAVE_SPIDER), new MobRewardData(
                        List.of(new MobItemDropData(Items.STRING, 1.0f, false, 1),
                                new MobItemDropData(Items.SPIDER_EYE, 0.5f, false, 1)
                        ),
                        1,
                        8,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        8.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.WITCH), new MobRewardData(
                        List.of(new MobItemDropData(Items.GUNPOWDER, 0.5f, false, 1),
                                new MobItemDropData(Items.GLOWSTONE_DUST, 0.5f, false, 1),
                                new MobItemDropData(Items.GLASS_BOTTLE, 0.2f, false, 1, 2)
                        ),
                        1,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        15.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ENDERMAN), new MobRewardData(
                        List.of(new MobItemDropData(Items.ENDER_PEARL, 1.0f, false, 1)),
                        2,
                        12,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        15.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BAT), new MobRewardData(
                        List.of(new MobItemDropData(UnshatteredItems.BAT_TALISMAN.get(), 0.01f, true, 1)),
                        100,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        33.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CREEPER), new MobRewardData(
                        List.of(new MobItemDropData(Items.GUNPOWDER, 1.0f, false, 1)),
                        2,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        8.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.BLAZE), new MobRewardData(
                        List.of(new MobItemDropData(Items.BLAZE_ROD, 1.0f, false, 1)),
                        3,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        10.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SQUID), new MobRewardData(
                        List.of(new MobItemDropData(Items.INK_SAC, 1.0f, false, 1, 2),
                                new MobItemDropData(Items.LILY_PAD, 1.0f, false, 1)
                        ),
                        5,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        75.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.GLOW_SQUID), new MobRewardData(
                        List.of(new MobItemDropData(Items.INK_SAC, 1.0f, false, 3, 6),
                                new MobItemDropData(Items.LILY_PAD, 1.0f, false, 1),
                                new MobItemDropData(UnshatteredItems.GLOW_SQUID_BOOTS.get(), 0.08f, true, 1)
                        ),
                        5,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        36.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.SHEEP), new MobRewardData(
                        List.of(new MobItemDropData(Items.MUTTON, 1.0f, false, 1, 2),
                                new MobItemDropData(Items.WHITE_WOOL, 1.0f, false, 1)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        3.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.COW), new MobRewardData(
                        List.of(new MobItemDropData(Items.BEEF, 1.0f, false, 1),
                                new MobItemDropData(Items.LEATHER, 1.0f, false, 1)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        3.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.CHICKEN), new MobRewardData(
                        List.of(new MobItemDropData(Items.FEATHER, 1.0f, false, 1),
                                new MobItemDropData(Items.CHICKEN, 1.0f, false, 1)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        2.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.RABBIT), new MobRewardData(
                        List.of(new MobItemDropData(Items.RABBIT, 1.0f, false, 1),
                                new MobItemDropData(Items.RABBIT_HIDE, 0.7f, false, 1),
                                new MobItemDropData(Items.RABBIT_FOOT, 0.7f, false, 1)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        5.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.PIG), new MobRewardData(
                        List.of(new MobItemDropData(Items.PORKCHOP, 1.0f, false, 1)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        3.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.MOOSHROOM), new MobRewardData(
                        List.of(new MobItemDropData(Items.BEEF, 1.0f, false, 1),
                                new MobItemDropData(Items.RED_MUSHROOM, 1.0f, false, 1, 4),
                                new MobItemDropData(Items.LEATHER, 1.0f, false, 1)
                        ),
                        0,
                        PlayerSkillsAttachment.Skill.FARMING,
                        5.0f
                ), false)
                .add(BuiltInRegistries.ENTITY_TYPE.wrapAsHolder(EntityType.ENDERMITE), new MobRewardData(
                        List.of(new MobItemDropData(Items.END_STONE, 1.0f, false, 1, 2)),
                        10,
                        PlayerSkillsAttachment.Skill.COMBAT,
                        20.0f
                ), false);

        this.builder(BLOCK_BREAKING_POWER_DATA)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK.get()), 2, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK.get()), 1, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.PURE_DIAMOND_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK.get()), 4, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK.get()), 3, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK.get()), 4, false)
                .add(BuiltInRegistries.BLOCK.wrapAsHolder(UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK.get()), 4, false);

        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK, new ItemRange(UnshatteredItems.FIG_LOG.get()));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_STONE_BLOCK, new ItemRange(Items.COBBLESTONE));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK, new ItemRange(Items.COBBLESTONE));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK, new ItemRange(Items.COAL));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK, new ItemRange(Items.IRON_INGOT));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK, new ItemRange(Items.COPPER_INGOT));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK, new ItemRange(Items.GOLD_INGOT));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK, new ItemRange(Items.REDSTONE));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK, new ItemRange(Items.EMERALD));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK, new ItemRange(Items.DIAMOND));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK, new ItemRange(Items.WHEAT));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.PURE_DIAMOND_BLOCK, new ItemRange(Items.COBBLESTONE));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK, new ItemRange(Items.OBSIDIAN));
        breakableDropsBuilder.add(UnshatteredBlocks.BREAKABLE_COBBLED_MITHRIL_BLOCK,
                List.of(new BlockDropData(new ItemRange(UnshatteredItems.MITHRIL.get())), new BlockDropData(new ItemRange(Items.COBBLESTONE, 1, 3))),
                false
        );
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_SOFT_MITHRIL_BLOCK, new ItemRange(UnshatteredItems.MITHRIL.get()));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK, new ItemRange(UnshatteredItems.MITHRIL.get(), 1, 2));
        createSingleBlockDropData(breakableDropsBuilder, UnshatteredBlocks.BREAKABLE_HARD_MITHRIL_BLOCK, new ItemRange(UnshatteredItems.MITHRIL.get(), 3, 5));
    }

    private void createSingleBlockDropData(DataMapProvider.Builder<List<BlockDropData>, Block> builder, DeferredBlock<Block> block, ItemRange itemRange) {
        builder.add(block, List.of(new BlockDropData(itemRange, 1.0f)), false);
    }
}
