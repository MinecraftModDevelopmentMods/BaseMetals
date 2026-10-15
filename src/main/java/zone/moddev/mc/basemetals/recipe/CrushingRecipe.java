package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;
import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrushingRecipe implements Recipe<Container> {
    public static final DeferredRegister<RecipeType<?>> TYPES = DeferredRegister.create(
            net.minecraft.core.registries.Registries.RECIPE_TYPE, BaseMetals.MOD_ID);
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.RECIPE_SERIALIZERS, BaseMetals.MOD_ID);
    public static final RegistryObject<RecipeType<CrushingRecipe>> TYPE = TYPES
            .<RecipeType<CrushingRecipe>>register("crushing", CrushingRecipeType::new);
    public static final RegistryObject<RecipeSerializer<CrushingRecipe>> SERIALIZER = SERIALIZERS.register(
            "crushing", Serializer::new);
    public static final RegistryObject<RecipeSerializer<LegacySmeltingRecipe>> LEGACY_SMELTING_SERIALIZER =
            SERIALIZERS.register("legacy_smelting", LegacySmeltingRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<PlateRepairRecipe>> PLATE_REPAIR_SERIALIZER =
            SERIALIZERS.register("plate_repair", PlateRepairRecipe.Serializer::new);
    public static final RegistryObject<RecipeSerializer<Recipe<?>>> CONTENT_CRAFTING_SERIALIZER =
            SERIALIZERS.register("content_crafting", () -> ContentCraftingRecipe.SERIALIZER);

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;

    public CrushingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result.copy();
    }

    @Override public boolean matches(Container container, Level level) { return ingredient.test(container.getItem(0)); }
    @Override public ItemStack assemble(Container container, net.minecraft.core.RegistryAccess registries) { return result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    @Override public ItemStack getResultItem(net.minecraft.core.RegistryAccess registries) { return result.copy(); }
    @Override public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return SERIALIZER.get(); }
    @Override public RecipeType<?> getType() { return TYPE.get(); }
    @Override public NonNullList<Ingredient> getIngredients() { return NonNullList.of(Ingredient.EMPTY, ingredient); }
    @Override public boolean isSpecial() { return true; }

    private static final class CrushingRecipeType implements RecipeType<CrushingRecipe> {
        @Override
        public String toString() {
            return BaseMetals.MOD_ID + ":crushing";
        }
    }

    public static final class Serializer
            implements RecipeSerializer<CrushingRecipe> {
        @Override
        public CrushingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            JsonObject resultJson = GsonHelper.getAsJsonObject(json, "result");
            ItemStack result = net.minecraftforge.common.crafting.CraftingHelper.getItemStack(resultJson, true);
            return new CrushingRecipe(id, ingredient, result);
        }

        @Override
        public CrushingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new CrushingRecipe(id, Ingredient.fromNetwork(buffer), buffer.readItem());
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, CrushingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
