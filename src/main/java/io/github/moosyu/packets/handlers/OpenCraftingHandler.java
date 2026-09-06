package io.github.moosyu.packets.handlers;

import io.github.moosyu.packets.OpenCraftingPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jspecify.annotations.NonNull;

public class OpenCraftingHandler {
    public static void handleData(final OpenCraftingPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId, inventory, _) -> new CraftingMenu(containerId, inventory, ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())) {
                            @Override
                            public boolean stillValid(@NonNull Player player) {
                                return true;
                            }
                        },
                        Component.translatable("container.unshattered.crafting")
                ));
            }
        });
    }
}
