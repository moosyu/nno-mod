package io.github.moosyu.data.recipes;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES = DeferredRegister.create(Registries.RECIPE_TYPE, MODID);

    public static final Supplier<RecipeSerializer<SizedItemRecipe>> SIZED_RECIPE = RECIPE_SERIALIZERS.register("sized_recipe", () -> new RecipeSerializer<>(SizedItemRecipe.CODEC, SizedItemRecipe.STREAM_CODEC));
    public static final Supplier<RecipeType<SizedItemRecipe>> SIZED_RECIPE_TYPE = RECIPE_TYPES.register("sized_recipe_type", RecipeType::simple);
}