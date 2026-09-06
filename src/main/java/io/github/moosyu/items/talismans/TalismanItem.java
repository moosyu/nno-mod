package io.github.moosyu.items.talismans;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import net.minecraft.world.item.Item;

public class TalismanItem extends Item {
    public TalismanItem(Properties properties) {
        super(properties.stacksTo(1).component(UnshatteredDataComponents.ITEM_TYPE.get(), ItemTypes.TALISMAN));
    }
}
