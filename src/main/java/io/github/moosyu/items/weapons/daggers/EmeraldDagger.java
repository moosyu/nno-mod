package io.github.moosyu.items.weapons.daggers;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredInstantPassiveAbilityItem;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.Nullable;

import static io.github.moosyu.Unshattered.MODID;

public class EmeraldDagger extends DaggerItem implements UnshatteredInstantPassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = UnshatteredUtils.getUnshatteredIdentifier("emerald_dagger_greed");

    public EmeraldDagger(Properties properties) {
        super(properties
                .component(UnshatteredDataComponents.RARITY, UnshatteredRarities.EPIC)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true))
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "emerald_dagger_damage"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(UnshatteredAttributeValues.FEROCITY.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "emerald_dagger_ferocity"), 40, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "emerald_dagger_attack_speed"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                        .build()
                ));
    }

    @Override
    public void onAbilityTriggered(Player player, @Nullable LivingEntity target) {
        UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
                .ifPresent(attribute -> attribute
                        .addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER,
                                0.5 * Math.pow(player.getData(UnshatteredAttachments.PLAYER_CURRENCY).getCoins(), 0.25),
                                AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                        )
                );
    }

    @Override
    public void onAbilityFinished(Player player, @Nullable LivingEntity target) {
        UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(attribute -> attribute.removeModifier(ABILITY_IDENTIFIER));
    }

    @Override
    public boolean abilityConditionsMet(Player player, @Nullable LivingEntity target) {
        return target != null;
    }
}
