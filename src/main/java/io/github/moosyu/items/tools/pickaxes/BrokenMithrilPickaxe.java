package io.github.moosyu.items.tools.pickaxes;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.blocks.UnshatteredBlocks;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import org.jspecify.annotations.Nullable;

import static io.github.moosyu.Unshattered.MODID;

public class BrokenMithrilPickaxe extends Item implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "mithril_speed");

    public BrokenMithrilPickaxe(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.PICKAXE)
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 500)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true))
                .attributes(ItemAttributeModifiers.builder()
                        .add(UnshatteredAttributeValues.DAMAGE.holder,
                                new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "broken_mithril_pickaxe_damage"), 4, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.BREAKING_POWER.holder,
                                new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "broken_mithril_pickaxe_breaking_power"), 4, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(UnshatteredAttributeValues.MINING_SPEED.holder,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("broken_mithril_pickaxe_mining_speed"), 11, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).add(Attributes.ATTACK_SPEED,
                                new AttributeModifier(UnshatteredUtils.getUnshatteredIdentifier("broken_mithril_pickaxe_attack_speed"), -2.8, AttributeModifier.Operation.ADD_VALUE),
                                EquipmentSlotGroup.MAINHAND
                        ).build()
                )
        );
    }

    @Override
    public void onAbilityTriggered(ServerPlayer player, @Nullable LivingEntity target) {
        AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
        if (miningSpeed != null) {
            miningSpeed.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, 20, AttributeModifier.Operation.ADD_VALUE));
        }
    }

    @Override
    public void onAbilityFinished(ServerPlayer player, @Nullable LivingEntity target) {
        AttributeInstance miningSpeed = player.getAttribute(UnshatteredAttributeValues.MINING_SPEED.holder);
        if (miningSpeed != null) {
            miningSpeed.removeModifier(ABILITY_IDENTIFIER);
        }
    }

    @Override
    public boolean abilityConditionsMet(ServerPlayer player, @Nullable LivingEntity target) {
        return UnshatteredUtils.getLookedAtBlock(player, player.getAttributeValue(Attributes.BLOCK_INTERACTION_RANGE))
                .map(blockHitResult -> player.level().getBlockState(blockHitResult.getBlockPos()).is(UnshatteredBlocks.BREAKABLE_COAL_ORE_BLOCK))
                .orElse(false);
    }

    @Override
    public boolean ticked() {
        return true;
    }
}
