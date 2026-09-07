package io.github.moosyu.events;

import io.github.moosyu.Unshattered;
import io.github.moosyu.data.regen.RegenClientCache;
import io.github.moosyu.data.regen.RegenPaths;
import io.github.moosyu.data.regen.RegenSavedData;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.datagen.UnshatteredBlockTagsProvider;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.util.*;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;
import static io.github.moosyu.data.regen.RegenPaths.REGEN_IDENTIFIER_BY_BLOCK;

// ran just before a player is to break a block
@EventBusSubscriber(modid = MODID)
public class BlockBreakHandler {
    @SubscribeEvent
    public static void onBlockBreak(BreakBlockEvent event) {
        Player player = event.getPlayer();
        Level level = player.level();
        BlockState blockState = event.getState();
        Holder<Block> blockHolder = blockState.typeHolder();
        float experienceReward = Objects.requireNonNullElse(blockHolder.getData(UnshatteredDataMaps.HARVESTABLE_BLOCKS_EXP_DATA), 0.0f);
        Block block = blockState.getBlock();
        BlockPos blockPos = event.getPos();

        // so you can still break stuff normally in creative
        if (player.isCreative()) return;
        event.setCanceled(true);

        // so the block doesnt flash in and out of existence when broken before the server says what's up
        if (level.isClientSide()) {
            Registry<RegenPaths.RegenPath> registry = level.registryAccess().lookupOrThrow(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY);
            RegenClientCache.State cached = RegenClientCache.get(blockPos);
            BlockState predictedBlockstate = null;

            if (cached != null) {
                RegenPaths.RegenPath regenPath = registry.getValue(cached.pathIdentifier());
                if (regenPath != null) {
                    int newIndex = cached.index() + regenPath.stagesIncremented();
                    if (newIndex < regenPath.path().size()) {
                        RegenClientCache.put(blockPos, cached.pathIdentifier(), newIndex);
                        predictedBlockstate = regenPath.path().get(newIndex);
                    }
                }
            }

            Identifier regenId = REGEN_IDENTIFIER_BY_BLOCK.get(blockState);
            if (regenId != null) {
                ResourceKey<RegenPaths.RegenPath> regenPathResourceKey = ResourceKey.create(DataPackRegistryHandler.REGEN_PATH_REGISTRY_KEY, regenId);
                RegenPaths.RegenPath regenPath = registry.getValue(regenPathResourceKey);
                if (regenPath != null) {
                    int index = regenPath.stagesIncremented();
                    RegenClientCache.put(blockPos, regenPathResourceKey, index);
                    predictedBlockstate = index < regenPath.path().size() ? regenPath.path().get(index) : null;
                }
            }

            level.levelEvent(player, LevelEvent.PARTICLES_DESTROY_BLOCK, blockPos, Block.getId(blockState));
            level.setBlockAndUpdate(blockPos, Objects.requireNonNullElseGet(predictedBlockstate, Blocks.BEDROCK::defaultBlockState));
            UnshatteredUtils.playClientsideSound(player, blockState.getSoundType(level, blockPos, player).getBreakSound(), SoundSource.BLOCKS, 1.5f);
            return;
        }

        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());

