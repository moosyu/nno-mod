package io.github.moosyu.mixins;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.common.CommonHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ResultSlot.class)
public abstract class ResultSlotMixin {
    @Shadow
    protected abstract void checkTakeAchievements(ItemStack carried);

    @Shadow
    CraftingContainer craftSlots;

    @Inject(method = "onTake", at = @At("HEAD"), cancellable = true)
    private void onTake(Player player, ItemStack carried, CallbackInfo ci) {
        ci.cancel();
        this.checkTakeAchievements(carried);
        CraftingInput.Positioned positionedRecipe = this.craftSlots.asPositionedCraftInput();
        CraftingInput input = positionedRecipe.input();
        int recipeLeft = positionedRecipe.left();
        int recipeTop = positionedRecipe.top();
        CommonHooks.setCraftingPlayer(player);

        for (int y = 0; y < input.height(); ++y) {
            for (int x = 0; x < input.width(); ++x) {
                int slot = x + recipeLeft + (y + recipeTop) * this.craftSlots.getWidth();
                ItemStack itemStack = this.craftSlots.getItem(slot);
                ItemStack replacement = ItemStack.EMPTY;
                if (!itemStack.isEmpty()) {
                    this.craftSlots.removeItem(slot, 1);
                    itemStack = this.craftSlots.getItem(slot);
                }

                if (!replacement.isEmpty()) {
                    if (itemStack.isEmpty()) {
                        this.craftSlots.setItem(slot, replacement);
                    } else if (ItemStack.isSameItemSameComponents(itemStack, replacement)) {
                        replacement.grow(itemStack.getCount());
                        this.craftSlots.setItem(slot, replacement);
                    } else if (!player.getInventory().add(replacement)) {
                        player.drop(replacement, false);
                    }
                } else {
                    this.craftSlots.setItem(slot, replacement);
                }
            }
        }
    }
}
