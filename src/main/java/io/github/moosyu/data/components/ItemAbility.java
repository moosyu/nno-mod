package io.github.moosyu.data.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.Identifier;

/**
 * an item ability, used really only for the corresponding data component which is then used for displaying a correct tooltip
 * @param abilityId the ability identifier
 * @param manaCost mana cost to use the ability (usually 0 if it's passive)
 * @param cooldown ability cooldown
 * @param duration duration of ability
 * @param passive whether the ability is passive
 */
public record ItemAbility(Identifier abilityId, int manaCost, int cooldown, int duration, boolean passive) {
    public static final Codec<ItemAbility> CODEC = RecordCodecBuilder.create((RecordCodecBuilder.Instance<ItemAbility> instance) -> instance.group(
            Identifier.CODEC.fieldOf("ability_id").forGetter(ItemAbility::abilityId),
            Codec.INT.optionalFieldOf("mana_cost", 0).forGetter(ItemAbility::manaCost),
            Codec.INT.optionalFieldOf("cooldown", 0).forGetter(ItemAbility::cooldown),
            Codec.INT.optionalFieldOf("duration", 0).forGetter(ItemAbility::duration),
            Codec.BOOL.fieldOf("passive").forGetter(ItemAbility::passive)
    ).apply(instance, ItemAbility::new)).validate(ability -> {
        if (ability.passive() && ability.manaCost() > 0) {
            return DataResult.error(() -> "Passive abilities cannot have a mana cost");
        }
        if (ability.passive() && ability.cooldown() > 0) {
            return DataResult.error(() -> "Passive abilities cannot have a cooldown");
        }
        if (ability.passive() && ability.duration() > 0) {
            return DataResult.error(() -> "Passive abilities cannot have a duration");
        }
        return DataResult.success(ability);
    });
}