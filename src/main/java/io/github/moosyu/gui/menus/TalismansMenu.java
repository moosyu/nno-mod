package io.github.moosyu.gui.menus;

import io.github.moosyu.data.attachments.UnshatteredAttachments;
import io.github.moosyu.data.components.UnshatteredDataComponents;
import io.github.moosyu.items.ItemTypes;
import io.github.moosyu.gui.menus.storage.TalismanContainer;
import io.github.moosyu.items.PassiveAbilityItem;
import net.minecraft.server.level.ServerPlayer;
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
    private final int ROWS = 3;
    private final int COLUMNS = 9;
    private final ItemStack[] lastKnownStacks = new ItemStack[TalismanContainer.TALISMAN_SLOTS_MAX];
    private final Inventory playerInventory;
    private final Container container;

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

        for (int i = 0; i < container.getContainerSize(); i++) {
            lastKnownStacks[i] = container.getItem(i).copy();
        }

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLUMNS; col++) {
                this.addSlot(new Slot(container, col + row * 9, 8 + col * 18, 17 + row * 18) {
                    @Override
                    public boolean mayPlace(@NonNull ItemStack itemStack) {
                        if (itemStack.getComponents().get(UnshatteredDataComponents.ITEM_TYPE) == ItemTypes.TALISMAN) {
                            Item placingItem = itemStack.getItem();
                            for (int i = 0; i < container.getContainerSize(); i++) {
                                if (container.getItem(i).is(placingItem)) return false;
                            }
                            return true;
                        }
                        return false;
                    }
                });
            }
        }

        this.addStandardInventorySlots(playerInventory, 8, 84);
        this.addInventoryHotbarSlots(playerInventory, 8, 142);
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

        Player player = playerInventory.player;
        if (player.level().isClientSide() || !(player instanceof ServerPlayer serverPlayer)) return;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack current = container.getItem(i);
            ItemStack previous = lastKnownStacks[i];

            if (!ItemStack.isSameItemSameComponents(previous, current)) {
                if (!current.isEmpty() && current.getItem() instanceof PassiveAbilityItem newAbility) {
                    player.getData(UnshatteredAttachments.PLAYER_ABILITIES).addPassiveItem(newAbility, serverPlayer);
                    System.out.println("stored passive item added");
                }

                if (!previous.isEmpty() && previous.getItem() instanceof PassiveAbilityItem oldAbility) {
                    player.getData(UnshatteredAttachments.PLAYER_ABILITIES).removePassiveItem(oldAbility, serverPlayer);
                    System.out.println("stored passive item removed");
                }

                lastKnownStacks[i] = current.copy();
            }
        }
    }

    @Override
    public @NonNull ItemStack quickMoveStack(@NonNull Player player, int slotIndex) {
        // taken from ChestMenu
        ItemStack clicked = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            clicked = stack.copy();
            if (slotIndex < ROWS * COLUMNS) {
                if (!this.moveItemStackTo(stack, ROWS * COLUMNS, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack, 0, ROWS * COLUMNS, false)) {
                return ItemStack.EMPTY;
            }

            if (stack.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return clicked;
    }

    // pretty sure container.stillValid(player) is always true for simple containers anyways...
    @Override
    public boolean stillValid(@NonNull Player player) {
        return true;
    }
}