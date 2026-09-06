package io.github.moosyu.items.tools.rods;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.UnshatteredInstantPassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredRod extends FishingRodItem implements UnshatteredInstantPassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "fish_out_of_water");

    public UnshatteredRod(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(ABILITY_IDENTIFIER, 0, 0, 0, true)));
    }

    @Override
    public void onAbilityTriggered(Player player, LivingEntity target) {
        UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(finalDamageModifierAttribute -> {
            finalDamageModifierAttribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, -finalDamageModifierAttribute.getValue(), AttributeModifier.Operation.ADD_VALUE));
        });
    }

    @Override
    public void onAbilityFinished(Player player, LivingEntity target) {
        UnshatteredUtils.getAttributeInstance(player,
                UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder)
                .ifPresent(finalDamageModifierAttribute -> finalDamageModifierAttribute.removeModifier(ABILITY_IDENTIFIER));
    }

    @Override
    public boolean abilityConditionsMet(Player player, LivingEntity target) {
        return target != null && !target.is(EntityTypeTags.AQUATIC);
    }
}
