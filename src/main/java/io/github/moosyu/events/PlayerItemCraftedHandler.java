package io.github.moosyu.events;

import io.github.moosyu.recipes.SizedItemRecipe;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;
import java.util.Optional;

@EventBusSubscriber
public class PlayerItemCraftedHandler {
    @SubscribeEvent
    public static void onItemCrafted(PlayerEvent.ItemCraftedEvent event) {
        Level level = event.getEntity().level();
        if (level.isClientSide() || !(level instanceof ServerLevel serverLevel)) return;

        Container grid = event.getInventory();
        if (grid instanceof CraftingContainer craftingContainer) {
            CraftingInput input = craftingContainer.asCraftInput();
            RecipeManager recipeManager = serverLevel.getServer().getRecipeManager();

            recipeManager.getRecipeFor(RecipeType.CRAFTING, input, level)
                    .filter(holder -> holder.value() instanceof SizedItemRecipe)
                    .map(holder -> (SizedItemRecipe) holder.value())
                    .ifPresent(recipe -> {
                        List<Optional<SizedIngredient>> patternIngredients = recipe.pattern().ingredients();

                        for (int i = 0; i < patternIngredients.size() && i < grid.getContainerSize(); i++) {
                            final int slot = i;

                            patternIngredients.get(i).ifPresent(sized -> {
                                int required = sized.count();
                                if (required > 1 && !grid.getItem(slot).isEmpty()) {
                                    grid.removeItem(slot, required - 1);
                                }
                            });
                        }

                        grid.setChanged();
                    });
        }
    }
}
