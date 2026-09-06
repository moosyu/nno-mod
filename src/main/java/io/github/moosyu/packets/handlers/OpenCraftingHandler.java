package io.github.moosyu.packets.handlers;

import io.github.moosyu.gui.menus.PortableCraftingMenu;
import io.github.moosyu.packets.OpenCraftingPacket;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public class OpenCraftingHandler {
    public static void handleData(final OpenCraftingPacket data, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer serverPlayer) {
                serverPlayer.openMenu(new SimpleMenuProvider(
                        (containerId, inventory, _) -> new PortableCraftingMenu(containerId, inventory, ContainerLevelAccess.create(serverPlayer.level(), serverPlayer.blockPosition())),
                        Component.translatable("container.unshattered.crafting")
                ));
            }
        });
    }
}
