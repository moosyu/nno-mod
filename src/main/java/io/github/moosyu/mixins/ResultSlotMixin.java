package io.github.moosyu.mixins;

import io.github.moosyu.data.recipes.SizedItemRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.ResultSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.common.CommonHooks;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
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
        CommonHooks.setCraftingPlayer(player);

        if (player.level() instanceof ServerLevel serverLevel) {
            CraftingInput.Positioned positionedRecipe = this.craftSlots.asPositionedCraftInput();
            CraftingInput input = positionedRecipe.input();
            int recipeLeft = positionedRecipe.left();
            int recipeTop = positionedRecipe.top();

            serverLevel.recipeAccess().getRecipeFor(RecipeType.CRAFTING, input, serverLevel).ifPresent(recipe -> {
                SizedItemRecipe sizedRecipe = recipe.value() instanceof SizedItemRecipe sized ? sized : null;

                for (int y = 0; y < input.height(); ++y) {
                    for (int x = 0; x < input.width(); ++x) {
                        int slot = x + recipeLeft + (y + recipeTop) * this.craftSlots.getWidth();
                        this.craftSlots.removeItem(slot,
                                sizedRecipe == null ? 1 : sizedRecipe.pattern().ingredients().get(slot).map(SizedIngredient::count).orElse(1)
                        );
                    }
                }
            });
        }
    }
}
