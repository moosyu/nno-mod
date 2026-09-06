package io.github.moosyu.data.datagen;

import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.data.recipes.SizedItemRecipeBuilder;
import io.github.moosyu.data.recipes.SizedShapedRecipePattern;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import org.jspecify.annotations.NonNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

import static io.github.moosyu.Unshattered.MODID;

public class UnshatteredRecipeProvider extends RecipeProvider {
    protected UnshatteredRecipeProvider(HolderLookup.Provider provider, RecipeOutput output) {
        super(provider, output);
    }

    @Override
    protected void buildRecipes() {
        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.ZOMBIE_HEART.get()))
                .pattern("AAA", "A A", "AAA")
                .define('A', SizedIngredient.of(UnshatteredItems.ENCHANTED_ROTTEN_FLESH, 32))
                .save(output, createRecipeResourceKey(UnshatteredItems.ZOMBIE_HEART.get()));

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.ZOMBIE_SWORD.get()))
                .pattern("A", "A", "C")
                .define('A', singleSizedIngredient(UnshatteredItems.ZOMBIE_HEART))
                .define('C', singleSizedIngredient(Items.STICK))
                .save(output, createRecipeResourceKey(UnshatteredItems.ZOMBIE_SWORD.get()));

        createEnchantedItemRecipe(output, Items.GOLD_INGOT, UnshatteredItems.ENCHANTED_GOLD_INGOT.get());
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_GOLD_INGOT, UnshatteredItems.ENCHANTED_GOLD_BLOCK.get());
        createEnchantedItemRecipe(output, Items.DIAMOND, UnshatteredItems.ENCHANTED_DIAMOND.get());
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_DIAMOND, UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get());

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()))
                .pattern("A", "B", "C")
                .define('A', singleSizedIngredient(UnshatteredItems.ENCHANTED_GOLD_BLOCK))
                .define('B', singleSizedIngredient(UnshatteredItems.GOLDEN_POWDER))
                .define('C', singleSizedIngredient(Items.STICK))
                .save(output, createRecipeResourceKey(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()));

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()))
                .pattern("A", "A", "C")
                .define('A', SizedIngredient.of(UnshatteredItems.HEALING_TISSUE, 24))
                .define('C', singleSizedIngredient(Items.STICK))
                .save(output, createRecipeResourceKey(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()));
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(output, lookupProvider);
        }

        @Override
        protected @NonNull RecipeProvider createRecipeProvider(HolderLookup.@NonNull Provider provider, @NonNull RecipeOutput output) {
            return new UnshatteredRecipeProvider(provider, output);
        }

        @Override
        public @NonNull String getName() {
            return "Unshattered Recipe Provider";
        }
    }

    private void createRecipe(RecipeOutput output, Item result, SizedIngredient... ingredients) {
        if (ingredients.length == 0 || ingredients.length > 9) {
            throw new IllegalArgumentException("count must be between 1 and 9");
        }

        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(new ItemStackTemplate(result));
        int width = Math.min(ingredients.length, 3);
        int height = (int) Math.ceil(ingredients.length / (double) width);
        char[] symbols = "ABCDEFGHI".toCharArray();
        StringBuilder[] rows = new StringBuilder[height];
        HashMap<SizedIngredient, Character> recipeEntries = new HashMap<>();

        for (int row = 0; row < height; row++) {
            rows[row] = new StringBuilder();
        }

        for (int i = 0; i < ingredients.length; i++) {
            char symbol = symbols[i];
            if (recipeEntries.containsKey(ingredients[i])) {
                rows[i / width].append(recipeEntries.get(ingredients[i]));
            } else {
                builder.define(symbol, ingredients[i]);
                rows[i / width].append(symbol);
                recipeEntries.put(ingredients[i], symbol);
            }
        }

        for (StringBuilder row : rows) {
            while (row.length() < width) {
                row.append(SizedShapedRecipePattern.EMPTY_SLOT);
            }
        }

        builder.pattern(Arrays.stream(rows).map(StringBuilder::toString).toArray(String[]::new));
        builder.save(output, createRecipeResourceKey(result));
    }

    private ResourceKey<Recipe<?>> createRecipeResourceKey(Item result) {
        return ResourceKey.create(Registries.RECIPE, UnshatteredUtils.getUnshatteredIdentifier(result.getDescriptionId().replace("item." + MODID + ".", "") + "_recipe"));
    }

    /**
     * creates a basic enchanted item recipe (5 sets of 32)
     * @param output output
     * @param ingredient the ingredient that makes up the enchanted item
     * @param result the item that's crafted
     */
    private void createEnchantedItemRecipe(RecipeOutput output, ItemLike ingredient, Item result) {
        SizedIngredient[] ingredients = new SizedIngredient[5];
        Arrays.fill(ingredients, SizedIngredient.of(ingredient, 32));
        createRecipe(output, result, ingredients);
    }

    private SizedIngredient singleSizedIngredient(ItemLike item) {
        return SizedIngredient.of(item, 1);
    }
}