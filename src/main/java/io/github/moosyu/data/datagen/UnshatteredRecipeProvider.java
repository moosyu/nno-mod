package io.github.moosyu.data.datagen;

import io.github.moosyu.items.UnshatteredItems;
import io.github.moosyu.data.recipes.SizedItemRecipeBuilder;
import io.github.moosyu.data.recipes.SizedShapedRecipePattern;
import io.github.moosyu.util.UnshatteredUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
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
import org.lwjgl.system.ffm.mapping.Mapping;

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
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.ZOMBIE_SWORD.get()))
                .pattern("A", "A", "C")
                .define('A', singleSizedIngredient(UnshatteredItems.ZOMBIE_HEART))
                .define('C', singleSizedIngredient(Items.STICK))
                .save(output);

        createEnchantedItemWithBlocksRecipe(output, Items.GOLD_INGOT, Items.GOLD_BLOCK, UnshatteredItems.ENCHANTED_GOLD_INGOT.get());
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_GOLD_INGOT, UnshatteredItems.ENCHANTED_GOLD_BLOCK.get());
        createEnchantedItemWithBlocksRecipe(output, Items.DIAMOND, Items.DIAMOND_BLOCK, UnshatteredItems.ENCHANTED_DIAMOND.get());
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_DIAMOND, UnshatteredItems.ENCHANTED_DIAMOND_BLOCK.get());
        createEnchantedItemWithBlocksRecipe(output, Items.EMERALD, Items.EMERALD_BLOCK, UnshatteredItems.ENCHANTED_EMERALD.get());
        createEnchantedItemRecipe(output, UnshatteredItems.ENCHANTED_EMERALD, UnshatteredItems.ENCHANTED_EMERALD_BLOCK.get());

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.ORNATE_ZOMBIE_SWORD.get()))
                .pattern("A", "B", "C")
                .define('A', singleSizedIngredient(UnshatteredItems.ENCHANTED_GOLD_BLOCK))
                .define('B', singleSizedIngredient(UnshatteredItems.GOLDEN_POWDER))
                .define('C', singleSizedIngredient(Items.STICK))
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.FLORID_ZOMBIE_SWORD.get()))
                .pattern("A", "A", "C")
                .define('A', SizedIngredient.of(UnshatteredItems.HEALING_TISSUE, 24))
                .define('C', singleSizedIngredient(Items.STICK))
                .save(output);

        new SizedItemRecipeBuilder(new ItemStackTemplate(UnshatteredItems.COINS_TALISMAN.get()))
                .pattern(" A ", "ABA", " A ")
                .define('A', SizedIngredient.of(Items.EMERALD, 5))
                .define('B', SizedIngredient.of(Items.GOLD_INGOT, 5))
                .save(output);
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

    /**
     * creates a simple recipe with a shape across and then down (sorry im bad at describing things)
     * @param output output
     * @param result the item  to be crafted
     * @param ingredients sized ingredients required to create the result
     */
    private void createRecipe(RecipeOutput output, Item result, SizedIngredient... ingredients) {
        createRecipe(output, result, 1, createRecipeResourceKey(result), ingredients);
    }

    /**
     * creates a simple recipe with a shape across and then down (sorry im bad at describing things)
     * @param output output
     * @param result the item  to be crafted
     * @param amount amount of item crafted
     * @param ingredients sized ingredients required to create the result
     */
    private void createRecipe(RecipeOutput output, Item result, int amount, SizedIngredient... ingredients) {
        createRecipe(output, result, amount, createRecipeResourceKey(result), ingredients);
    }

    /**
     * creates a simple recipe with a shape across and then down (sorry im bad at describing things)
     * @param output output
     * @param result the item crafted
     * @param amount amount of item crafted
     * @param key recipe identifier key
     * @param ingredients sized ingredients required to create the item
     */
    private void createRecipe(RecipeOutput output, Item result, int amount, ResourceKey<Recipe<?>> key, SizedIngredient... ingredients) {
        if (ingredients.length == 0 || ingredients.length > 9) {
            throw new IllegalArgumentException("count must be between 1 and 9");
        }

        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(new ItemStackTemplate(result, amount));
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
        builder.save(output, key);
    }

    public static ResourceKey<Recipe<?>> createRecipeResourceKey(Item result) {
        return ResourceKey.create(Registries.RECIPE, UnshatteredUtils.getUnshatteredIdentifier(BuiltInRegistries.ITEM.getKey(result).getPath() + "_recipe"));
    }

    public static ResourceKey<Recipe<?>> createRecipeResourceKey(Item result, String suffix) {
        return ResourceKey.create(Registries.RECIPE, UnshatteredUtils.getUnshatteredIdentifier(BuiltInRegistries.ITEM.getKey(result).getPath() + "_recipe" + suffix));
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

        SizedItemRecipeBuilder builder = new SizedItemRecipeBuilder(new ItemStackTemplate(result));
        builder.pattern(" A ", "AAA", " A ")
                .define('A', SizedIngredient.of(ingredient, 32))
                .save(output, BuiltInRegistries.ITEM.getKey(ingredient.asItem()).getPath() + "_2");
    }

    /**
     * creates a basic enchanted item recipe including block variants which create 9
     * @param output output
     * @param itemIngredient the ingredient that makes up the enchanted item's single outputs
     * @param blockIngredient the block used to create 9 of the enchanted item
     * @param result the item that's crafted
     */
    private void createEnchantedItemWithBlocksRecipe(RecipeOutput output, ItemLike itemIngredient, ItemLike blockIngredient, Item result) {
        SizedIngredient[] ingredients = new SizedIngredient[5];
        Arrays.fill(ingredients, SizedIngredient.of(itemIngredient, 32));
        createRecipe(output, result, ingredients);

        SizedItemRecipeBuilder builderItem = new SizedItemRecipeBuilder(new ItemStackTemplate(result));
        builderItem.pattern(" A ", "AAA", " A ")
                .define('A', SizedIngredient.of(itemIngredient, 32))
                .save(output, createRecipeResourceKey(result, "_recipe_2"));

        Arrays.fill(ingredients, SizedIngredient.of(blockIngredient, 32));
        createRecipe(output,
                result,
                9,
                createRecipeResourceKey(result, "_recipe_3"),
                ingredients
        );

        SizedItemRecipeBuilder builderBlock = new SizedItemRecipeBuilder(new ItemStackTemplate(result, 9));
        builderBlock.pattern(" A ", "AAA", " A ")
                .define('A', SizedIngredient.of(blockIngredient, 32))
                .save(output, createRecipeResourceKey(result, "_recipe_4"));
    }

    private SizedIngredient singleSizedIngredient(ItemLike item) {
        return SizedIngredient.of(item, 1);
    }
}