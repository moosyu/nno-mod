package io.github.moosyu.gui.menus;

import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.gui.menus.storage.TalismanContainer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jspecify.annotations.NonNull;

public class TalismansMenu extends AbstractContainerMenu {
    final Inventory playerInventory;
    final Container container;

    // client side
    public TalismansMenu(int containerId, Inventory playerInventory) {
        this(containerId, playerInventory, new SimpleContainer(TalismanContainer.TALISMAN_SLOTS_MAX));
    }

    // server side (also now used by client, with a dummy container)
    public TalismansMenu(int containerId, Inventory playerInventory, Container container) {
        super(UnshatteredMenus.TALISMAN_MENU_TYPE.get(), containerId);

        checkContainerSize(container, container.getContainerSize());
        this.container = container;
        this.playerInventory = playerInventory;

        // talisman bag slots
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.addSlot(new Slot(container, col + row * 9, 8 + col * 18, 17 + row * 18) {
                    @Override
                    public boolean mayPlace(@NonNull ItemStack itemStack) {
                        if (itemStack.getComponents().get(UnshatteredDataComponents.ITEM_TYPE) == ItemTypes.TALISMAN) {
                            Item placingItem = itemStack.getItem();
                            for (int i = 0; i < container.getContainerSize(); i++) {
                                ItemStack currentSlotContents = container.getItem(i);
                                if (currentSlotContents.is(placingItem)) return false;
                            }
                        } else return false;

                        return true;
                    }
                });
            }
        }

        this.addStandardInventorySlots(playerInventory, 8, 84);
        this.addInventoryHotbarSlots(playerInventory, 8, 142);
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slot) {
        return ItemStack.EMPTY;
    }

    // pretty sure container.stillValid(player) is always true for simple containers anyways...
    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
