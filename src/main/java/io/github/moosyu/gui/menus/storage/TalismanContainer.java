package io.github.moosyu.gui.menus.storage;

import net.minecraft.core.NonNullList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.common.util.ValueIOSerializable;

public class TalismanContainer extends SimpleContainer implements ValueIOSerializable {
    public static final int TALISMAN_SLOTS_MAX = 27;

    public TalismanContainer() {
        super(TALISMAN_SLOTS_MAX);
    }

    @Override
    public void serialize(ValueOutput output) {
        output.store("items", NonNullList.codecOf(ItemStack.OPTIONAL_CODEC), this.getItems());
    }

    @Override
    public void deserialize(ValueInput input) {
        input.read("items", NonNullList.codecOf(ItemStack.OPTIONAL_CODEC)).ifPresent(items -> {
            for (int i = 0; i < Math.min(items.size(), this.getItems().size()); i++) {
                this.getItems().set(i, items.get(i));
            }
        });
    }
}