package io.github.moosyu.items.talismans;

import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.rarities.UnshatteredRarities;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public class BatTalisman extends TalismanItem implements PassiveAbilityItem {
    public BatTalisman(Properties properties) {
        super(properties.component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("bat_talisman_leech"), 0, 0, 0, true))
                .component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 10000)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }

    @Override
    public void onAbilityTriggered(ServerPlayer player, @Nullable LivingEntity target) {}

    @Override
    public void onAbilityFinished(ServerPlayer player, @Nullable LivingEntity target) {
        // the target could only be dying at this point not when triggered
        if (target != null && target.isDeadOrDying()) {
            System.out.println(player.getData(UnshatteredAttachments.PLAYER_STATE).getStatValue(PlayerStateAttachment.Stat.HEALTH));
            player.getData(UnshatteredAttachments.PLAYER_STATE).increaseStatValue(PlayerStateAttachment.Stat.HEALTH,
                    UnshatteredUtils.getDefaultAttributes(target).map(supplier -> supplier.getBaseValue(UnshatteredAttributeValues.HEALTH.holder)).orElse(0.0) * 0.02,
                    player
            );
            System.out.println(player.getData(UnshatteredAttachments.PLAYER_STATE).getStatValue(PlayerStateAttachment.Stat.HEALTH));
        }
    }

    @Override
    public boolean abilityConditionsMet(ServerPlayer player, @Nullable LivingEntity target) {
        return true;
    }

    @Override
    public boolean ticked() {
        return false;
    }
}
