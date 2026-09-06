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

public class SuperCleaver extends CleaverItem {
    public SuperCleaver(Properties properties) {
        super(properties.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
                        .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_cleave"), 0, 0, 0, true))
                        .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                        .component(UnshatteredDataComponents.SELL_VALUE.get(), 20000)
                        .attributes(ItemAttributeModifiers.builder()
                                .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_damage"), 8, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .add(UnshatteredAttributeValues.STRENGTH.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_strength"), 9, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .add(UnshatteredAttributeValues.CRITICAL_DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_crit_damage"), 5, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "super_cleaver_sword_attack_speed"), -3.0f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .build()
                        ),
                3.0f,
                0.3f
        );
    }
}
