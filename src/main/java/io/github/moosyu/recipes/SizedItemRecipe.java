package io.github.moosyu.recipes;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.List;

public record SizedItemRecipe(ItemStackTemplate result, SizedShapedRecipePattern pattern) implements CraftingRecipe {
    public static final MapCodec<SizedItemRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    ItemStackTemplate.CODEC.fieldOf("result").forGetter(SizedItemRecipe::result),
                    SizedShapedRecipePattern.MAP_CODEC.forGetter(SizedItemRecipe::pattern)
            ).apply(instance, SizedItemRecipe::new)
    );

    public static final StreamCodec<RegistryFriendlyByteBuf, SizedItemRecipe> STREAM_CODEC = StreamCodec.composite(
            ItemStackTemplate.STREAM_CODEC, SizedItemRecipe::result,
            SizedShapedRecipePattern.STREAM_CODEC, SizedItemRecipe::pattern,
            SizedItemRecipe::new
    );

    @Override
    public boolean showNotification() {
        return false;
    }

    @Override
    public boolean matches(@NonNull CraftingInput input, @NonNull Level level) {
        return pattern.matches(input);
    }

    @Override
    public @NonNull ItemStack assemble(@NonNull CraftingInput craftingInput) {
        return result.create();
    }

    @Override
    public boolean isSpecial() {
        return true;
    }

    @Override
    public @NonNull String group() {
        return "";
    }

    @Override
    public @NonNull CraftingBookCategory category() {
        return CraftingBookCategory.EQUIPMENT;
    }

    @Override
    public @NonNull RecipeType<CraftingRecipe> getType() {
        return RecipeType.CRAFTING;
    }

    @Override
    public @NonNull RecipeSerializer<? extends CraftingRecipe> getSerializer() {
        return UnshatteredRecipes.SIZED_RECIPE.get();
    }

    @Override
    public @NonNull PlacementInfo placementInfo() {
        return PlacementInfo.create(pattern.ingredients().stream()
                .map(sizedIngredient -> sizedIngredient.map(SizedIngredient::ingredient).orElse(Ingredient.of()))
                .toList());
    }

    @Override
    public @NonNull RecipeBookCategory recipeBookCategory() {
        return RecipeBookCategories.CRAFTING_MISC;
    }
}