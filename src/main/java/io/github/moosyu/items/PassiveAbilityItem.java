package io.github.moosyu.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

// only has code to make this do stuff if its for killing an enemy or during tree sweep, more will be added when required
// not really passive as it only changes something the instant that the ability is to be triggered not always
public interface PassiveAbilityItem {
    /**
     * runs when a passive ability should be fired off
     * @param player player triggering passive
     * @param target target that is possibly required for the ability
     */
    void onAbilityTriggered(ServerPlayer player, @Nullable LivingEntity target);

    /**
     * runs when a passive ability should be ended, should be used to reset things like attribute modifiers
     * @param player player that triggered the passive
     * @param target target that is possibly required for the ability
     */
    void onAbilityFinished(ServerPlayer player, @Nullable LivingEntity target);

    /**
     * @param player player trying to trigger the passive
     * @param target target that is possibly required for the passive
     * @return whether the conditions were met to run the passive effect. for ticked abilities especially this needs to be really quick or stuff will get wild.
     */
    boolean abilityConditionsMet(ServerPlayer player, @Nullable LivingEntity target);

    /**
     * if ticked is false the effect will need to be stopped manually as soon as possible after the effect goes off, probably via {@link io.github.moosyu.util.UnshatteredUtils#finishInstantPassiveAbility(ServerPlayer, LivingEntity, PassiveAbilityItem)}}
     * @return whether ticked is true or not
     */
    boolean ticked();
}
