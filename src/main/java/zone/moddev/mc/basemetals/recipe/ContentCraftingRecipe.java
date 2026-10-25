package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import com.google.gson.JsonSyntaxException;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Keeps recipe IDs stable between modes, including IDs stored in an old recipe book. */
public final class ContentCraftingRecipe {
    public static final RecipeSerializer<Recipe<?>> SERIALIZER = new Serializer();
    private ContentCraftingRecipe() {}

    private static boolean allowed(Recipe recipe) {
        return ContentPolicy.active().allows(ForgeRegistries.ITEMS.getKey(recipe.getResultItem(net.minecraft.core.RegistryAccess.EMPTY).getItem()).toString());
    }

    private static Recipe wrap(Recipe recipe) {
        // Recipe-book placement needs the shaped recipe's width and height, not just its ingredients.
        if (recipe instanceof ShapedRecipe) return new Shaped((ShapedRecipe) recipe);
        if (recipe instanceof ShapelessRecipe) return new Shapeless((ShapelessRecipe) recipe);
        throw new IllegalArgumentException("Not an ordinary crafting recipe: " + recipe.getSerializer());
    }

    private static final class Shaped extends ShapedRecipe {
        private final ShapedRecipe plain;

        private Shaped(ShapedRecipe plain) {
            super(plain.getGroup(), plain.category(),
                    new net.minecraft.world.item.crafting.ShapedRecipePattern(plain.getWidth(), plain.getHeight(),
                            plain.getIngredients(), java.util.Optional.empty()),
                    plain.getResultItem(net.minecraft.core.RegistryAccess.EMPTY), plain.showNotification());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingInput inventory, Level world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingInput inventory, net.minecraft.core.HolderLookup.Provider registries) {
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
        public ItemStack assemble(CraftingInput inventory, net.minecraft.core.HolderLookup.Provider registries) {
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

    private static Recipe<?> plain(Recipe<?> recipe) {
        return recipe instanceof Shaped shaped ? shaped.plain : ((Shapeless) recipe).plain;
    }

    private static final class Serializer implements RecipeSerializer<Recipe<?>> {
        private static final MapCodec<Recipe<?>> CODEC = Recipe.CODEC.fieldOf("recipe")
                .xmap(ContentCraftingRecipe::wrap, ContentCraftingRecipe::plain);
        private static final StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> STREAM_CODEC =
                Recipe.STREAM_CODEC.map(ContentCraftingRecipe::wrap, ContentCraftingRecipe::plain);

        @Override public MapCodec<Recipe<?>> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, Recipe<?>> streamCodec() { return STREAM_CODEC; }
    }
}
