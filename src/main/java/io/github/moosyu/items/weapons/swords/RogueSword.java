package io.github.moosyu.items.weapons.swords;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jspecify.annotations.NonNull;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID, value = Dist.DEDICATED_SERVER)
public class RogueSword extends UnshatteredSword {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "rogue_sword_speed_boost");
    private static final ItemAbility SPEED_BOOST_ABILITY = new ItemAbility(ABILITY_IDENTIFIER,10, 100, 600, false);

    public RogueSword(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.ABILITY.get(), SPEED_BOOST_ABILITY)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 3)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rogue_sword_damage"), 2, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rogue_sword_attack_speed"), -2.4f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
                )
        );
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (level.isClientSide()) return InteractionResult.FAIL;
        AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        PlayerAbilityEffectsAttachment playerAbilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get());
        if (player.getCooldowns().isOnCooldown(this.getDefaultInstance()) || !UnshatteredUtils.passesManaCheck(player, SPEED_BOOST_ABILITY.manaCost()) || movementSpeedAttribute == null) return InteractionResult.FAIL;
        if (playerAbilities.hasActiveEffect(ABILITY_IDENTIFIER)) {
            playerAbilities.addActiveEffect(ABILITY_IDENTIFIER, SPEED_BOOST_ABILITY.duration(), level, this::onSpeedBoostExpire, player.getItemBySlot(hand.asEquipmentSlot()));
        } else {
            playerAbilities.addActiveEffect(ABILITY_IDENTIFIER, SPEED_BOOST_ABILITY.duration(), level, this::onSpeedBoostExpire, player.getItemBySlot(hand.asEquipmentSlot()));
            movementSpeedAttribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, 0.05, AttributeModifier.Operation.ADD_VALUE));
        }
        if (!player.isCreative()) {
            player.getData(UnshatteredAttachments.PLAYER_STATE.get())
                    .removeCurrentStat(PlayerStateAttachment.Stat.MANA, SPEED_BOOST_ABILITY.manaCost(), player);
        }
        player.getCooldowns().addCooldown(this.getDefaultInstance(), SPEED_BOOST_ABILITY.cooldown());
        return InteractionResult.PASS;
    }

    private void onSpeedBoostExpire(Player player) {
        if (player.level().isClientSide()) return;
        AttributeInstance movementSpeedAttribute = player.getAttribute(Attributes.MOVEMENT_SPEED);
        if (movementSpeedAttribute != null) movementSpeedAttribute.removeModifier(ABILITY_IDENTIFIER);
    }
}
