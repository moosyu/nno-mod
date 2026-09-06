package io.github.moosyu.gui.menus;

import io.github.moosyu.gui.menus.storage.StorageContainer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class StorageMenu extends AbstractContainerMenu {
    final Inventory inventory;
    final Container container;

    public StorageMenu(int containerId, Inventory inventory) {
        this(containerId, inventory, new SimpleContainer(StorageContainer.STORAGE_SLOTS));
    }

    public StorageMenu(int containerId, Inventory inventory, Container container) {
        super(UnshatteredMenus.STORAGE_MENU_TYPE.get(), containerId);

        this.container = container;
        this.inventory = inventory;

        this.addStandardInventorySlots(inventory, 8, 84);
        this.addInventoryHotbarSlots(inventory, 8, 142);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int i) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
