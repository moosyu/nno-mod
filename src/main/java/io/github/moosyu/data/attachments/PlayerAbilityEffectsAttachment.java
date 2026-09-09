package io.github.moosyu.data.attachments;

import io.github.moosyu.gui.menus.storage.TalismanContainer;
import io.github.moosyu.items.PassiveAbilityItem;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public final class PlayerAbilityEffectsAttachment {
    private final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();
    private record ActiveEffectEntry(long expiryTime, @Nullable Consumer<ServerPlayer> onExpire, ItemStack itemStack) {}
    private final Map<PassiveAbilityItem, Boolean> storedPassiveTickedItems = new HashMap<>();
    private final Set<PassiveAbilityItem> storedPassiveNonTickedItems = new HashSet<>();

    /**
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires
     * @param itemStack itemstack being used
     */
    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<ServerPlayer> onExpire, ItemStack itemStack) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire, itemStack));
    }

    /**
     * for adding not-item related active effects
     * @param abilityIdentifier identifier for the ability
     * @param abilityLength length of the ability in ticks
     * @param level server level
     * @param onExpire consumer to run when effect expires
     */
    public void addActiveEffect(Identifier abilityIdentifier, long abilityLength, Level level, Consumer<ServerPlayer> onExpire) {
        activeEffects.put(abilityIdentifier, new ActiveEffectEntry(level.getGameTime() + abilityLength, onExpire, ItemStack.EMPTY));
    }

    /**
     * removes an active effect on the player (for when the effect is finished), removing it from active effects and running its onExpire consumer
     * @param abilityIdentifier identifier for the effect
     * @param player player having the effect removed
     */
    public void removeActiveEffect(Identifier abilityIdentifier, ServerPlayer player) {
        ActiveEffectEntry entry = activeEffects.remove(abilityIdentifier);
        if (entry == null) {
            return;
        }

        if (entry.onExpire() != null) {
            entry.onExpire().accept(player);
        }
    }

    /**
     * checks whether player has an effect active
     * @param abilityIdentifier ability identifier
     * @return true if the player has it
     */
    public boolean hasActiveEffect(Identifier abilityIdentifier) {
        return activeEffects.containsKey(abilityIdentifier);
    }

    /**
     * @return true if the player has any active effects
     */
    public boolean hasAnyActiveEffect() {
        return !activeEffects.isEmpty();
    }

    /**
     * @param abilityIdentifier ability identifier
     * @param level level for game time
     * @return whether or not an active effect has been finished
     */
    public boolean activeEffectFinished(Identifier abilityIdentifier, Level level) {
        if (!hasActiveEffect(abilityIdentifier)) return false;
        // if expiry time is 0 effect should be removed manually
        return level.getGameTime() > activeEffects.get(abilityIdentifier).expiryTime() && activeEffects.get(abilityIdentifier).expiryTime() != 0;
    }

    /**
     * get ticks left until expiration or throws an exception if theres no effect for that identifier
     * @param abilityIdentifier the effect identifier (used to add the effect initially
     * @return the amount of time until effect is to expire
     */
    public long expiryTimeTicks(Identifier abilityIdentifier) {
        ActiveEffectEntry entry = activeEffects.get(abilityIdentifier);
        if (entry == null) {
            throw new IllegalArgumentException("no effect for " + abilityIdentifier);
        }

        return entry.expiryTime();
    }

    /**
     * adds a passive tick item (with active set to true) to map and triggers its ability
     * @param item the item
     * @param player the player having the effect added
     */
    public void addPassiveItem(PassiveAbilityItem item, ServerPlayer player) {
        if (item.ticked()) {
            if (!Boolean.TRUE.equals(storedPassiveTickedItems.put(item, true))) {
                item.onAbilityTriggered(player, null);
            }
        } else {
            storedPassiveNonTickedItems.add(item);
        }
    }

    /**
     * removes a passive ticked item from the set/map and if also runs onAbilityFinished
     * @param item the item
     * @param player the player
     */
    public void removePassiveItem(PassiveAbilityItem item, ServerPlayer player) {
        if (item.ticked()) {
            if (storedPassiveTickedItems.remove(item)) {
                item.onAbilityFinished(player, null);
            }
        } else {
            if (storedPassiveNonTickedItems.remove(item)) {
                item.onAbilityFinished(player, null);
            }
        }
    }

    public Set<PassiveAbilityItem> getStoredPassiveNonTickedItems() {
        return storedPassiveNonTickedItems;
    }

    /**
     * tick player active effects and ensure passive item abilities are still having their conditions met
     * @param player player having effects iterated through
     */
    public void updateEffects(ServerPlayer player) {
        Iterator<Map.Entry<Identifier, ActiveEffectEntry>> currentEffect = activeEffects.entrySet().iterator();
        while (currentEffect.hasNext()) {
            Map.Entry<Identifier, ActiveEffectEntry> entry = currentEffect.next();
            if (activeEffectFinished(entry.getKey(), player.level())) {
                if (entry.getValue().onExpire() != null) {
                    entry.getValue().onExpire().accept(player);
                }
                currentEffect.remove();
            }
        }

        for (Map.Entry<PassiveAbilityItem, Boolean> entry : storedPassiveTickedItems.entrySet()) {
            PassiveAbilityItem item = entry.getKey();
            boolean active = entry.getValue();
            boolean conditionsMet = item.abilityConditionsMet(player, null);

            if (conditionsMet && !active) {
                item.onAbilityTriggered(player, null);
                entry.setValue(true);
            } else if (!conditionsMet && active) {
                item.onAbilityFinished(player, null);
                entry.setValue(false);
            }
        }
    }

    /**
     * runs the ability finishes for active effects and passive ticked items, clearing the active effects map but not the storedPassiveTickedItems set
     * (it needs to be serialized so the effects can be reapplied on rejoin)
     * @param player server player
     */
    public void forceStopEffects(ServerPlayer player) {
        activeEffects.forEach((_, effect) -> {
            if (effect.onExpire != null) {
                effect.onExpire.accept(player);
            }
        });

        storedPassiveTickedItems.forEach((item, active) -> {
            if (active) {
                item.onAbilityFinished(player, null);
            }
        });

        storedPassiveNonTickedItems.forEach(item -> item.onAbilityFinished(player, null));

        activeEffects.clear();
    }

    /**
     * apply passive ticked effects joining, does a full inventory and talisman bag check instead
     * of checking a codec to make sure nothing terrible occured in the last session.
     * @param player server player having effects reapplied
     */
    public void applyPassiveTickedEffects(ServerPlayer player) {
        storedPassiveTickedItems.clear();
        storedPassiveNonTickedItems.clear();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack itemStack = player.getItemBySlot(slot);
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof PassiveAbilityItem item) {
                if (item.ticked()) {
                    if (item.abilityConditionsMet(player, null)) {
                        item.onAbilityTriggered(player, null);
                        storedPassiveTickedItems.put(item, true);
                    } else {
                        storedPassiveTickedItems.put(item, false);
                    }
                } else {
                    storedPassiveNonTickedItems.add(item);
                }
            }
        }

        player.getData(UnshatteredAttachments.PLAYER_TALISMAN_STORAGE.get()).forEach(itemStack -> {
            if (!itemStack.isEmpty() && itemStack.getItem() instanceof PassiveAbilityItem item) {
                if (item.ticked()) {
                    if (item.abilityConditionsMet(player, null)) {
                        item.onAbilityTriggered(player, null);
                        storedPassiveTickedItems.put(item, true);
                    } else {
                        storedPassiveTickedItems.put(item, false);
                    }
                } else {
                    storedPassiveNonTickedItems.add(item);
                }
            }
        });
    }
}