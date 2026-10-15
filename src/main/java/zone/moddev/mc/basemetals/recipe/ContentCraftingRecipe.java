package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.world.inventory.CraftingContainer;
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
        throw new IllegalArgumentException("Not an ordinary crafting recipe: " + recipe.getId());
    }

    private static final class Shaped extends ShapedRecipe {
        private final ShapedRecipe plain;

        private Shaped(ShapedRecipe plain) {
            super(plain.getId(), plain.getGroup(), plain.category(), plain.getWidth(), plain.getHeight(),
                    plain.getIngredients(), plain.getResultItem(net.minecraft.core.RegistryAccess.EMPTY), plain.showNotification());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingContainer inventory, Level world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingContainer inventory, net.minecraft.core.RegistryAccess registries) {
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
            super(plain.getId(), plain.getGroup(), plain.category(), plain.getResultItem(net.minecraft.core.RegistryAccess.EMPTY), plain.getIngredients());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingContainer inventory, Level world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingContainer inventory, net.minecraft.core.RegistryAccess registries) {
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

    private static final class Serializer
            implements RecipeSerializer<Recipe<?>> {
        private Serializer() {}

        @Override
        public Recipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject nested = GsonHelper.getAsJsonObject(json, "recipe");
            String type = GsonHelper.getAsString(nested, "type");
            if (!"minecraft:crafting_shaped".equals(type) && !"minecraft:crafting_shapeless".equals(type)) {
                throw new JsonSyntaxException("Content-mode crafting requires an ordinary shaped or shapeless recipe");
            }

            return wrap(RecipeManager.fromJson(id, nested));
        }

        @Override
        public Recipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            ResourceLocation serializerId = buffer.readResourceLocation();
            ResourceLocation recipeId = buffer.readResourceLocation();
            RecipeSerializer<?> serializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerId);
            if (serializer == null || serializer == SERIALIZER) {
                throw new IllegalArgumentException("Invalid nested recipe serializer " + serializerId);
            }
            Recipe<?> nested = serializer.fromNetwork(recipeId, buffer);
            if (!nested.getId().equals(id) || nested.getSerializer() == SERIALIZER) {
                throw new IllegalArgumentException("Invalid nested content-mode recipe " + id);
            }
            return wrap(nested);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, Recipe value) {
            Recipe plain = value instanceof Shaped ? ((Shaped) value).plain : ((Shapeless) value).plain;
            buffer.writeResourceLocation(ForgeRegistries.RECIPE_SERIALIZERS.getKey(plain.getSerializer()));
            buffer.writeResourceLocation(plain.getId());
            writePlain(buffer, plain);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private static void writePlain(FriendlyByteBuf buffer, Recipe plain) {
            ((RecipeSerializer) plain.getSerializer()).toNetwork(buffer, plain);
        }
    }
}
