package io.github.moosyu.recipes;

import com.google.common.annotations.VisibleForTesting;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.chars.CharArraySet;
import it.unimi.dsi.fastutil.chars.CharSet;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.common.crafting.SizedIngredient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public final class SizedShapedRecipePattern {
    public static final char EMPTY_SLOT = ' ';
    public static final MapCodec<SizedShapedRecipePattern> MAP_CODEC;
    public static final StreamCodec<RegistryFriendlyByteBuf, SizedShapedRecipePattern> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, e -> e.width,
                    ByteBufCodecs.VAR_INT, pattern -> pattern.height,
                    ByteBufCodecs.optional(SizedIngredient.STREAM_CODEC).apply(ByteBufCodecs.list()), pattern -> pattern.ingredients,
                    SizedShapedRecipePattern::createFromNetwork
            );

    private final int width;
    private final int height;
    private final List<Optional<SizedIngredient>> ingredients;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private final Optional<Data> data;
    private final int ingredientCount;

    public SizedShapedRecipePattern(int width, int height, List<Optional<SizedIngredient>> ingredients, @SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Data> data) {
        this.width = width;
        this.height = height;
        this.ingredients = ingredients;
        this.data = data;
        this.ingredientCount = (int) ingredients.stream().flatMap(Optional::stream).count();
    }

    private static SizedShapedRecipePattern createFromNetwork(Integer width, Integer height, List<Optional<SizedIngredient>> ingredients) {
        return new SizedShapedRecipePattern(width, height, ingredients, Optional.empty());
    }

    private static DataResult<SizedShapedRecipePattern> unpack(Data data) {
        String[] shrunkPattern = shrink(data.pattern);
        int width = shrunkPattern[0].length();
        int height = shrunkPattern.length;
        List<Optional<SizedIngredient>> ingredients = new ArrayList<>(width * height);
        CharSet unusedSymbols = new CharArraySet(data.key.keySet());

        for (String line : shrunkPattern) {
            for (int x = 0; x < line.length(); ++x) {
                char symbol = line.charAt(x);
                Optional<SizedIngredient> ingredient;
                if (symbol == ' ') {
                    ingredient = Optional.empty();
                } else {
                    SizedIngredient forSymbol = data.key.get(symbol);
                    if (forSymbol == null) {
                        return DataResult.error(() -> "Pattern references symbol '" + symbol + "' but it's not defined in the key");
                    }
                    ingredient = Optional.of(forSymbol);
                }
                unusedSymbols.remove(symbol);
                ingredients.add(ingredient);
            }
        }

        return !unusedSymbols.isEmpty()
                ? DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + unusedSymbols)
                : DataResult.success(new SizedShapedRecipePattern(width, height, ingredients, Optional.of(data)));
    }

    // shrink/firstNonEmpty/lastNonEmpty copied VERBATIM from ShapedRecipePattern — no changes needed,
    // they operate purely on the pattern strings, nothing Ingredient-specific.
    @VisibleForTesting
    static String[] shrink(List<String> pattern) {
        int left = Integer.MAX_VALUE, right = 0, top = 0, bottom = 0;
        for (int i = 0; i < pattern.size(); ++i) {
            String line = pattern.get(i);
            left = Math.min(left, firstNonEmpty(line));
            int lastNonSpace = lastNonEmpty(line);
            right = Math.max(right, lastNonSpace);
            if (lastNonSpace < 0) {
                if (top == i) ++top;
                ++bottom;
            } else {
                bottom = 0;
            }
        }
        if (pattern.size() == bottom) return new String[0];
        String[] result = new String[pattern.size() - bottom - top];
        for (int line = 0; line < result.length; ++line) {
            result[line] = pattern.get(line + top).substring(left, right + 1);
        }
        return result;
    }

    private static int firstNonEmpty(String line) {
        int i = 0;
        while (i < line.length() && line.charAt(i) == ' ') i++;
        return i;
    }

    private static int lastNonEmpty(String line) {
        int i = line.length() - 1;
        while (i >= 0 && line.charAt(i) == ' ') i--;
        return i;
    }

    public boolean matches(CraftingInput input) {
        if (input.ingredientCount() != this.ingredientCount) return false;
        return input.width() == this.width && input.height() == this.height && this.matches(input, false);
        // xFlip variant dropped for simplicity — add back the symmetry branch later if you want
        // mirrored recipes to match; skipping it only means players must place your pattern
        // exactly as written, not mirrored, which is a safe default to start with.
    }

    private boolean matches(CraftingInput input, boolean xFlip) {
        for (int y = 0; y < this.height; ++y) {
            for (int x = 0; x < this.width; ++x) {
                Optional<SizedIngredient> expected = xFlip
                        ? this.ingredients.get(this.width - x - 1 + y * this.width)
                        : this.ingredients.get(x + y * this.width);
                ItemStack actual = input.getItem(x, y);
                if (expected.isEmpty()) {
                    if (!actual.isEmpty()) return false;
                } else if (!expected.get().test(actual)) { // adjust to your SizedIngredient's actual test method name
                    return false;
                }
            }
        }
        return true;
    }

    public List<Optional<SizedIngredient>> ingredients() {
        return this.ingredients;
    }

    public static SizedShapedRecipePattern of(Map<Character, SizedIngredient> key, String... pattern) {
        return of(key, List.of(pattern));
    }

    public static SizedShapedRecipePattern of(Map<Character, SizedIngredient> key, List<String> pattern) {
        Data data = new Data(key, pattern);
        return unpack(data).getOrThrow();
    }

    static {
        MAP_CODEC = SizedShapedRecipePattern.Data.MAP_CODEC.flatXmap(
                SizedShapedRecipePattern::unpack,
                pattern -> pattern.data.map(DataResult::success)
                        .orElseGet(() -> DataResult.error(() -> "Cannot encode unpacked recipe")));
    }

    public record Data(Map<Character, SizedIngredient> key, List<String> pattern) {
        private static final Codec<List<String>> PATTERN_CODEC;
        private static final Codec<Character> SYMBOL_CODEC;
        public static final MapCodec<Data> MAP_CODEC;

        static {
            PATTERN_CODEC = Codec.STRING.listOf().comapFlatMap(strings -> {
                if (strings.size() > 3) { // your own max, or expose your own maxWidth/maxHeight statics
                    return DataResult.error(() -> "Invalid pattern: too many rows, 3 is maximum");
                } else if (strings.isEmpty()) {
                    return DataResult.error(() -> "Invalid pattern: empty pattern not allowed");
                } else {
                    int firstLength = strings.getFirst().length();
                    for (String line : strings) {
                        if (line.length() > 3) {
                            return DataResult.error(() -> "Invalid pattern: too many columns, 3 is maximum");
                        }
                        if (firstLength != line.length()) {
                            return DataResult.error(() -> "Invalid pattern: each row must be the same width");
                        }
                    }
                    return DataResult.success(strings);
                }
            }, Function.identity());

            SYMBOL_CODEC = Codec.STRING.comapFlatMap(symbol -> {
                if (symbol.length() != 1) {
                    return DataResult.error(() -> "Invalid key entry: '" + symbol + "' is an invalid symbol (must be 1 character only).");
                }
                return " ".equals(symbol)
                        ? DataResult.error(() -> "Invalid key entry: ' ' is a reserved symbol.")
                        : DataResult.success(symbol.charAt(0));
            }, String::valueOf);

            MAP_CODEC = RecordCodecBuilder.mapCodec(i -> i.group(
                    ExtraCodecs.strictUnboundedMap(SYMBOL_CODEC, SizedIngredient.NESTED_CODEC).fieldOf("key").forGetter(Data::key),
                    PATTERN_CODEC.fieldOf("pattern").forGetter(Data::pattern)
            ).apply(i, Data::new));
        }
    }
}
