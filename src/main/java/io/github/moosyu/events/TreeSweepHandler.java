package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.UnshatteredDataMaps;
import io.github.moosyu.data.regen.RegenSavedData;
import io.github.moosyu.items.ItemRange;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_SKILLS;

// shouldnt need to check if serverside as this will being run from BlockBreakHandler which already had that check
@EventBusSubscriber(modid = MODID)
public class TreeSweepHandler {
    private static final int BREAK_COOLDOWN_MAX = 2;
    private static final List<TreeBreakInstance> ACTIVE_BREAKS = new ArrayList<>();
    private record BreakTask(Level level, BlockPos pos, Player player, BlockState state) {}

    private static class TreeBreakInstance {
        private final List<BreakTask> tasks;
        private int listPos = 0;
        private int cooldown = BREAK_COOLDOWN_MAX;

        public TreeBreakInstance(List<BreakTask> tasks) {
            this.tasks = tasks;
        }

        private boolean tick() {
            if (listPos >= tasks.size()) {
                finish();
                return true;
            }

            if (cooldown > 0) {
                cooldown--;
                return false;
            }

            BreakTask current = tasks.get(listPos);
            Level level = current.level();
            BlockPos pos = current.pos();

            if (!level.isEmptyBlock(pos)) {
                ServerLevel serverLevel = (ServerLevel) level;
                serverLevel.getDataStorage().computeIfAbsent(RegenSavedData.ID).destroyRegeneratingBlock(pos, serverLevel);
            }

            listPos++;
            cooldown = BREAK_COOLDOWN_MAX;

            return false;
        }

        private void finish() {
            // unless something has gone horribly wrong the "player" value in tasks should be the same in every index
            Player player = tasks.getFirst().player();
            // a little more sketchy but i probably wont mix and match logs so this should be fine
            Block logItem = tasks.getFirst().level.getBlockState(tasks.getFirst().pos()).getBlock();
            PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
            float expReward = Objects.requireNonNullElse(BuiltInRegistries.BLOCK.wrapAsHolder(logItem).getData(UnshatteredDataMaps.HARVESTABLE_BLOCKS_EXP_DATA), 0.0f);

            for (BreakTask current : tasks) {
                giveHarvestedLogs(player, current.state.getBlock());
            }

            skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, tasks.size() * expReward, player);
            player.syncData(PLAYER_SKILLS);
        }
    }

    private static int calculateLogs(Player player, int dropAmount) {
        UnshatteredUtils.triggerInstantPassiveAbilities((ServerPlayer) player, null);
        int itemCount = UnshatteredUtils.getItemsCount(player.getAttributeValue(UnshatteredAttributeValues.FORAGING_FORTUNE.holder), dropAmount);
        UnshatteredUtils.finishInstantPassiveAbilities((ServerPlayer) player, null);
        return itemCount;
    }

    public static void trySweep(Level level, BlockPos startPos, Player player) {
        PlayerSkillsAttachment skills = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        BlockState startBlock = level.getBlockState(startPos);

        // removing the initial block (as the vanilla block break is cancelled)
        ServerLevel serverLevel = (ServerLevel) level;
        serverLevel.getDataStorage().computeIfAbsent(RegenSavedData.ID).destroyRegeneratingBlock(startPos, serverLevel);

        int sweep = (int) player.getAttributeValue(UnshatteredAttributeValues.SWEEP.holder);
        if (sweep <= 0) {
            skills.addExp(PlayerSkillsAttachment.Skill.FORAGING, 6.0f, player);
            player.syncData(PLAYER_SKILLS);
            giveHarvestedLogs(player, startBlock.getBlock());
            return;
        }

        Queue<BreakTask> result = breakConnectedLogs(level, startPos, player, sweep);
        result.add(new BreakTask(level, startPos, player, startBlock));
        ACTIVE_BREAKS.add(new TreeBreakInstance(new ArrayList<>(result)));
    }

    private static void giveHarvestedLogs(Player player, Block brokenBlock) {
        ItemRange itemRange = brokenBlock.defaultBlockState().getData(UnshatteredDataMaps.BREAKABLE_DROPS_DATA);
        if (itemRange == null) return;

        UnshatteredUtils.givePlayerHarvestedItemStack(player, new ItemStack(itemRange.item(), calculateLogs(player, itemRange.getDropAmount(player.getRandom()))));
    }

    private static Queue<BreakTask> breakConnectedLogs(Level level, BlockPos startPos, Player player, int sweep) {
        Queue<BreakTask> toBreak = new ArrayDeque<>();
        Queue<Long> queue = new LinkedList<>();
        LongOpenHashSet visited = new LongOpenHashSet();
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();

        queue.add(startPos.asLong());
        visited.add(startPos.asLong());

        while (!queue.isEmpty()) {
            long currentLong = queue.poll();
            BlockPos currentPos = BlockPos.of(currentLong);

            // this 3d business is messed up, looping through 26 is pretty slow so that's why im getting all wacky and wild
            // by using mutablePos and LongOpenHashSet (longs are primatives so faster). still not fast but not the worst thing ever
            // also future me don't change this to 0 -> < 2, i know it looks weird but it needs to be this way for MutableBlockPos#set
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -1; z <= 1; z++) {
                        if (x == 0 && y == 0 && z == 0) continue;

                        mutablePos.set(currentPos.getX() + x,currentPos.getY() + y,currentPos.getZ() + z);
                        long neighborLong = mutablePos.asLong();

                        if (!visited.contains(neighborLong)) {
                            visited.add(neighborLong);
                            BlockState state = level.getBlockState(mutablePos);
                            if (state.is(BlockTags.LOGS)) {
                                if (toBreak.size() >= sweep || toBreak.size() >= 35)
                                    return toBreak;
                                queue.add(neighborLong);
                                toBreak.add(new BreakTask(level, mutablePos.immutable(), player, state));
                            }
                        }
                    }
                }
            }
        }
        return toBreak;
    }

    @SubscribeEvent
    private static void onServerTickEvent(ServerTickEvent.Post event) {
        // so multiplayer doesnt get all wild and wacky with everyone having the same queue
        Iterator<TreeBreakInstance> iterator = ACTIVE_BREAKS.iterator();

        while (iterator.hasNext()) {
            TreeBreakInstance instance = iterator.next();
            boolean finished = instance.tick();

            if (finished) {
                iterator.remove();
            }
        }
    }
}