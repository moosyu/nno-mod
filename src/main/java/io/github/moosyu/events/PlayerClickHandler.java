package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.dialogue.DialogueInteractable;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.TriState;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class PlayerClickHandler {
    public static final Identifier ACTIVE_RIGHT_CLICK = Identifier.fromNamespaceAndPath(MODID, "active_right_click");

    @SubscribeEvent
    public static void onPlayerRightClick(PlayerInteractEvent.RightClickBlock event) {
        BlockPos pos = event.getPos();
        BlockState interactedBlock = event.getLevel().getBlockState(pos);
        Player player = event.getEntity();
        // was thinking about a set or something for this but supposedly thatd be slower. i cant see how but whatever
        if (interactedBlock.is(Blocks.FURNACE)
                || interactedBlock.is(Blocks.BLAST_FURNACE)
                || interactedBlock.is(BlockTags.COPPER_CHESTS)
                || interactedBlock.is(Blocks.ENDER_CHEST)
                || interactedBlock.is(BlockTags.SHULKER_BOXES)
                || interactedBlock.is(Blocks.BARREL)
                || interactedBlock.is(BlockTags.BEDS)
                || interactedBlock.is(BlockTags.ALL_SIGNS)
                || interactedBlock.is(Blocks.RESPAWN_ANCHOR)
                || interactedBlock.is(Blocks.LECTERN)
                || interactedBlock.is(BlockTags.WOODEN_SHELVES)
                || interactedBlock.is(Blocks.REDSTONE_TORCH)
                || interactedBlock.is(Blocks.SMOKER)
                || interactedBlock.is(Blocks.BLAST_FURNACE)
                || interactedBlock.is(Blocks.LOOM)
                || interactedBlock.is(Blocks.GRINDSTONE)
                || interactedBlock.is(Blocks.SMITHING_TABLE)
                || interactedBlock.is(Blocks.FLETCHING_TABLE)
                || interactedBlock.is(Blocks.CARTOGRAPHY_TABLE)
                || interactedBlock.is(Blocks.STONECUTTER)
                || interactedBlock.is(Blocks.BREWING_STAND)
                || interactedBlock.is(Blocks.ENCHANTING_TABLE)
        ) event.setCanceled(true);

          // anvils and crafting tables will have custom logic
//        if (interactedBlock.is(Blocks.CRAFTING_TABLE) || interactedBlock.is(Blocks.ANVIL)) {
//            event.setCanceled(true);
//            player.swing(InteractionHand.MAIN_HAND);
//        }

        // disables block placement
        if (event.getItemStack().getItem() instanceof BlockItem && !player.isCreative()) {
            event.setUseItem(TriState.FALSE);
        }

        // i *think* this was firing on both hands for some reason so i added the event#getHand gate
        if (interactedBlock.getBlock() instanceof DialogueInteractable dialogueBlock && event.getHand() == InteractionHand.MAIN_HAND) {
            PlayerAbilityEffectsAttachment playerAbilityEffectsAttachment = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);
            if (playerAbilityEffectsAttachment.hasActiveEffect(ACTIVE_RIGHT_CLICK) && !event.getLevel().isClientSide()) {
                player.sendSystemMessage(Component.translatable("misc.messages.unshattered.interact_cooldown").withColor(UnshatteredUtils.ERROR_COLOR));
                return;
            }
            dialogueBlock.onDialogueTriggered(player);
            playerAbilityEffectsAttachment.addActiveEffect(ACTIVE_RIGHT_CLICK, 5, player.level(), null);
        }
    }

    @SubscribeEvent
    public static void onPlayerRightClickItem(PlayerInteractEvent.RightClickItem event) {
        if (event.getLevel().isClientSide()) return;
        if (!UnshatteredUtils.passesSkillCheck(event.getEntity(), event.getItemStack())) event.setCanceled(true);
    }

    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event) {
        Level level = event.getLevel();
        if (level.isClientSide()) return;
        if (event.getTarget().is(EntityType.ARMOR_STAND) && !event.getEntity().isCreative()) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onLeftClickBlock(PlayerInteractEvent.LeftClickBlock event) {
        Player player = event.getEntity();
        if (event.getAction() == PlayerInteractEvent.LeftClickBlock.Action.ABORT
                && !player.level().isClientSide()
                && player.getData(UnshatteredAttachments.PLAYER_STATE).isFailedMessageFired()
        ) {
            System.out.println("fire reset");
            player.getData(UnshatteredAttachments.PLAYER_STATE).setFailedMessageFired(false);
            player.syncData(UnshatteredAttachments.PLAYER_STATE);
        }
    }
}