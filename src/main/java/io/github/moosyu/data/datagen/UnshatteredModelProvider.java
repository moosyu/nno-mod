package io.github.moosyu.data.datagen;

import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.item.properties.conditional.FishingRodCast;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredModelProvider extends ModelProvider {
    public UnshatteredModelProvider(PackOutput output) {
        super(output, MODID);
    }

    @Override
    protected void registerModels(@NonNull BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        itemModels.generateFlatItem(UnshatteredItems.MERCENARY_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ROGUE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.TREECAPITATOR.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SPRUCE_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SERIOUSLY_DAMAGED_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DECENT_AXE.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FIG_HEW.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FIGSTONE_SPLITTER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SQUIRE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.UNDEAD_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ZOMBIE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FLORID_ZOMBIE_SWORD.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.IRON_DAGGER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EMERALD_DAGGER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GOLDEN_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SUPER_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.HYPER_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GIANT_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.RUSTY_CLEAVER.get(), ModelTemplates.FLAT_HANDHELD_ITEM);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.LEAFLET_HELMET.get(), Blocks.OAK_LEAVES);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_CHESTPLATE.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_LEGGINGS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.LEAFLET_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GLOW_SQUID_BOOTS.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.BAT_TALISMAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.BAT_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CENTURY_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CHILL_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CLUNK_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DIAMOND_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.DUST_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EGG_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EON_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FLAKE_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.EXPERIMENT_THE_FISH.get(), Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.FOSSIL_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GABAGOOL_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GIFT_THE_FISH.get(), Items.PUFFERFISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.HERRING_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.NOPE_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.OOPS_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.PARTY_THE_FISH.get(), Items.TROPICAL_FISH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ROCK_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SHRIMP_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SKELETON_THE_FISH.get(), Items.COOKED_COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SPOOK_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.STEW_THE_FISH.get(), Items.COD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.SWAMP_THE_FISH.get(), Items.COOKED_SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ZOOP_THE_FISH.get(), Items.SALMON, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.CAKE_SOUL.get(), Items.PINK_DYE, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_ROTTEN_FLESH.get(), Items.ROTTEN_FLESH, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ZOMBIE_HEART.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.GOLDEN_POWDER.get(), Items.GLOWSTONE_DUST, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_GOLD_INGOT.get(), Items.GOLD_INGOT, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_DIAMOND.get(), Items.DIAMOND, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_EMERALD.get(), Items.EMERALD, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_IRON.get(), Items.IRON_INGOT, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_COAL.get(), Items.COAL, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_LAPIS.get(), Items.LAPIS_LAZULI, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.ENCHANTED_REDSTONE.get(), Items.REDSTONE, ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.COINS_TALISMAN.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(UnshatteredItems.BROKEN_MITHRIL_PICKAXE.get(), ModelTemplates.FLAT_ITEM);

        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_GOLD_BLOCK.get(), Blocks.GOLD_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get(), Blocks.DIAMOND_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_EMERALD_BLOCK.get(), Blocks.EMERALD_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_IRON_BLOCK.get(), Blocks.IRON_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_COAL_BLOCK.get(), Blocks.COAL_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_LAPIS_BLOCK.get(), Blocks.LAPIS_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.ENCHANTED_REDSTONE_BLOCK.get(), Blocks.REDSTONE_BLOCK);
        createCubeBlockItemModel(itemModels, blockModels, UnshatteredItems.BEDROCK.get(), Blocks.BEDROCK);

        itemModels.generateFlatItem(UnshatteredItems.HEALING_TISSUE.get(), ModelTemplates.FLAT_ITEM);

        itemModels.generateFishingRod(UnshatteredItems.CHALLENGING_ROD.get());

        itemModels.itemModelOutput.accept(UnshatteredItems.ENCHANTED_FIG_LOG.get(),
                ItemModelUtils.plainModel(ModelTemplates.CUBE_COLUMN.create(UnshatteredUtils.getUnshatteredIdentifier("block/enchanted_fig_log"),
                        TextureMapping.column(UnshatteredBlocks.FIG_LOG_BLOCK.get()),
                        blockModels.modelOutput)
                )
        );

        itemModels.itemModelOutput.accept(UnshatteredItems.FISHING_ROD.get(),
                ItemModelUtils.conditional(new FishingRodCast(),
                        ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(Items.FISHING_ROD, "_cast")),
                        ItemModelUtils.plainModel(ModelLocationUtils.getModelLocation(Items.FISHING_ROD))
                )
        );

        blockModels.createRotatedPillarWithHorizontalVariant(UnshatteredBlocks.FIG_LOG_BLOCK.get(), TexturedModel.COLUMN, TexturedModel.COLUMN_HORIZONTAL);

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(),
                BlockModelGenerators.plainVariant(ModelTemplates.CUBE_COLUMN.create(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(),
                        TextureMapping.column(UnshatteredBlocks.FIG_LOG_BLOCK.get()),
                        blockModels.modelOutput)
                ))
        );

        blockModels.blockStateOutput.accept(MultiVariantGenerator
                .dispatch(UnshatteredBlocks.ROCK_TALKABLE_BLOCK.get(), BlockModelGenerators.variant(new Variant(UnshatteredUtils.getUnshatteredIdentifier("block/rock_talkable"))))
                .with(PropertyDispatch.modify(BlockStateProperties.HORIZONTAL_FACING)
                        .select(Direction.SOUTH, BlockModelGenerators.NOP)
                        .select(Direction.WEST,  BlockModelGenerators.Y_ROT_90)
                        .select(Direction.NORTH, BlockModelGenerators.Y_ROT_180)
                        .select(Direction.EAST,  BlockModelGenerators.Y_ROT_270)
                )
        );

        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(
                UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK.get(),
                BlockModelGenerators.plainVariant(ModelTemplates.CROP.create(
                        UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK.get(),
                        TextureMapping.crop(TextureMapping.getBlockTexture(Blocks.WHEAT, "_stage7")),
                        blockModels.modelOutput
                ))
        ));

        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(), Blocks.STONE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.COBBLESTONE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get(), Blocks.COAL_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK.get(), Blocks.IRON_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK.get(), Blocks.COPPER_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK.get(), Blocks.GOLD_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK.get(), Blocks.REDSTONE_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK.get(), Blocks.EMERALD_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK.get(), Blocks.DIAMOND_ORE);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.PURE_DIAMOND_BLOCK.get(), Blocks.DIAMOND_BLOCK);
        generateVanillaCubeBlock(blockModels, UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK.get(), Blocks.OBSIDIAN);
    }

    /**
     * creates a breakable block model that's based on a vanilla block with a texture that's the same on all sides
     * @param blockModels block models
     * @param breakableBlock the breakable block
     * @param blockTexture the block being used for the texture
     */
    private void generateVanillaCubeBlock(BlockModelGenerators blockModels, Block breakableBlock, Block blockTexture) {
        blockModels.blockStateOutput.accept(BlockModelGenerators.createSimpleBlock(breakableBlock,
                BlockModelGenerators.plainVariant(ModelTemplates.CUBE_ALL.create(breakableBlock,
                        TextureMapping.cube(blockTexture),
                        blockModels.modelOutput)
                ))
        );
    }

    /**
     * give an item a texture of a cube block (same on all sides) without actually having a block variant of it
     * @param itemModels item models
     * @param blockModels block models
     * @param blockItem the item being textured
     * @param blockTexture the block to be used for the texture
     */
    private void createCubeBlockItemModel(ItemModelGenerators itemModels, BlockModelGenerators blockModels, Item blockItem, Block blockTexture) {
        itemModels.itemModelOutput.accept(blockItem,
                ItemModelUtils.plainModel(ModelTemplates.CUBE_ALL.create(UnshatteredUtils.getUnshatteredIdentifier("block/" + BuiltInRegistries.ITEM.getKey(blockItem).getPath()),
                        TextureMapping.cube(blockTexture),
                        blockModels.modelOutput
                ))
        );
    }
}
