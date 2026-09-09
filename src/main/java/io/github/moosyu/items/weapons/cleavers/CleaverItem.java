package io.github.moosyu.items.weapons.cleavers;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

import static io.github.moosyu.Unshattered.MODID;

public class CleaverItem extends Item implements PassiveAbilityItem {
    private static final Identifier ABILITY_IDENTIFIER = Identifier.fromNamespaceAndPath(MODID, "cleaver_cleave");
    private final float radius;
    private final float cleaveDamageFraction;

    public CleaverItem(Properties properties, float radius, float cleaveDamageFraction) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.CLEAVER));
        this.radius = radius;
        this.cleaveDamageFraction = cleaveDamageFraction;
    }

    @Override
    public void onAbilityTriggered(ServerPlayer player, LivingEntity target) {
        Level level = player.level();
        if (level.isClientSide()) return;
        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
        AABB boundingBox = new AABB(target.getX() - this.radius, target.getY() - this.radius, target.getZ() - this.radius, target.getX() + 1 + this.radius, target.getY() + 1 + this.radius, target.getZ() + 1 + this.radius);
        // already miss-using this... sorry but i dont really want to make another attachment and adding like a cleaving state would feel weirder
        abilities.addActiveEffect(
                ABILITY_IDENTIFIER,
                0,
                level,
                null,
                player.getItemInHand(InteractionHand.MAIN_HAND)
        );
        for (Entity entity : level.getEntities(null, boundingBox)) {
            if (entity instanceof LivingEntity && !(entity instanceof Player) && !(entity == target)) {
                UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(attribute -> attribute.addTransientModifier(new AttributeModifier(ABILITY_IDENTIFIER, -(attribute.getValue() - (1 - cleaveDamageFraction)), AttributeModifier.Operation.ADD_VALUE)));
                player.attack(entity);
            }
        }
    }

    @Override
    public void onAbilityFinished(ServerPlayer player, LivingEntity target) {
        PlayerAbilityEffectsAttachment abilities = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
        abilities.removeActiveEffect(ABILITY_IDENTIFIER, player);
        UnshatteredUtils.getAttributeInstance(player, UnshatteredAttributeValues.FINAL_DAMAGE_MODIFIER.holder).ifPresent(attribute -> attribute.removeModifier(ABILITY_IDENTIFIER));
    }

    @Override
    public boolean abilityConditionsMet(ServerPlayer player, LivingEntity target) {
        return !player.getData(UnshatteredAttachments.PLAYER_ABILITIES).hasActiveEffect(ABILITY_IDENTIFIER);
    }

    @Override
    public boolean isOngoing() {
        return false;
    }
}
