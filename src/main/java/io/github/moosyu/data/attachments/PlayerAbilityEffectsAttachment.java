package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.items.PassiveAbilityItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public final class PlayerAbilityEffectsAttachment {
    private final Map<Identifier, ActiveEffectEntry> activeEffects = new HashMap<>();
    private record ActiveEffectEntry(long expiryTime, @Nullable Consumer<ServerPlayer> onExpire, ItemStack itemStack) {}
    // the only serialized part
    private final Set<PassiveAbilityItem> storedPassiveTickedItems = new LinkedHashSet<>();

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
     * adds a passive tick item to the set and triggers its ability
     * @param item the item
     * @param player the player having the effect added
     */
    public void addStoredPassiveTickedItem(PassiveAbilityItem item, ServerPlayer player) {
        if (item.ticked() && storedPassiveTickedItems.add(item)) {
            item.onAbilityTriggered(player, null);
        }
    }

    /**
     * removes a passive ticked item from the set and if successful also finishes the effect
     * @param item the item
     * @param player the player
     */
    public void removeStoredPassiveTickedItem(PassiveAbilityItem item, ServerPlayer player) {
        if (storedPassiveTickedItems.remove(item)) {
            item.onAbilityFinished(player, null);
        }
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

        Iterator<PassiveAbilityItem> passiveIterator = storedPassiveTickedItems.iterator();
        while (passiveIterator.hasNext()) {
            PassiveAbilityItem item = passiveIterator.next();
            if (!item.abilityConditionsMet(player, null)) {
                item.onAbilityFinished(player, null);
                passiveIterator.remove();
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

        storedPassiveTickedItems.forEach(item -> item.onAbilityFinished(player, null));

        activeEffects.clear();
    }

    /**
     * reapply passive ticked effects after leaving and rejoining level
     * @param player server player having effects reapplied
     */
    public void reapplyPassiveTickedEffects(ServerPlayer player) {
        Iterator<PassiveAbilityItem> iterator = storedPassiveTickedItems.iterator();
        while (iterator.hasNext()) {
            PassiveAbilityItem item = iterator.next();
            if (item.abilityConditionsMet(player, null)) {
                item.onAbilityTriggered(player, null);
            } else {
                iterator.remove();
            }
        }
    }

    public static final Codec<PassiveAbilityItem> PASSIVE_ABILITY_ITEM_CODEC = BuiltInRegistries.ITEM.byNameCodec()
            .comapFlatMap(
                    item -> item instanceof PassiveAbilityItem passive
                            ? DataResult.success(passive)
                            : DataResult.error(() -> BuiltInRegistries.ITEM.getKey(item) + " doesnt implement PassiveAbilityItem"),
                    passive -> (Item) passive
            );

    public static final MapCodec<PlayerAbilityEffectsAttachment> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    PASSIVE_ABILITY_ITEM_CODEC.listOf()
                            .fieldOf("stored_passive_ticked_items")
                            .forGetter(attachment -> List.copyOf(attachment.storedPassiveTickedItems))
            ).apply(instance, items -> {
                PlayerAbilityEffectsAttachment attachment = new PlayerAbilityEffectsAttachment();
                attachment.storedPassiveTickedItems.addAll(items);
                return attachment;
            })
    );
}