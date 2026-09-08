package io.github.moosyu.util.damage;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerCurrencyAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.packets.DamageNumberPacket;
import io.github.moosyu.packets.DeathSoundEffectPacket;
import io.github.moosyu.packets.FerocityEffectPacket;
import io.github.moosyu.packets.WeakHitSoundEffectPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_CURRENCY;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_STATE;

public final class DamageUtil {
    public static final List<FerocityHit> SCHEDULED_FEROCITY_ATTACKS = new ArrayList<>();
    public static final int FEROCITY_COOLDOWN = 4;
    public static final int INVULNERABILITY_TIME_MAX = 20;

    /**
     * runs damage code for a player attacking an entity that factors in unshattered damage attributes and checks skill requirements.
     * make sure it isn't clientside before triggering.
     * @param player player dealing damage
     * @param target target attempting to be damaged
     */
    public static void playerDealDamage(Player player, LivingEntity target, @Nullable PassiveAbilityItem item, ItemTypes itemType) {
        if (!player.isCreative() && target.is(EntityType.ARMOR_STAND)) return;

        SkillRequirement skillRequirement = player.getItemInHand(InteractionHand.MAIN_HAND).get(UnshatteredDataComponents.SKILL_REQUIREMENT);
        PlayerSkillsAttachment playerSkill = player.getData(UnshatteredAttachments.PLAYER_SKILLS.get());
        float attackStrength = player.getAttackStrengthScale(0.0f);
        double critDamage = 0.0d;

        if (skillRequirement != null && skillRequirement.level() > playerSkill.getLevel(playerSkill.getExp(skillRequirement.skill()))) {
            player.sendSystemMessage(Component.literal(Component.translatable(skillRequirement.skill().getTranslationKey()).getString() + " level " + skillRequirement.level() + " is required to use this weapon!").withColor(0xFFFF5555));
            return;
        }

        if (attackStrength >= 0.9f) {
            critDamage = player.getAttributeValue(UnshatteredAttributeValues.CRITICAL_CHANCE.holder) >= (player.getRandom().nextIntBetweenInclusive(0, 101))
                    ? player.getAttributeValue(UnshatteredAttributeValues.CRITICAL_DAMAGE.holder)
                    : 0.0d;
        } else {
            PacketDistributor.sendToPlayer((ServerPlayer) player, new WeakHitSoundEffectPacket());
        }

        double damage = (5 + player.getAttributeValue(UnshatteredAttributeValues.DAMAGE.holder))
                * (1 + (player.getAttributeValue(UnshatteredAttributeValues.STRENGTH.holder) / 100))
                * (1 + (critDamage / 100))
                * player.getAttributeValue(UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
                * attackStrength;
        AttributeInstance targetHealth = target.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
        if (targetHealth != null) {
            if (critDamage > 0.0d) {
                player.crit(target);
                target.playSound(SoundEvents.PLAYER_ATTACK_CRIT, 1.0F, 1.0F);
            }

            if ((targetHealth.getBaseValue() - damage) > 0) {
                targetHealth.setBaseValue(targetHealth.getBaseValue() - damage);
                // fake hit to trigger some of the effects which i cant be bothered replicating
                target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 0.0f);

                double ferocityAmount = player.getAttributeValue(UnshatteredAttributeValues.FEROCITY.holder);
                if (ferocityAmount > 0 && player.getData(UnshatteredAttachments.PLAYER_FEROCITY_COOLDOWN) <= 0) {
                    // shouldn't ever be negative so id hope this is fine
                    int prevHundredPlace = (int) (ferocityAmount / 100.0);
                    double nextHundredDiff = ferocityAmount - (prevHundredPlace * 100);
                    int ferocityHits = prevHundredPlace;

                    if (new Random().nextDouble(100.0d) < nextHundredDiff) {
                        ferocityHits++;
                    }

                    for (int i = 0; i < ferocityHits; i++) {
                        SCHEDULED_FEROCITY_ATTACKS.add(new FerocityHit(target, FEROCITY_COOLDOWN + ((FEROCITY_COOLDOWN / 2) * i), damage, player, i == 0));
                    }

                    player.setData(UnshatteredAttachments.PLAYER_FEROCITY_COOLDOWN, FEROCITY_COOLDOWN * 2);
                }
                // has to be placed after hurt as hurt sets its own invulnerability
                target.invulnerableTime = itemType.getInvulnerability();
            } else {
                targetHealth.setBaseValue(0.0);
                // this should one shot just about any vanilla mob to my knowledge (and actually calculating it wouldnt make sense as custom mobs ill make will have a base normal hp of like 1)
                target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 500.0f);
            }
            PacketDistributor.sendToPlayer((ServerPlayer) player, new DamageNumberPacket((int) damage, target.position()));
        }

