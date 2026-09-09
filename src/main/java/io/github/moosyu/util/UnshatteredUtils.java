package io.github.moosyu.util;

import io.github.moosyu.Unshattered;
import io.github.moosyu.collectables.CollectableEntries;
import io.github.moosyu.data.attachments.PlayerCollectionsAttachment;
import io.github.moosyu.data.attachments.PlayerSkillsAttachment;
import io.github.moosyu.data.attachments.PlayerStateAttachment;
import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.ItemCharges;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.data.dialogue.DialogueTree;
import io.github.moosyu.events.DataPackRegistryHandler;
import io.github.moosyu.items.PassiveAbilityItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.Tags;
import org.jspecify.annotations.NonNull;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.github.moosyu.Unshattered.MODID;

// generic utilities i use in multiple places
public final class UnshatteredUtils {
    // text stuff
    public static DecimalFormat oneDecimalFormat = new DecimalFormat("0.#");
    public static final int ERROR_COLOR = 0xFFFF5555;

    /**
     *
     * @param input string input to be converted to component
     * @param baseColor text colour to be used when section's colour is unspecified
     * @return parses components to work in bbcode-esque format where you can select colours with [colour=...] [/colour] in hexcode format as well as [i][/i] for itallics
     */
    public static Component parseStyledText(String input, int baseColor) {
        MutableComponent result = Component.empty();
        Matcher matcher = Pattern.compile("\\[colour=(0x[0-9A-Fa-f]+)](.*?)\\[/colour]|\\[i](.*?)\\[/i]").matcher(input);
        int lastEnd = 0;

        while (matcher.find()) {
            if (matcher.start() > lastEnd) {
                String before = input.substring(lastEnd, matcher.start());
                result.append(Component.literal(before).withColor(baseColor));
            }
            if (matcher.group(1) != null) {
                String colorHex = matcher.group(1);
                String text = matcher.group(2);
                int color = (int) Long.parseLong(colorHex.substring(2), 16);

                result.append(Component.literal(text).withColor(color));
            } else if (matcher.group(3) != null) {
                String text = matcher.group(3);
                result.append(Component.literal(text).withColor(baseColor).withStyle(ChatFormatting.ITALIC));
            }
            lastEnd = matcher.end();
        }

        if (lastEnd < input.length()) {
            String remaining = input.substring(lastEnd);
            result.append(Component.literal(remaining).withColor(baseColor));
        }

        return result;
    }

    /**
     * <a href="https://www.geeksforgeeks.org/dsa/converting-decimal-number-lying-between-1-to-3999-to-roman-numerals/">taken from geeksforgeeks 💋</a>
     * @param x the value to be converted to a roman numeral (has to be in the range 1-3999)
     * @return a roman numeral string
     */
    public static String convertTextToRomanNumeral(int x) {
        int[] base = {1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500, 900, 1000};
        String[] sym = {"I", "IV", "V", "IX", "X", "XL", "L", "XC", "C", "CD", "D", "CM", "M"};

        // to store result
        StringBuilder res = new StringBuilder();

        // Loop from the right side to find
        // the largest smaller base value
        int i = base.length - 1;
        while (x > 0) {
            int div = x / base[i];
            while (div > 0) {
                res.append(sym[i]);
                div--;
            }

            // Repeat the process for remainder
            x = x % base[i];
            i--;
        }

        return res.toString();
    }

    /**
     * @param input string that may need conversion
     * @return lowercase string with spaces replaced with underscores
     */
    public static String convertToSnakeCase(String input) {
        return input.replace(" ", "_").toLowerCase();
    }

    // sounds

