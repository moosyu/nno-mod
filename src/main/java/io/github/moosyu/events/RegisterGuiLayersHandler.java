package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.gui.layers.*;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;
import net.neoforged.neoforge.client.gui.VanillaGuiLayers;

import static io.github.moosyu.Unshattered.MODID;
import static io.github.moosyu.data.attachments.UnshatteredAttachments.PLAYER_STATE;

@EventBusSubscriber(modid = MODID, value = Dist.CLIENT)
public class RegisterGuiLayersHandler {
    @SubscribeEvent
    public static void onRegisterGuiLayer(RegisterGuiLayersEvent event) {
        event.replaceLayer(VanillaGuiLayers.PLAYER_HEALTH, new BarLayer(0xFFFF030B,
                0xFFFC5454,
                -54,
                -18,
                player -> player.getData(PLAYER_STATE.get()).getStatValue(PlayerStateAttachment.Stat.HEALTH),
                player -> player.getData(PLAYER_STATE.get()).getMaxStatValue(PlayerStateAttachment.Stat.HEALTH))
        );

        event.replaceLayer(VanillaGuiLayers.FOOD_LEVEL, new BarLayer(0xFF00A6FF,
                0xFF4E5BC6,
                54,
                -18,
                player -> player.getData(PLAYER_STATE.get()).getStatValue(PlayerStateAttachment.Stat.MANA),
                player -> player.getData(PLAYER_STATE.get()).getMaxStatValue(PlayerStateAttachment.Stat.MANA))
        );

        event.replaceLayer(VanillaGuiLayers.SELECTED_ITEM_NAME, new SelectedItemLayer());

        event.replaceLayer(VanillaGuiLayers.AIR_LEVEL, new BarLayer(0xFF56B8FF,
                0xFF56B8FF,
                130,
                5,
                player -> Math.max(player.getAirSupply() / 3, 0),
                player -> (double) player.getMaxAirSupply() / 3,
                player -> player.getAirSupply() != player.getMaxAirSupply())
        );

        event.replaceLayer(VanillaGuiLayers.SCOREBOARD_SIDEBAR, new InfoBarLayer());

        event.registerAbove(VanillaGuiLayers.PLAYER_HEALTH, Identifier.fromNamespaceAndPath(MODID, "charges_layer"), new ChargesLayer());

        // hiding some vanilla bits
        event.replaceLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR, (_, _) -> {});
        event.replaceLayer(VanillaGuiLayers.EXPERIENCE_LEVEL, (_, _) -> {});
        event.replaceLayer(VanillaGuiLayers.CONTEXTUAL_INFO_BAR_BACKGROUND, (_, _) -> {});
        event.replaceLayer(VanillaGuiLayers.ARMOR_LEVEL, (_, _) -> {});
    }
}
