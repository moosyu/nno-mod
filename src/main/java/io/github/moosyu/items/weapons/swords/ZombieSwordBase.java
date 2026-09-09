package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.Unshattered;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.packets.ZombieSwordEffectsPacket;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

public class ZombieSwordBase extends UnshatteredSword {
    private final Identifier abilityIdentifier;
    private final int healAmount;
    private final ItemAbility instantHealAbility;

    public ZombieSwordBase(Properties properties, String identifier, int damage, int strength, int mana, int healAmount, int manaCost, int cooldown) {
        String snakeCaseIdentifier = UnshatteredUtils.convertToSnakeCase(identifier);
        Identifier abilityId = Identifier.fromNamespaceAndPath(MODID, snakeCaseIdentifier + "_instant_heal");
        ItemAbility ability = new ItemAbility(abilityId, manaCost, cooldown, 0, false);

        super(properties.component(UnshatteredDataComponents.ABILITY.get(), ability)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(snakeCaseIdentifier + "_damage", false), damage, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(snakeCaseIdentifier + "_strength", false), strength, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(UnshatteredAttributeValues.MANA.holder, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(snakeCaseIdentifier + "_mana", false), mana, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier(snakeCaseIdentifier + "_attack_speed", false), -2.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
                )
        );

        this.abilityIdentifier = abilityId;
        this.healAmount = healAmount;
        this.instantHealAbility = ability;
    }
    
    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.FAIL;

        AttributeInstance maxHealthAttribute = player.getAttribute(UnshatteredAttributeValues.HEALTH.holder);
        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
        ItemStack itemStack = player.getItemInHand(InteractionHand.MAIN_HAND);
        ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES.get());

        if (maxHealthAttribute == null || itemCharges == null) {
            Unshattered.LOGGER.error("maxHealthAttribute or itemCharges are null (from {})", abilityIdentifier);

            return InteractionResult.FAIL;
        }

        if (!player.isCreative()) {
            PlayerStateAttachment playerState = player.getData(UnshatteredAttachments.PLAYER_STATE.get());

            if (!UnshatteredUtils.passesManaCheck(player, instantHealAbility.manaCost())
                    || player.getCooldowns().isOnCooldown(itemStack)
                    || !UnshatteredUtils.passesChargesCheck(player, itemCharges, abilityIdentifier)) {
                return InteractionResult.FAIL;
            } else {
                itemStack.set(UnshatteredDataComponents.CHARGES.get(), itemCharges.decrementCharges());
                player.getCooldowns().addCooldown(itemStack, instantHealAbility.cooldown());

                if (!abilities.hasActiveEffect(abilityIdentifier)) {
                    abilities.addActiveEffect(abilityIdentifier, itemCharges.rechargeTime(), level, p -> onRecharge(p, itemStack), player.getItemBySlot(hand.asEquipmentSlot()));
                }
                playerState.decreaseStatValue(PlayerStateAttachment.Stat.MANA, instantHealAbility.manaCost(), player);
            }

            playerState.increaseStatValue(PlayerStateAttachment.Stat.HEALTH, healAmount + (maxHealthAttribute.getValue() * 0.05), player);
        }

        PacketDistributor.sendToPlayer((ServerPlayer) player, new ZombieSwordEffectsPacket());

        return InteractionResult.PASS;
    }

    private void onRecharge(Player player, ItemStack itemStack) {
        Level level = player.level();
        ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES.get());

        if (level.isClientSide() || itemCharges == null) return;

        itemStack.set(UnshatteredDataComponents.CHARGES.get(), itemCharges.incrementCharges());
    }

    // so that changing data doesn't do the re-equip animation
    @Override
    public boolean shouldCauseReequipAnimation(@NonNull ItemStack oldStack, @NonNull ItemStack newStack, boolean slotChanged) {
        return slotChanged || oldStack.getItem() != newStack.getItem();
    }

    @Override
    public void inventoryTick(@NonNull ItemStack itemStack, @NonNull ServerLevel level, @NonNull Entity owner, EquipmentSlot slot) {
        if (owner instanceof Player player) {
            PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
            ItemCharges itemCharges = itemStack.get(UnshatteredDataComponents.CHARGES.get());
            if (itemCharges == null) return;

            if (itemCharges.currentCharges() < itemCharges.maxCharges() && !abilities.hasActiveEffect(abilityIdentifier)) {
                abilities.addActiveEffect(abilityIdentifier, itemCharges.rechargeTime(), level, p -> onRecharge(p, itemStack), player.getItemBySlot(slot));
            }
        }
    }
}
