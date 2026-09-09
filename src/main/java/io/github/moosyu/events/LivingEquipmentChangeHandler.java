package io.github.moosyu.events;

import io.github.moosyu.data.attachments.PlayerAbilityEffectsAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.components.SkillRequirement;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.PassiveAbilityItem;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;

import static io.github.moosyu.Unshattered.MODID;

@EventBusSubscriber(modid = MODID)
public class LivingEquipmentChangeHandler {
    @SubscribeEvent
    public static void onLivingEquipmentChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof Player player) {
            ItemStack oldItem = event.getFrom();
            ItemStack newItem = event.getTo();
            // duplicate from ResultSlotMixin, this handles things like right clicks but less elegantly so i include both
            if (!UnshatteredUtils.passesEquipmentSkillCheck(player, newItem)) {
                player.setItemSlot(event.getSlot(), oldItem.copy());

                if (!newItem.isEmpty() && !player.getInventory().add(newItem.copy())) {
                    player.drop(newItem.copy(), false);
                }

                return;
            }

            if (!player.level().isClientSide() && player instanceof ServerPlayer serverPlayer) {
                PlayerAbilityEffectsAttachment abilityEffects = player.getData(UnshatteredAttachments.PLAYER_ABILITIES);

                if (event.getFrom().getItem() instanceof PassiveAbilityItem oldAbilityItem) {
                    abilityEffects.removeStoredPassiveTickedItem(oldAbilityItem, serverPlayer);
                }

                if (event.getTo().getItem() instanceof PassiveAbilityItem newAbilityItem) {
                    abilityEffects.addStoredPassiveTickedItem(newAbilityItem, serverPlayer);
                }
            }
        }
    }
}