        if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_MINING_BLOCKS)) {
            ItemStack blockDrops = getBlockDrop(block, player, UnshatteredAttributeValues.MINING_FORTUNE);

            UnshatteredUtils.givePlayerHarvestedItemStack(player, blockDrops);

            if (experienceReward > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.MINING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
            }

            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.getDataStorage().computeIfAbsent(RegenSavedData.ID).destroyRegeneratingBlock(blockPos, serverLevel);
        } else if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_FARMING_BLOCKS)) {
            ItemStack blockDrops = getBlockDrop(block, player, UnshatteredAttributeValues.FARMING_FORTUNE);
            UnshatteredUtils.givePlayerHarvestedItemStack(player, blockDrops);

            if (experienceReward > 0.0f) {
                skills.addExp(PlayerSkillsAttachment.Skill.FARMING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
            }

            ServerLevel serverLevel = (ServerLevel) level;
            serverLevel.getDataStorage().computeIfAbsent(RegenSavedData.ID).destroyRegeneratingBlock(blockPos, serverLevel);
        } else if (blockState.is(UnshatteredBlockTagsProvider.COLLECTABLE_FORAGING_BLOCKS)) {
            if (blockState.is(BlockTags.FLOWERS)) {
                skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, experienceReward, player);
                player.syncData(PLAYER_SKILLS);
            } else {
                TreeSweepHandler.trySweep(player.level(), blockPos, player);
            }
        }
    }

    // to stop players from attempting to break blocks
    @SubscribeEvent
    public static void modifyBreakSpeed(PlayerEvent.BreakSpeed event) {
        Player player = event.getEntity();
        Optional<BlockPos> blockPos = event.getPosition();
        Level level = player.level();

        if (blockPos.isEmpty()) {
            event.setNewSpeed(0.0f);
            return;
        }

        Holder<Block> block = level.getBlockState(blockPos.get()).typeHolder();

        if (!block.unwrapKey()
                .map(key -> level.registryAccess()
                        .lookupOrThrow(Registries.BLOCK)
                        .getDataMap(UnshatteredDataMaps.BREAKABLE_DROPS_DATA)
                        .containsKey(key))
                .orElse(false)
                || !hasBreakingPowerRequirement(player, level.getBlockState(blockPos.get()).typeHolder())
                || !UnshatteredUtils.passesSkillCheck(player, player.getMainHandItem())
        ) {
            event.setNewSpeed(0.0f);
            return;
        }

        if (block.is(UnshatteredBlockTagsProvider.COLLECTABLE_MINING_BLOCKS)) {
            event.setNewSpeed((float) player.getAttributeValue(UnshatteredAttributeValues.MINING_SPEED.holder));
        }
    }

    /**
     * @param blockBroken the block being broken
     * @param player the player getting the drop (and having their fortune checked)
     * @param fortuneType the type of fortune to be used to calculate the drop amount
     * @return item drop (with fortune calculation)
     */
    private static ItemStack getBlockDrop(Block blockBroken, Player player, UnshatteredAttributeValues fortuneType) {
        ItemRange drop = BuiltInRegistries.BLOCK.wrapAsHolder(blockBroken).getData(UnshatteredDataMaps.BREAKABLE_DROPS_DATA);
        if (drop == null) {
            Unshattered.LOGGER.warn("{} doesn't have a drop but it was broken!", blockBroken.getName());
            return ItemStack.EMPTY;
        }

        return new ItemStack(drop.item(), UnshatteredUtils.getItemsCount(player.getAttributeValue(fortuneType.holder), drop.getDropAmount(player.getRandom())));
    }

    /**
     * check if a player can break a given block based on their breaking power, sending a message if not
     * @param player player breaking the block
     * @param block block attempting to be broken
     * @return whether the block can be broken by the player
     */
    public static boolean hasBreakingPowerRequirement(Player player, Holder<Block> block) {
        int requiredBreakingPower = Objects.requireNonNullElse(block.getData(UnshatteredDataMaps.BLOCK_BREAKING_POWER_DATA), 0);
        int playerBreakingPower = (int) player.getAttributeValue(UnshatteredAttributeValues.BREAKING_POWER.holder);
        if (playerBreakingPower >= requiredBreakingPower) return true;
        else if (!player.level().isClientSide()) {
            PlayerStateAttachment playerStateAttachment = player.getData(UnshatteredAttachments.PLAYER_STATE);
            if (!playerStateAttachment.isFailedMessageFired()) {
                player.sendSystemMessage(
                        Component.translatable(player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() ? "breaking_power.messages.unshattered.hand" : "breaking_power.messages.unshattered.tool")
                                .append(Component.translatable("breaking_power.messages.unshattered.generic_breaking_power_requirment_start"))
                                .append(Component.literal(requiredBreakingPower + UnshatteredAttributeValues.BREAKING_POWER.symbol).withColor(UnshatteredAttributeValues.BREAKING_POWER.color))
                                .append(Component.translatable("breaking_power.messages.unshattered.generic_breaking_power_requirment_end"))
                                .append(".")
                );
            }
            playerStateAttachment.setFailedMessageFired(true);
            player.syncData(UnshatteredAttachments.PLAYER_STATE);

        }
        return false;
    }
}