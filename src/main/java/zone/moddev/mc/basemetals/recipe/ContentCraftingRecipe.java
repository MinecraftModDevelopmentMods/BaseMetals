package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Keeps recipe IDs stable between modes, including IDs stored in an old recipe book. */
public final class ContentCraftingRecipe {
    public static final IRecipeSerializer<IRecipe<?>> SERIALIZER = new Serializer();
    private ContentCraftingRecipe() {}

    private static boolean allowed(IRecipe recipe) {
        return ContentPolicy.active().allows(recipe.getResultItem().getItem().getRegistryName().toString());
    }

    private static IRecipe wrap(IRecipe recipe) {
        // Recipe-book placement needs the shaped recipe's width and height, not just its ingredients.
        if (recipe instanceof ShapedRecipe) return new Shaped((ShapedRecipe) recipe);
        if (recipe instanceof ShapelessRecipe) return new Shapeless((ShapelessRecipe) recipe);
        throw new IllegalArgumentException("Not an ordinary crafting recipe: " + recipe.getId());
    }

    private static final class Shaped extends ShapedRecipe {
        private final ShapedRecipe plain;

        private Shaped(ShapedRecipe plain) {
            super(plain.getId(), plain.getGroup(), plain.getWidth(), plain.getHeight(),
                    plain.getIngredients(), plain.getResultItem());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingInventory inventory, World world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingInventory inventory) {
            return allowed(this) ? super.assemble(inventory) : ItemStack.EMPTY;
        }

        @Override
        public boolean isSpecial() {
            return !allowed(this);
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return SERIALIZER;
        }
    }

    private static final class Shapeless extends ShapelessRecipe {
        private final ShapelessRecipe plain;

        private Shapeless(ShapelessRecipe plain) {
            super(plain.getId(), plain.getGroup(), plain.getResultItem(), plain.getIngredients());
            this.plain = plain;
        }

        @Override
        public boolean matches(CraftingInventory inventory, World world) {
            return allowed(this) && super.matches(inventory, world);
        }

        @Override
        public ItemStack assemble(CraftingInventory inventory) {
            return allowed(this) ? super.assemble(inventory) : ItemStack.EMPTY;
        }

        @Override
        public boolean isSpecial() {
            return !allowed(this);
        }

        @Override
        public IRecipeSerializer<?> getSerializer() {
            return SERIALIZER;
        }
    }

    private static final class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<IRecipe<?>> {
        private Serializer() { setRegistryName("basemetals", "content_crafting"); }

        @Override
        public IRecipe fromJson(ResourceLocation id, JsonObject json) {
            JsonObject nested = JSONUtils.getAsJsonObject(json, "recipe");
            String type = JSONUtils.getAsString(nested, "type");
            if (!"minecraft:crafting_shaped".equals(type) && !"minecraft:crafting_shapeless".equals(type)) {
                throw new JsonSyntaxException("Content-mode crafting requires an ordinary shaped or shapeless recipe");
            }

            return wrap(RecipeManager.fromJson(id, nested));
        }

        @Override
        public IRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            ResourceLocation serializerId = buffer.readResourceLocation();
            ResourceLocation recipeId = buffer.readResourceLocation();
            IRecipeSerializer<?> serializer = ForgeRegistries.RECIPE_SERIALIZERS.getValue(serializerId);
            if (serializer == null || serializer == SERIALIZER) {
                throw new IllegalArgumentException("Invalid nested recipe serializer " + serializerId);
            }
            IRecipe<?> nested = serializer.fromNetwork(recipeId, buffer);
            if (!nested.getId().equals(id) || nested.getSerializer() == SERIALIZER) {
                throw new IllegalArgumentException("Invalid nested content-mode recipe " + id);
            }
            return wrap(nested);
        }

        @Override
        public void toNetwork(PacketBuffer buffer, IRecipe value) {
            IRecipe plain = value instanceof Shaped ? ((Shaped) value).plain : ((Shapeless) value).plain;
            buffer.writeResourceLocation(plain.getSerializer().getRegistryName());
            buffer.writeResourceLocation(plain.getId());
            writePlain(buffer, plain);
        }

        @SuppressWarnings({"rawtypes", "unchecked"})
        private static void writePlain(PacketBuffer buffer, IRecipe plain) {
            ((IRecipeSerializer) plain.getSerializer()).toNetwork(buffer, plain);
        }
    }
}
