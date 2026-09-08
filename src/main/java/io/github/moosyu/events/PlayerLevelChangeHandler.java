package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.regen.RegenClientCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.EntityLeaveLevelEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_STATE;

@EventBusSubscriber(modid = MODID)
public class PlayerLevelChangeHandler {
    private static final Set<UUID> pendingAttributeUpdates = new HashSet<>();

    @SubscribeEvent
    public static void onPlayerJoin(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Player player && !player.level().isClientSide()) {
            pendingAttributeUpdates.add(player.getUUID());
            player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).reapplyPassiveTickedEffects((ServerPlayer) player);
        }
    }

    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        
        if (player.level().isClientSide() || !pendingAttributeUpdates.remove(player.getUUID())) return;

        PlayerStateAttachment stats = player.getData(PLAYER_STATE.get());
        AttributeInstance healthAttribute = player.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
        AttributeInstance manaAttribute = player.getAttribute(UnshatteredAttributeValues.MANA.holder);

        if (healthAttribute == null || manaAttribute == null) return;

        stats.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, healthAttribute.getValue(), player);
        stats.setCurrentStat(PlayerStateAttachment.Stat.MANA, manaAttribute.getValue(), player);
    }

    @SubscribeEvent
    public static void onPlayerLeave(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof Player player && player.level().isClientSide()) {
            RegenClientCache.clear();
        } else if (event.getEntity() instanceof ServerPlayer player && !player.level().isClientSide()) {
            player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).forceStopEffects(player);
        }
    }
}
