package io.github.moosyu.items.weapons.cleavers;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.rarities.UnshatteredRarities;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import static io.github.moosyu.Unshattered.MODID;

public class GoldenCleaver extends CleaverItem {
    public GoldenCleaver(Properties properties) {
        super(properties.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.UNCOMMON)
                        .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(Identifier.fromNamespaceAndPath(MODID, "golden_cleaver_cleave"), 0, 0, 0, true))
                        .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                        .component(UnshatteredDataComponents.SELL_VALUE.get(), 80)
                        .attributes(ItemAttributeModifiers.builder()
                                .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_cleaver_damage"), 7, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_cleaver_strength"), 3, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "golden_cleaver_sword_attack_speed"), -3.0f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .build()
                        ),
                3.0f,
                0.2f
        );
    }
}
