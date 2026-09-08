package io.github.moosyu.events;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.util.damage.DamageUtil.*;

@EventBusSubscriber(modid = MODID)
public class AttackEntityHandler {
    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        Player player = event.getEntity();
        if (!(event.getTarget() instanceof LivingEntity target)
                || player.level().isClientSide()
                || (event.getTarget() instanceof Player)
        ) return;
        event.setCanceled(true);

        ItemStack item = player.getItemInHand(InteractionHand.MAIN_HAND);
        playerDealDamage(player,
                target,
                UnshatteredUtils.triggerInstantPassiveAbility((ServerPlayer) player,
                        target,
                        item.getItem()
                ),
                item.getOrDefault(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.ITEM)
        );
    }

    @SubscribeEvent
    public static void onReachedFerocityHits(ServerTickEvent.Post event) {
        SCHEDULED_FEROCITY_ATTACKS.removeIf(FerocityHit::tick);
    }
}