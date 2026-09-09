package io.github.moosyu.commands;

import com.mojang.brigadier.CommandDispatcher;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.server.level.ServerPlayer;

public class StatCommand {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(Commands.literal("health").executes(context -> {
            ServerPlayer player = context.getSource().getPlayer();
            if (player == null) {
                return 0;
            }
            PlayerStateAttachment stats = player.getData(UnshatteredAttachments.PLAYER_STATE.get());
            context.getSource().sendSuccess(() -> net.minecraft.network.chat.Component.literal(String.valueOf(stats.getStatValue(PlayerStateAttachment.Stat.HEALTH))),
                    false);
            return 1;
        }));
    }
}
