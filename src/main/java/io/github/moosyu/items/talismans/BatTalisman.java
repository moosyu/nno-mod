package io.github.moosyu.items.talismans;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.rarities.UnshatteredRarities;
import net.minecraft.world.item.Item;

// talismans (proper) coming soon i promise
public class BatTalisman extends TalismanItem {
    public BatTalisman(Properties properties) {
        super(properties.component(UnshatteredDataComponents.RARITY.get(), UnshatteredRarities.RARE)
                .component(UnshatteredDataComponents.DESCRIPTION.get(), true)
        );
    }
}
