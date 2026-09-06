package io.github.moosyu.gui.layers;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.regions.Region;
import io.github.moosyu.data.regions.TemperatureTypes;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.neoforged.neoforge.client.gui.GuiLayer;
import org.jspecify.annotations.NonNull;

public class InfoBarLayer implements GuiLayer {
    private int lastCoins = 0;
    private int displayNewCoinsTicks = 0;
    private int coinChange = 0;
    private ResourceKey<Region> lastRegionKey;
    private float lastTemperature;
    private Component lastText;

    @Override
    public void render(@NonNull GuiGraphicsExtractor graphics, @NonNull DeltaTracker deltaTracker) {
        Minecraft minecraft = Minecraft.getInstance();
        Player player = minecraft.player;
        if (player == null || minecraft.options.hideGui) return;

        int coins = player.getData(UnshatteredAttachments.PLAYER_CURRENCY.get()).coins;
        ResourceKey<Region> regionKey = player.getData(UnshatteredAttachments.PLAYER_REGION.get()).regionKey();
        float playerTemperature = player.getData(UnshatteredAttachments.PLAYER_TEMPERATURE.get());
        TemperatureTypes temperatureType;
        boolean requiresRebuild = false;

        if (coins != lastCoins) {
            requiresRebuild = true;
            // so a change won't appear when the player first joins
            if (lastCoins != 0) {
                displayNewCoinsTicks = 120;
                coinChange = coins - lastCoins;
            }
            lastCoins = coins;
        }

        if (displayNewCoinsTicks > 0) {
            displayNewCoinsTicks--;
            if (displayNewCoinsTicks == 0) {
                requiresRebuild = true;
            }
        }

        if (regionKey != lastRegionKey) {
            requiresRebuild = true;
            lastRegionKey = regionKey;
        }

        if (lastTemperature != playerTemperature) {
            requiresRebuild = true;
            lastTemperature = playerTemperature;
        }

        graphics.fill(0, 0, graphics.guiWidth(), 20, 0x64000000);

        if (requiresRebuild) {
            Region region = Region.getRegion(regionKey, player);

            if (playerTemperature >= TemperatureTypes.HIGH_TEMP.getValue()) {
                temperatureType = TemperatureTypes.HIGH_TEMP;
            } else if (playerTemperature <= TemperatureTypes.LOW_TEMP.getValue()) {
                temperatureType = TemperatureTypes.LOW_TEMP;
            } else {
                temperatureType = TemperatureTypes.BASE_TEMP;
            }

            lastText = Component.empty().append(Component.translatable("gui.text.unshattered.purse"))
                    .append(": ")
                    .append(Component.literal(displayNewCoinsTicks > 0 ? String.format("%,d", coins) + " (" + (coinChange < 0 ? "" : "+") + String.format("%,d", coinChange) + ")" : String.format("%,d", coins)).withColor(0xFFF9A604))
                    .append("  ⏣ ")
                    .append(Component.translatable(Region.getRegionTranslationKey(regionKey)).withColor(region.colour()))
                    .append("  ")
                    .append(Component.literal(temperatureType.getSymbol() + UnshatteredUtils.oneDecimalFormat.format(playerTemperature))
                            .withColor(temperatureType.getColour())
                    );
        }

        graphics.text(minecraft.font,
                lastText,
                6,
                (20 - minecraft.font.lineHeight) / 2,
                0xFFFFFFFF
        );
    }
}