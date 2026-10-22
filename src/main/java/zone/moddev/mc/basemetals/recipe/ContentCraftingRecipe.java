package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Decoder;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.Level;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Keeps recipe IDs stable between modes, including IDs stored in an old recipe book. */
public final class ContentCraftingRecipe {
    public static final RecipeSerializer<Recipe<?>> SERIALIZER = new Serializer();
    private ContentCraftingRecipe() {}

    private static boolean allowed(Recipe<?> recipe) {
        return ContentPolicy.active().allows(BuiltInRegistries.ITEM.getKey(recipe.getResultItem(net.minecraft.core.RegistryAccess.EMPTY).getItem()).toString());
    }

    private static Recipe<?> wrap(Recipe<?> recipe) {
        // Recipe-book placement needs the shaped recipe's width and height, not just its ingredients.
        if (recipe instanceof ShapedRecipe) return new Shaped((ShapedRecipe) recipe);
        if (recipe instanceof ShapelessRecipe) return new Shapeless((ShapelessRecipe) recipe);
        throw new IllegalArgumentException("Not an ordinary crafting recipe: " + recipe.getClass().getName());
    }

    private static final class Shaped extends ShapedRecipe {
        private final ShapedRecipe plain;

        private Shaped(ShapedRecipe plain) {
            super(plain.getGroup(), plain.category(), plain.pattern,
                    plain.getResultItem(net.minecraft.core.RegistryAccess.EMPTY), plain.showNotification());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingInput inventory, Level world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider registries) {
            return allowed(this) ? super.assemble(inventory, registries) : ItemStack.EMPTY;
        }

        @Override
        public boolean isSpecial() {
            return !allowed(this);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return SERIALIZER;
        }
    }

    private static final class Shapeless extends ShapelessRecipe {
        private final ShapelessRecipe plain;

        private Shapeless(ShapelessRecipe plain) {
            super(plain.getGroup(), plain.category(), plain.getResultItem(net.minecraft.core.RegistryAccess.EMPTY), plain.getIngredients());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingInput inventory, Level world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingInput inventory, HolderLookup.Provider registries) {
            return allowed(this) ? super.assemble(inventory, registries) : ItemStack.EMPTY;
        }

        @Override
        public boolean isSpecial() {
            return !allowed(this);
        }

        @Override
        public RecipeSerializer<?> getSerializer() {
            return SERIALIZER;
        }
    }

    private static Recipe<?> unwrap(Recipe<?> recipe) {
        if (recipe instanceof Shaped shaped) return shaped.plain;
        if (recipe instanceof Shapeless shapeless) return shapeless.plain;
        throw new IllegalArgumentException("Not a content-mode crafting recipe");
    }

    private static final class Serializer implements RecipeSerializer<Recipe<?>> {
        // Existing generated crafting JSON uses result.item; 1.21 uses result.id.
        // Normalize only that field, retaining registry-aware ops and components.
        private static final Codec<Recipe<?>> NESTED_CODEC = Codec.of(Recipe.CODEC, new Decoder<Recipe<?>>() {
            @Override
            public <T> DataResult<Pair<Recipe<?>, T>> decode(DynamicOps<T> ops, T input) {
                Dynamic<T> recipe = new Dynamic<>(ops, input);
                var result = recipe.get("result").result();
                if (result.isPresent()) {
                    var oldItem = result.get().get("item").result();
                    if (oldItem.isPresent() && result.get().get("id").result().isEmpty()) {
                        recipe = recipe.set("result", result.get().remove("item").set("id", oldItem.get()));
                    }
                }
                return Recipe.CODEC.decode(ops, recipe.getValue());
            }
        });
        private static final MapCodec<Recipe<?>> CODEC = NESTED_CODEC.<Recipe<?>>flatXmap(
                recipe -> recipe instanceof ShapedRecipe || recipe instanceof ShapelessRecipe
                        ? DataResult.success(wrap(recipe))
                        : DataResult.error(() -> "Content-mode crafting requires a shaped or shapeless recipe"),
                recipe -> DataResult.success(unwrap(recipe))).fieldOf("recipe");
        private static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC =
                Recipe.STREAM_CODEC.map(ContentCraftingRecipe::wrap, ContentCraftingRecipe::unwrap);

        @Override public MapCodec<Recipe<?>> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> streamCodec() { return STREAM_CODEC; }
    }
}
