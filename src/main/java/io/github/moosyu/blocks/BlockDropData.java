package io.github.moosyu.blocks;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.moosyu.items.ItemRange;

/**
 * a block's drop data
 * @param itemRange block drop's result and range
 * @param dropChance the chance the drop triggers at all
 */
public record BlockDropData(ItemRange itemRange, float dropChance) {
    /**
     * item drop data with 100% drop chance
     * @param itemRange the item range
     */
    public BlockDropData(ItemRange itemRange) {
        this(itemRange, 1.0f);
    }

    public static Codec<BlockDropData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    ItemRange.CODEC.fieldOf("item_range").forGetter(BlockDropData::itemRange),
                    Codec.FLOAT.fieldOf("drop_chance").forGetter(BlockDropData::dropChance)
            ).apply(instance, BlockDropData::new)
    );
}
