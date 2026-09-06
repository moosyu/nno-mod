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

public class RustyCleaver extends CleaverItem {
    public RustyCleaver(Properties properties) {
        super(properties.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.COMMON)
                        .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(Identifier.fromNamespaceAndPath(MODID, "rusty_cleaver_cleave"), 0, 0, 0, true))
                        .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                        .component(UnshatteredDataComponents.SELL_VALUE.get(), 8)
                        .attributes(ItemAttributeModifiers.builder()
                                .add(UnshatteredAttributeValues.DAMAGE.holder, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rusty_cleaver_damage"), 4, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .add(Attributes.ATTACK_SPEED, new AttributeModifier(Identifier.fromNamespaceAndPath(MODID, "rusty_cleaver_sword_attack_speed"), -3.0f, AttributeModifier.Operation.ADD_VALUE), EquipmentSlotGroup.MAINHAND)
                                .build()
                ),
            2.0f,
            0.1f
        );
    }
}