        UnshatteredUtils.finishInstantPassiveAbility((ServerPlayer) player, target, item);

        player.resetAttackStrengthTicker();
        if (player.isSprinting()) player.setSprinting(true);
    }

    public static class FerocityHit {
        private final LivingEntity target;
        private int waitTime;
        private final double damage;
        private final Player player;
        private final boolean playSound;

        public FerocityHit(LivingEntity target, int waitTime, double damage, Player player, boolean playSound) {
            this.target = target;
            this.waitTime = waitTime;
            this.damage = damage;
            this.player = player;
            this.playSound = playSound;
        }

        /**
         * @return true if the wait time has hit 0
         */
        public boolean tick() {
            waitTime--;

            if (waitTime <= 0) {
                AttributeInstance targetHealth = target.getAttribute(UnshatteredAttributeValues.HEALTH.holder);

                if (targetHealth != null && (player.isCreative() || !target.is(EntityType.ARMOR_STAND))) {
                    if ((targetHealth.getBaseValue() - damage) > 0) {
                        targetHealth.setBaseValue(targetHealth.getBaseValue() - damage);
                        target.invulnerableTime = 0;
                    } else {
                        targetHealth.setBaseValue(0.0);
                        target.hurtServer((ServerLevel) target.level(), target.damageSources().playerAttack(player), 500.0f);
                    }

                    PacketDistributor.sendToPlayer((ServerPlayer) player, new FerocityEffectPacket(target.getId(), playSound));
                    PacketDistributor.sendToPlayer((ServerPlayer) player, new DamageNumberPacket((int) damage, target.position()));
                }

                return true;
            }

            return false;
        }
    }

    /**
     * deal damage to a player
     * @param player player being damaged
     * @param damageDealt damage being dealt to the player
     * @param level server level
     * @param deathMessage death message if the damage kills the player
     */
    public static void damagePlayer(Player player, double damageDealt, ServerLevel level, Component deathMessage) {
        PlayerStateAttachment states = player.getData(PLAYER_STATE.get());
        double playerHealth = states.getCurrentStat(PlayerStateAttachment.Stat.HEALTH);
        double originalDamage = damageDealt;

        if (states.getInvulnerableTime() > INVULNERABILITY_TIME_MAX / 2) {
            if (damageDealt <= states.getLastHitAmount()) return;
            damageDealt -= states.getLastHitAmount();
        }

        states.setLastHitAmount(originalDamage);
        states.setInvulnerableTime(INVULNERABILITY_TIME_MAX);

        if (playerHealth - damageDealt > 0.0d) {
            states.removeCurrentStat(PlayerStateAttachment.Stat.HEALTH, damageDealt, player);
        } else {
            PlayerCurrencyAttachment currency = player.getData(PLAYER_CURRENCY.get());
            BlockPos spawnPos = level.getRespawnData().pos();

            player.teleportTo(spawnPos.getX() + 0.5, spawnPos.getY(), spawnPos.getZ() + 0.5);
            player.sendSystemMessage(deathMessage.copy()
                    .withStyle(ChatFormatting.RED)
                    .append(Component.literal(" You lost " + (currency.getCoins() / 2) + " coins."))
            );
            states.setCurrentStat(PlayerStateAttachment.Stat.HEALTH, player.getAttributeValue(UnshatteredAttributeValues.HEALTH.holder), player);
            states.setCurrentStat(PlayerStateAttachment.Stat.MANA, player.getAttributeValue(UnshatteredAttributeValues.MANA.holder), player);
            currency.removeCoins(currency.getCoins() / 2);
            player.syncData(PLAYER_CURRENCY.get());
            PacketDistributor.sendToPlayer((ServerPlayer) player, new DeathSoundEffectPacket());
            states.setCancelledKnockback(true);
        }
        player.invulnerableTime = 0;
    }
}
