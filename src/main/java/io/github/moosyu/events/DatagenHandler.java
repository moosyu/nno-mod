package io.github.moosyu.events;

import io.github.moosyu.blocks.TalkingRockBlock;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.dialogue.*;
import io.github.moosyu.data.quests.Quest;
import io.github.moosyu.data.quests.QuestTypes;
import io.github.moosyu.data.regen.RegenPaths.*;
import io.github.moosyu.data.regions.*;
import io.github.moosyu.data.datagen.*;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class DatagenHandler {
    @SubscribeEvent
    public static void onGatherDataClient(GatherDataEvent.Client event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        generator.addProvider(true, new UnshatteredModelProvider(packOutput));
        generator.addProvider(true, new EquipmentAssets(packOutput));
        generator.addProvider(true, new UnshatteredBlockTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredItemTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredEntityTagsProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredDataMapProvider(packOutput, lookupProvider));
        generator.addProvider(true, new UnshatteredSoundDefinitionsProvider(packOutput));
        event.createProvider(UnshatteredRecipeProvider.Runner::new);

        event.createDatapackRegistryObjects(
                new RegistrySetBuilder().add(DataPackRegistryHandler.REGION_REGISTRY_KEY, bootstrap -> {
                    bootstrap.register(UnshatteredRegions.PLAINS_REGION, new Region(0xFF71AD1C, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.DEFAULT_REGION, new Region(0xFFFFFFFF, RegionTemperatureTypes.COLD, false));
                    bootstrap.register(UnshatteredRegions.FORGOTTEN_DWELLING, new Region(0xFFCCD9E3, RegionTemperatureTypes.COMFORTABLE, false));
                    bootstrap.register(UnshatteredRegions.HYTHE, new Region(0xFFB1FA96, RegionTemperatureTypes.COMFORTABLE, false));
                }).add(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, bootstrap -> {
                    HolderGetter<Region> regions = bootstrap.lookup(DataPackRegistryHandler.REGION_REGISTRY_KEY);

                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-2048, -2048), new Vector2i(2048, 2048), 0), UnshatteredRegions.DEFAULT_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-846, 688), new Vector2i(-1119, 825), 1), UnshatteredRegions.PLAINS_REGION, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-1035, 837), new Vector2i(-1070, 805), 2), UnshatteredRegions.FORGOTTEN_DWELLING, regions);
                    registerRegionBoundary(bootstrap, new BoundaryCoordinates(new Vector2i(-864, 707), new Vector2i(-937, 792), 2), UnshatteredRegions.HYTHE, regions);

                }).add(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, bootstrap -> {
                    registerDialogueTree(bootstrap,
                            TalkingRockBlock.ROCK_DIALOGUE_TREE,
                            new DialogueTree(List.of(
                                    createDialogueOriginWithSelfFlag(
                                    0,
                                    new DialogueNode(Component.literal("hi, im a rock"),
                                            List.of(new DialogueChoice(Component.literal("interesting"),
                                                    new DialogueNode(Component.literal("im glad you think so :)"))),
                                                    new DialogueChoice(Component.literal("..."))
                                            )
                                    ),
                                    TalkingRockBlock.HI_MESSAGE_IDENTIFIER),
                                    createDialogueOriginWithSelfFlag(1,
                                            new DialogueNode(Component.literal("you've already spoken to me"),
                                                    List.of(new DialogueChoice(Component.literal("i know right"),
                                                            new GiveItemDialogueEvent(BuiltInRegistries.ITEM.wrapAsHolder(Items.DIAMOND), 1))
                                                    )
                                            ),
                                            List.of(TalkingRockBlock.HI_MESSAGE_IDENTIFIER),
                                            List.of(),
                                            List.of(),
                                            TalkingRockBlock.HI2_MESSAGE_IDENTIFIER
                                    ),
                                    createDialogueOrigin(2,
                                            new DialogueNode(Component.literal("find my pages"),
                                                    List.of(new DialogueChoice(Component.literal("i guess"), List.of(TalkingRockBlock.ROCKS_QUEST), new StartQuestDialogueEvent(TalkingRockBlock.ROCKS_QUEST)),
                                                            new DialogueChoice(Component.literal("no thanks")))
                                            ),
                                            List.of(TalkingRockBlock.HI2_MESSAGE_IDENTIFIER),
                                            List.of(TalkingRockBlock.ROCKS_QUEST)
                                    )
                            ))
                    );
                }).add(DataPackRegistryHandler.QUEST_REGISTRY_KEY, bootstrap -> {
                    registerQuest(bootstrap, TalkingRockBlock.ROCKS_QUEST, QuestTypes.NOVICE, new GiveItemDialogueEvent(BuiltInRegistries.ITEM.wrapAsHolder(Items.STONE), 1));
                }).add(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, bootstrap -> {
                    createRegenPathWithBlocks(bootstrap, "stone", List.of(UnshatteredBlocks.BREAKABLE_STONE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(),
                            Blocks.BEDROCK),
                            120
                    );
                    createRegenPathWithBlocks(bootstrap, "coal", List.of(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPathWithBlocks(bootstrap, "iron", List.of(UnshatteredBlocks.BREAKABLE_IRON_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPathWithBlocks(bootstrap, "copper", List.of(UnshatteredBlocks.BREAKABLE_COPPER_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPathWithBlocks(bootstrap,"gold", List.of(UnshatteredBlocks.BREAKABLE_GOLD_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPathWithBlocks(bootstrap, "redstone", List.of(UnshatteredBlocks.BREAKABLE_REDSTONE_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPathWithBlocks(bootstrap, "emerald", List.of(UnshatteredBlocks.BREAKABLE_EMERALD_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPathWithBlocks(bootstrap, "diamond", List.of(UnshatteredBlocks.BREAKABLE_DIAMOND_ORE_BLOCK.get(),
                            UnshatteredBlocks.BREAKABLE_COBBLESTONE_BLOCK.get(), Blocks.BEDROCK),
                            150
                    );
                    createRegenPath(bootstrap, "wheat", List.of(UnshatteredBlocks.BREAKABLE_WHEAT_BLOCK.get().defaultBlockState(),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 6),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 5),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 4),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 3),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 2),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 1),
                            Blocks.WHEAT.defaultBlockState().setValue(CropBlock.AGE, 0)),
                            200,
                            6
                    );
                    createRegenPathWithBlocks(bootstrap, "pure_diamond", List.of(UnshatteredBlocks.PURE_DIAMOND_BLOCK.get(),
                            Blocks.BEDROCK),
                            200
                    );
                    createRegenPathWithBlocks(bootstrap, "obsidian", List.of(UnshatteredBlocks.BREAKABLE_OBSIDIAN_BLOCK.get(),
                                    Blocks.BEDROCK),
                            240
                    );
                    createRegenPathWithBlocks(bootstrap, "fig_wood", List.of(UnshatteredBlocks.BREAKABLE_FIG_LOG_BLOCK.get(),
                            Blocks.AIR),
                            200
                    );
                    createRegenPathWithBlocks(bootstrap, "oak_leaf", List.of(Blocks.OAK_LEAVES,
                                    Blocks.AIR),
                            200
                    );
                })
        );
    }

    private static void registerRegionBoundary(BootstrapContext<RegionBoundary> bootstrap, BoundaryCoordinates boundaryCoordinates, ResourceKey<Region> region, HolderGetter<Region> regions) {
        Holder<Region> regionHolder = regions.getOrThrow(region);

        bootstrap.register(
                ResourceKey.create(DataPackRegistryHandler.REGION_BOUNDARY_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, region.identifier().getPath() + "_bounds")),
                new RegionBoundary(regionHolder, boundaryCoordinates)
        );
    }

    private static void registerDialogueTree(BootstrapContext<DialogueTree> bootstrap, Identifier dialogueTreeIdentifier, DialogueTree dialogueTree) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY, dialogueTreeIdentifier), dialogueTree);
    }


    private static DialogueTreeOrigin createDialogueOrigin(int priority, DialogueNode dialogueNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags) {
        return new DialogueTreeOrigin(priority, dialogueNode, Optional.of(new DialogueFlagRequirements(requiredFlags, excludedFlags)), List.of());
    }

    /**
     * @return a dialogue tree origin with itself as a flag along with any extra flags to be tracked
     */
    private static DialogueTreeOrigin createDialogueOriginWithSelfFlag(int priority, DialogueNode dialogueNode, List<Identifier> requiredFlags, List<Identifier> excludedFlags, List<Identifier> extraSetFlags, Identifier treeOriginIdentifier) {
        List<Identifier> combinedSetFlags = new ArrayList<>(1 + extraSetFlags.size());
        combinedSetFlags.add(treeOriginIdentifier);
        combinedSetFlags.addAll(extraSetFlags);

        return new DialogueTreeOrigin(priority, dialogueNode, Optional.of(new DialogueFlagRequirements(requiredFlags, excludedFlags)), combinedSetFlags);
    }

    /**
     * @return a dialogue tree origin with itself as a flag to be tracked
     */
    private static DialogueTreeOrigin createDialogueOriginWithSelfFlag(int priority, DialogueNode dialogueNode, Identifier treeOriginIdentifier) {
        return new DialogueTreeOrigin(priority, dialogueNode, List.of(treeOriginIdentifier));
    }

    private static void registerQuest(BootstrapContext<Quest> bootstrap, Identifier questIdentifier, QuestTypes questTypes, DialogueTriggeredEvent questCompleteEvent) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier), new Quest(questTypes, Optional.of(questCompleteEvent)));
    }

    private static void registerQuest(BootstrapContext<Quest> bootstrap, Identifier questIdentifier, QuestTypes questTypes) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.QUEST_REGISTRY_KEY, questIdentifier), new Quest(questTypes));
    }

    private static void createRegenPathWithBlocks(BootstrapContext<RegenPath> bootstrap, String identifier, List<Block> blocks, int regenTicks) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, identifier)),
                new RegenPath(blocks.stream().map(Block::defaultBlockState).toList(), regenTicks));
    }

    private static void createRegenPath(BootstrapContext<RegenPath> bootstrap, String identifier, List<BlockState> blocks, int regenTicks, int stagesIncremented) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, identifier)),
                new RegenPath(blocks, regenTicks, stagesIncremented));
    }

    private static void createRegenPath(BootstrapContext<RegenPath> bootstrap, String identifier, List<BlockState> blocks, int regenTicks) {
        bootstrap.register(ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, Identifier.fromNamespaceAndPath(MODID, identifier)),
                new RegenPath(blocks, regenTicks));
    }
}