    /**
     * @param player the player having the sound played
     * @param soundEvent the sound event of the sound (usually taken from SoundEvents)
     * @param soundSource the source of the sound for volume settings
     * @param volume the volume of the sound
     * @param pitch the pitch of the sound
     */
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume, float pitch) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, pitch, false);
        }
    }

    /**
     * @param player the player having the sound played
     * @param soundEvent the sound event of the sound (usually taken from SoundEvents)
     * @param soundSource the source of the sound for volume settings
     * @param volume the volume of the sound
     */
    public static void playClientsideSound(Player player, SoundEvent soundEvent, SoundSource soundSource, float volume) {
        Level level = player.level();
        if (level.isClientSide()) {
            level.playLocalSound(player.getX(), player.getY(), player.getZ(), soundEvent, soundSource, volume, 1.0f, false);
        }
    }

    // misc

    /**
     * helper to get opacity in a more comfortable way. taken from <a href="https://stackoverflow.com/a/28483738">stack overflow</a>.
     * @param color hex colour without opacity set
     * @param opacity opacity, (1.0f is 100% opacity and 0.0f is 0%)
     * @return the hex colour with its opacity modified accordingly
     */
    public static int getOpacityColor(int color, float opacity) {
        return ((int)(opacity * 255) << 24) | (color & 0xFFFFFF);
    }

    /**
     * @param path identifier path
     * @param convertToSnakeCase whether to convert the path to snakecase (for when it's already snakecase)
     * @return an identifier with unshattered's modid as the namespace (like withDefaultNamespace)
     */
    public static Identifier getUnshatteredIdentifier(String path, boolean convertToSnakeCase) {
        return Identifier.fromNamespaceAndPath(MODID, convertToSnakeCase ? convertToSnakeCase(path) : path);
    }

    /**
     * @param path identifier path
     * @return an identifier with unshattered's modid as the namespace (like withDefaultNamespace)
     */
    public static Identifier getUnshatteredIdentifier(String path) {
        return Identifier.fromNamespaceAndPath(MODID, convertToSnakeCase(path));
    }

    /**
     * @param fortuneAmount the fortune of whatever type is being used
     * @param baseDropAmount base drop amount dropped when you break the block (usually itll be one but you never know)
     * @return amount of whatever the player should be given
     */
    public static int getItemsCount(double fortuneAmount, int baseDropAmount) {
        double prevHundred = (Math.floor(fortuneAmount / 100.0));
        int guaranteedDrops = (int) prevHundred + baseDropAmount;
        double nextHundredDiff = fortuneAmount - (prevHundred * 100);

        if (new Random().nextDouble(100.0d) < nextHundredDiff) {
            return guaranteedDrops + 1;
        } else {
            return guaranteedDrops;
        }
    }

    // dialogue

    /**
     * @param dialogueInitiatorName name of whatever started the dialogue, doesn't have to be the actual name of the block/entity
     * @return the identifier for the dialogue tree with the path looking line name/dialogue_tree
     */
    public static Identifier createDialogueTreeIdentifier(String dialogueInitiatorName) {
        return getUnshatteredIdentifier(dialogueInitiatorName + "/" + "dialogue_tree");
    }

    /**
     * @param dialogueTreeIdentifier identifier probably created using {@link #createDialogueTreeIdentifier}
     * @param dialogueNodeName name of the node
     * @return an identifier for the dialogue node which will look something like initatior_name/dialogue_tree/node_nmae
     */
    public static Identifier createDialogueNodeIdentifier(Identifier dialogueTreeIdentifier, @NonNull String dialogueNodeName) {
        return getUnshatteredIdentifier(dialogueTreeIdentifier.getPath() + "/" +  dialogueNodeName);
    }

    /**
     * @param registryAccess registry access
     * @param dialogueTreeIdentifier dialogue tree identifier from {@link #createDialogueTreeIdentifier(String)}
     * @return gets a dialogue tree object or throws an null point exception if it doesnt exist. generally for {@link io.github.moosyu.data.dialogue.DialogueInteractable#getDialogueTree(RegistryAccess)} in {@link io.github.moosyu.data.dialogue.DialogueInteractable}
     */
    public static DialogueTree getDialogueTreeObject(RegistryAccess registryAccess, Identifier dialogueTreeIdentifier) {
        return Objects.requireNonNull(registryAccess.lookupOrThrow(DataPackRegistryHandler.DIALOGUE_TREE_REGISTRY_KEY).getValue(dialogueTreeIdentifier));
    }

    /**
     * adds items to collection while checking to make sure the item isn't empty or missing the collectable data component
     * @param player player getting the item
     * @param itemStack the item being acquired
     */    public static void addItemToCollection(Player player, ItemStack itemStack) {
        if (itemStack.isEmpty()
                || itemStack.count() < 1
                || CollectableEntries.getCollectableEntry(itemStack.typeHolder()) == null
        ) return;

        PlayerCollectionsAttachment collections = player.getData(UnshatteredAttachments.PLAYER_COLLECTIONS.get());

        collections.addPickedUpItem(itemStack, player);
    }

    /**
     * should be used instead of Inventory#add when adding items that were harvested by the player
     * @param player player having the item added
     * @param itemStack itemstack being added to inventory
     */
    public static void givePlayerHarvestedItemStack(Player player, ItemStack itemStack) {
        addItemToCollection(player, itemStack);
        player.getInventory().add(itemStack);
        player.syncData(UnshatteredAttachments.PLAYER_COLLECTIONS);
    }

    // item requirements

    /**
     * @param player the player having their mana checked
     * @param manaCost the mana requirement to do whatever
     * @return true if the player passes false if they dont + text saying the player doesn't meet the requirement
     */
    public static boolean passesManaCheck(Player player, int manaCost) {
        double playerManaAmount = player.getData(UnshatteredAttachments.PLAYER_STATE.get()).getCurrentStat(PlayerStateAttachment.Stat.MANA);
        if (playerManaAmount < manaCost) {
            player.sendSystemMessage(Component.literal("You don't have enough mana to use this " + "(" + Mth.ceil(playerManaAmount) + "/" + manaCost + ").").withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }

    /**
     * @param player the player using the item
     * @param itemCharges the item's charges
     * @param rechargeIdentifier identifier for the recharge ability to check time until expiration
     * @return true if the player passes false if they dont + text saying the player doesn't have any charges and how long they have to wait until it recharges.
     */
    public static boolean passesChargesCheck(Player player, ItemCharges itemCharges, Identifier rechargeIdentifier) {
        if (itemCharges.currentCharges() <= 0) {
            player.sendSystemMessage(
                    Component.literal("You don't have any charges left! Wait " + (((player.getData(UnshatteredAttachments.PLAYER_ABILITIES.get()).expiryTimeTicks(rechargeIdentifier) - player.level().getGameTime()) / 20) + 1) + "s.")
                            .withColor(ERROR_COLOR));
            return false;
        }
        return true;
    }

    // abilities

    /**
     * triggers an item's passive ability if ticked() is false and the conditions are met
     * @param player player having the ability triggered
     * @param target the (optional) target of the ability, obviously if its something like increasing foraging fortune the target is null
     * @param triggeringItem the item that's possibly triggering the passive, might be needed sometimes to make sure the finish isn't run for the wrong item but generally both will be getting run on the same thread
     * @return the ability item or null if the item doesnt have a passive ability
     */
    public static @Nullable PassiveAbilityItem triggerInstantPassiveAbility(ServerPlayer player, @Nullable LivingEntity target, @Nullable Item triggeringItem) {
        if (triggeringItem instanceof PassiveAbilityItem passiveAbilityItem && passiveAbilityItem.abilityConditionsMet(player, target) && !passiveAbilityItem.ticked()) {
            passiveAbilityItem.onAbilityTriggered(player, target);

            return passiveAbilityItem;
        }

        return null;
    }

    /**
     * finishes a unticked passive ability, triggering onAbilityFinished which should reset everything
     * @param player player having the ability finished
     * @param target the (optional) target of the ability, obviously if its something like increasing foraging fortune the target is null
     * @param triggeringItem the item that a passive was triggered for, it's checked if it's null inside before trying to finish so no need to check
     */
    public static void finishInstantPassiveAbility(ServerPlayer player, @Nullable LivingEntity target, @Nullable PassiveAbilityItem triggeringItem) {
        if (triggeringItem != null && !triggeringItem.ticked()) {
            triggeringItem.onAbilityFinished(player, target);
        }
    }

    /**
     * @param player player having the attribute modified/checked
     * @param attribute the attribute to get instance of
     * @return an optional attribute instance of the selected attribute to modify
     */
    public static Optional<AttributeInstance> getAttributeInstance(Player player, Holder<Attribute> attribute) {
        AttributeInstance attributeInstance = player.getAttribute(attribute);
        if (attributeInstance == null) {
            Unshattered.LOGGER.error("{} is null (from getAttributeInstance)", attribute.getRegisteredName());
            return Optional.empty();
        }
        return Optional.of(attributeInstance);
    }

    public static Optional<BlockHitResult> getLookedAtBlock(ServerPlayer player, double reach) {
        Vec3 eyePosition = player.getEyePosition();
        BlockHitResult result = player.level().clip(new ClipContext(eyePosition,
                eyePosition.add(player.getViewVector(1.0F).scale(reach)),
                ClipContext.Block.OUTLINE,
                ClipContext.Fluid.NONE,
                player)
        );

        return result.getType() == HitResult.Type.BLOCK ? Optional.of(result) : Optional.empty();
    }
}
