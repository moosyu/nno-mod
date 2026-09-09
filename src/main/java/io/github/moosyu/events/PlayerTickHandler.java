package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerRegionAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.attributes.UnshatteredAttributeValues;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.RegionAreas;
import io.github.moosyu.data.regions.RegionTemperatureTypes;
import io.github.moosyu.data.regions.TemperatureTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_STATE;
import static io.github.moosyu.events.DataPackRegistryHandler.REGION_REGISTRY_KEY;

@EventBusSubscriber(modid = MODID)
public class PlayerTickHandler {
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (player.level().isClientSide()) return;
        PlayerStateAttachment state = player.getData(PLAYER_STATE.get());
        double maxHealthValue = player.getAttributeValue(UnshatteredAttributeValues.HEALTH.holder);
        double healthRegenValue = player.getAttributeValue(UnshatteredAttributeValues.HEALTH_REGEN.holder);
        double maxManaValue = player.getAttributeValue(UnshatteredAttributeValues.MANA.holder);
        double manaRegenValue = player.getAttributeValue(UnshatteredAttributeValues.MANA_REGEN.holder);

        // disable hunger effects
        player.getFoodData().setFoodLevel(20);
        player.getFoodData().setSaturation(5.0f);
        //player.getFoodData().setExhaustion(0.0f);

        float currentTemperature = player.getData(UnshatteredAttachments.PLAYER_TEMPERATURE.get());
        if (player.tickCount % 20 == 0) {
            // may become a problem later but probably doesn't need to be updated more than once a second
            RegionAreas.updatePlayerRegion(player);

            PlayerRegionAttachment regionAttachment = player.getData(UnshatteredAttachments.PLAYER_REGION.get());
            Region region = player.level().registryAccess().lookupOrThrow(REGION_REGISTRY_KEY).getValue(regionAttachment.regionKey());
            if (region == null) return;

            float extraTemperatureChange = 0.0f;

            if (player.isOnFire()) {
                extraTemperatureChange += 1.0f;
            } else if (player.isInWater()) {
                extraTemperatureChange -= 0.1f;
            } else if (player.isInRain()) {
                extraTemperatureChange -= 0.01f;
            }

            if ((region.temperatureType() == RegionTemperatureTypes.COMFORTABLE && extraTemperatureChange == 0.0f) || player.isCreative()) {
                float temperatureChange = RegionTemperatureTypes.COMFORTABLE.getRegionTemperatureChange();

                // if the amount is close to the base (37) then just set to the base
                // or else it just kinda gets close forever
                if (Math.abs(TemperatureTypes.BASE_TEMP.getValue() - currentTemperature) <= temperatureChange) {
                    currentTemperature = TemperatureTypes.BASE_TEMP.getValue();
                } else {
                    currentTemperature += currentTemperature < TemperatureTypes.BASE_TEMP.getValue() ? temperatureChange : -temperatureChange;
                }
                player.setData(UnshatteredAttachments.PLAYER_TEMPERATURE.get(), currentTemperature);
            } else {
                currentTemperature += region.temperatureType().getRegionTemperatureChange() + extraTemperatureChange;
                if (currentTemperature < TemperatureTypes.HIGH_TEMP.getValue()
                        && currentTemperature > TemperatureTypes.LOW_TEMP.getValue()
                        || (!region.temperatureType().isRegionSafe()
                        && currentTemperature < TemperatureTypes.MAX_TEMP.getValue()
                        && currentTemperature > TemperatureTypes.MIN_TEMP.getValue())
                ) {
                    player.setData(UnshatteredAttachments.PLAYER_TEMPERATURE.get(), currentTemperature);
                }
            }
        }

        if (currentTemperature < 0) {
            int required = player.getTicksRequiredToFreeze();
            player.setTicksFrozen(Math.max(player.getTicksFrozen(),
                    Mth.clamp(Mth.floor(Mth.map(currentTemperature,
                                    0f,
                                    TemperatureTypes.MIN_TEMP.getValue(),
                                    0f,
                                    required)
                            ),
                            0,
                            required
                    )
            ));
        } else {
            player.setTicksFrozen(Math.max(0, player.getTicksFrozen() - 1));
        }

        // incremental for every 2 seconds
        if (player.tickCount % 40 == 0) {
            double healthGained = (maxHealthValue / 30) * (healthRegenValue / 100);
            double manaGained = (maxManaValue * 0.04) * (manaRegenValue / 100);

            state.increaseStatValue(PlayerStateAttachment.Stat.HEALTH, healthGained, player);
            state.increaseStatValue(PlayerStateAttachment.Stat.MANA, manaGained, player);
        } else {
            state.setStatValue(PlayerStateAttachment.Stat.HEALTH, Math.min(maxHealthValue, state.getStatValue(PlayerStateAttachment.Stat.HEALTH)), player);
            state.setStatValue(PlayerStateAttachment.Stat.MANA, Math.min(maxManaValue, state.getStatValue(PlayerStateAttachment.Stat.MANA)), player);
        }

        state.setMaxStat(PlayerStateAttachment.Stat.HEALTH, maxHealthValue, player);
        state.setMaxStat(PlayerStateAttachment.Stat.MANA, maxManaValue, player);

        int ferocityCooldown = player.getData(UnshatteredAttachments.PLAYER_FEROCITY_COOLDOWN);
        if (ferocityCooldown > 0) {
            player.setData(UnshatteredAttachments.PLAYER_FEROCITY_COOLDOWN, ferocityCooldown - 1);
        }

        state.decrementInvulnerableTime();

        player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).updateEffects((ServerPlayer) player);
    }
}
