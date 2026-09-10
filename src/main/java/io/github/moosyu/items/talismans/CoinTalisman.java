package io.github.moosyu.items.talismans;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemAbility;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.jspecify.annotations.Nullable;

public class CoinTalisman extends TalismanItem implements PassiveAbilityItem {
    public CoinTalisman(Properties properties) {
        super(properties.component(UnshatteredDataComponents.DESCRIPTION.get(), true)
                .component(UnshatteredDataComponents.SELL_VALUE.get(), 70)
                .component(UnshatteredDataComponents.ABILITY.get(), new ItemAbility(UnshatteredUtils.getUnshatteredIdentifier("coin_talisman_accrual"),
                        0,
                        0,
                        0,
                        true)
                )
        );
    }

    @Override
    public void onAbilityTriggered(ServerPlayer player, @Nullable LivingEntity target) {
        player.getData(UnshatteredAttachments.PLAYER_CURRENCY).addCoins(player.getRandom().nextIntBetweenInclusive(1, 9));
        player.syncData(UnshatteredAttachments.PLAYER_CURRENCY);
    }

    @Override
    public void onAbilityFinished(ServerPlayer player, @Nullable LivingEntity target) {}

    @Override
    public boolean abilityConditionsMet(ServerPlayer player, @Nullable LivingEntity target) {
        // roughly once every two minutes should be true
        return player.getRandom().nextInt(2400) == 0;
    }

    @Override
    public boolean isOngoing() {
        return true;
    }

    @Override
    public boolean isTicked() {
        return true;
    }
}
