package io.github.moosyu.data.attachments;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

public final class PlayerStateAttachment {
    // index 0 and 1 are stats' current values and 2 and 3 are max values so they can sync with proper timing
    private final double[] stats = new double[Stat.values().length * 2];
    // unsynced
    private boolean cancelKnockback = false;
    private boolean failedMessageFired = false;
    private boolean dialogueOpen = false;
    // unsynced
    private int invulnerableTime = 0;
    // unsynced
    private double lastHitAmount = 0.0d;
    private int lastUpdatedStat = -1;

    public enum Stat {
        HEALTH,
        MANA
    }

    public PlayerStateAttachment() {
        stats[Stat.HEALTH.ordinal()] = 0.0f;
        stats[Stat.MANA.ordinal()] = 0.0f;
    }

    public void setStats(double[] newStats) {
        System.arraycopy(newStats, 0, stats, 0, stats.length);
    }

    public double getStatValue(Stat stat) {
        return stats[stat.ordinal()];
    }

    private static int maxIndex(Stat stat) {
        return stat.ordinal() + Stat.values().length;
    }

    public double getMaxStatValue(Stat stat) {
        return stats[maxIndex(stat)];
    }

    /**
     * set a new max value for a stat
     * @param stat stat
     * @param newAmount new max
     * @param player player
     */
    public void setMaxStat(Stat stat, double newAmount, Player player) {
        setStat(newAmount, player, maxIndex(stat));
    }

    public void setStat(double newAmount, Player player, int index) {
        if (stats[index] != newAmount) {
            stats[index] = newAmount;
            lastUpdatedStat = index;
            player.syncData(UnshatteredAttachments.PLAYER_STATE);
        }
    }

    /**
     * set stat to value
     * @param stat the stat to modify
     * @param newAmount the amount to set it to
     * @param player the player having the stat modified
     */
    public void setStatValue(Stat stat, double newAmount, Player player) {
        setStat(newAmount, player, stat.ordinal());
    }

    /**
     * add amount from a stat and syncs it
     * @param stat the stat to modify
     * @param amount the amount to remove
     * @param player the player having the stat modified
     */
    public void increaseStatValue(Stat stat, double amount, Player player) {
        int index = stat.ordinal();
        // the Math.min should return the smaller of the two (so the value doesnt overflow max). very smart but very dangerous.
        stats[index] = (float) Math.min(stats[index] + amount, getMaxStatValue(stat));
        lastUpdatedStat = index;
        player.syncData(UnshatteredAttachments.PLAYER_STATE);
    }

    /**
     * removes amount from a stat and syncs it
     * @param stat the stat to modify
     * @param amount the amount to remove
     * @param player the player having the stat modified
     */
    public void decreaseStatValue(Stat stat, double amount, Player player) {
        int index = stat.ordinal();
        stats[index] -= amount;
        lastUpdatedStat = index;
        player.syncData(UnshatteredAttachments.PLAYER_STATE);
    }

    public void setStatValueByIndex(int index, double value) {
        stats[index] = value;
    }

    public boolean isKnockbackCancelled() {
        return cancelKnockback;
    }

    public void setCancelledKnockback(boolean cancelKnockback) {
        this.cancelKnockback = cancelKnockback;
    }

    public int getInvulnerableTime() {
        return invulnerableTime;
    }

    public void decrementInvulnerableTime() {
        invulnerableTime = Math.max(0, invulnerableTime - 1);
    }

    public void setInvulnerableTime(int invulnerable) {
        this.invulnerableTime = invulnerable;
    }

    public boolean isFailedMessageFired() {
        return failedMessageFired;
    }

    public void setFailedMessageFired(boolean failedMessageFired) {
        this.failedMessageFired = failedMessageFired;
    }

    public boolean isDialogueOpen() {
        return dialogueOpen;
    }

    public void setDialogueOpen(boolean dialogueOpen) {
        this.dialogueOpen = dialogueOpen;
    }

    public double getLastHitAmount() {
        return lastHitAmount;
    }

    public void setLastHitAmount(double lastHitAmount) {
        this.lastHitAmount = lastHitAmount;
    }

    public void writeSync(RegistryFriendlyByteBuf buf, boolean initialSync) {
        boolean fullSync = initialSync || lastUpdatedStat < 0;
        buf.writeBoolean(fullSync);
        if (fullSync) {
            buf.writeInt(stats.length);
            for (double stat : stats) {
                buf.writeDouble(stat);
            }
        } else {
            buf.writeInt(lastUpdatedStat);
            buf.writeDouble(stats[lastUpdatedStat]);
        }
        // cant be bothered optimising this (pretty sure its like 2 bytes anyways)
        buf.writeBoolean(failedMessageFired);
        buf.writeBoolean(dialogueOpen);
    }

    public static PlayerStateAttachment readSync(RegistryFriendlyByteBuf buf, @Nullable PlayerStateAttachment existing) {
        boolean fullSync = buf.readBoolean();
        PlayerStateAttachment attachment = existing != null ? existing : new PlayerStateAttachment();
        if (fullSync) {
            int length = buf.readInt();
            double[] stats = new double[length];

            for (int i = 0; i < length; i++) {
                stats[i] = buf.readDouble();
            }
            attachment.setStats(stats);
        } else {
            int statIndex = buf.readInt();
            double value = buf.readDouble();

            attachment.setStatValueByIndex(statIndex, value);
        }
        attachment.setFailedMessageFired(buf.readBoolean());
        attachment.setDialogueOpen(buf.readBoolean());
        return attachment;
    }
}