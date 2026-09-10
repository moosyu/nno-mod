package io.github.moosyu.blocks;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

import static io.github.moosyu.Unshattered.MODID;

public final class UnshatteredBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);

    public static final DeferredBlock<Block> FIG_LOG_BLOCK = BLOCKS.register("fig_log", registerName -> new RotatedPillarBlock(BlockBehaviour.Properties.of().setId(ResourceKey.create(Registries.BLOCK, registerName))));
    public static final DeferredBlock<Block> ROCK_TALKABLE_BLOCK = BLOCKS.registerBlock("rock_talkable", TalkingRockBlock::new);

    public static final DeferredBlock<Block> BREAKABLE_FIG_LOG_BLOCK = BLOCKS.register("breakable_fig_log",
            registerName -> new RotatedPillarBlock(BlockBehaviour.Properties.of()
                    .setId(ResourceKey.create(Registries.BLOCK, registerName))
                    .destroyTime(10.0f)
                    .sound(SoundType.WOOD)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_COBBLESTONE_BLOCK = BLOCKS.registerBlock("breakable_cobblestone_block",
            props -> new Block(props
                    .destroyTime(1.5f)
                    .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_STONE_BLOCK = BLOCKS.registerBlock("breakable_stone_block",
            props -> new Block(props
                    .destroyTime(1.5f)
                    .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_COAL_ORE_BLOCK = BLOCKS.registerBlock("breakable_coal_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
                    .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_WHEAT_BLOCK = BLOCKS.registerBlock("breakable_wheat_block",
            props -> new Block(props
                    .sound(SoundType.CROP)
                    .noCollision()
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_IRON_ORE_BLOCK = BLOCKS.registerBlock("breakable_iron_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_COPPER_ORE_BLOCK = BLOCKS.registerBlock("breakable_copper_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_GOLD_ORE_BLOCK = BLOCKS.registerBlock("breakable_gold_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_REDSTONE_ORE_BLOCK = BLOCKS.registerBlock("breakable_redstone_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_EMERALD_ORE_BLOCK = BLOCKS.registerBlock("breakable_emerald_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_DIAMOND_ORE_BLOCK = BLOCKS.registerBlock("breakable_diamond_ore_block",
            props -> new Block(props
                    .destroyTime(3.0f)
            )
    );

    public static final DeferredBlock<Block> PURE_DIAMOND_BLOCK = BLOCKS.registerBlock("pure_diamond_block",
            props -> new Block(props
                    .destroyTime(15.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_OBSIDIAN_BLOCK = BLOCKS.registerBlock("breakable_obsidian_block",
            props -> new Block(props
                    .destroyTime(50.0f)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_SOFT_MITHRIL_BLOCK = BLOCKS.registerBlock("breakable_soft_mithril_block",
            props -> new Block(props
                    .destroyTime(45.0f)
                    .sound(SoundType.WOOL)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_HARD_MITHRIL_BLOCK = BLOCKS.registerBlock("breakable_hard_mithril_block",
            props -> new Block(props
                    .destroyTime(60.0f)
                    .sound(SoundType.STONE)
            )
    );

    public static final DeferredBlock<Block> BREAKABLE_COBBLED_MITHRIL_BLOCK = BLOCKS.registerBlock("breakable_cobbled_mithril_block",
            props -> new Block(props
                    .destroyTime(32.5f)
                    .sound(SoundType.STONE)
            )
    );
}
