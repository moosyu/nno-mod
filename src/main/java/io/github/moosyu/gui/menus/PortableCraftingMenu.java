package io.github.moosyu.gui.menus;

import io.github.moosyu.recipes.CraftingCountClientCache;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import org.jspecify.annotations.NonNull;

public class PortableCraftingMenu extends CraftingMenu {
    final Inventory inventory;

    public PortableCraftingMenu(int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(containerId, inventory, access);

        this.inventory = inventory;
    }

    @Override
    public void slotsChanged(@NonNull Container container) {
        System.out.println("slots changed");
        super.slotsChanged(container);
        if (inventory.player.level().isClientSide()) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                CraftingCountClientCache.setSlotCount(i, container.getItem(i).count());
            }
        }
    }

    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}
