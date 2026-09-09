package io.github.moosyu.data.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public final class PlayerCurrencyAttachment {
    public int coins;
    public int motes;
    public int bankedCoins;

    PlayerCurrencyAttachment(int coins, int motes, int bankedCoins) {
        this.coins = coins;
        this.motes = motes;
        this.bankedCoins = bankedCoins;
    }

    public int getCoins() {
        return coins;
    }

    /**
     * set coins in purse, doesnt sync
     * @param coins amount to set as
     */
    public void setCoins(int coins) {
        this.coins = coins;
    }

    /**
     * adds coins to purse, doesnt sync
     * @param amount amount to add
     */
    public void addCoins(int amount) {
        coins += amount;
    }

    /**
     * remove coins for purse, doesnt sync
     * @param amount amount to remove
     */
    public void removeCoins(int amount) {
        coins -= amount;
    }

    public int getBankedCoins() {
        return coins;
    }

    public void setBankedCoins(int coins) {
        this.coins = coins;
    }

    public void addBankedCoins(int amount) {
        coins += amount;
    }

    public void removeBankedCoins(int amount) {
        coins -= amount;
    }

    public int getMotes() {
        return motes;
    }

    public void setMotes(int motes) {
        this.motes = motes;
    }

    public static final Codec<PlayerCurrencyAttachment> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.INT.fieldOf("coins").forGetter(PlayerCurrencyAttachment::getCoins),
                    Codec.INT.fieldOf("motes").forGetter(PlayerCurrencyAttachment::getMotes),
                    Codec.INT.fieldOf("banked_coins").forGetter(PlayerCurrencyAttachment::getBankedCoins)
            ).apply(instance, PlayerCurrencyAttachment::new)
    );

    public static final StreamCodec<ByteBuf, PlayerCurrencyAttachment> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, PlayerCurrencyAttachment::getCoins,
            ByteBufCodecs.INT, PlayerCurrencyAttachment::getMotes,
            ByteBufCodecs.INT, PlayerCurrencyAttachment::getBankedCoins,
            PlayerCurrencyAttachment::new
    );
}