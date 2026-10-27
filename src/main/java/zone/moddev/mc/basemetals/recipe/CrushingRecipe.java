package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public final class CrushingRecipe implements Recipe<SingleRecipeInput> {
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
    public static final RegistryObject<RecipeSerializer<ContentCraftingRecipe>> CONTENT_CRAFTING_SERIALIZER =
            SERIALIZERS.register("content_crafting", () -> ContentCraftingRecipe.SERIALIZER);

    private final Ingredient ingredient;
    private final ItemStack result;

    public CrushingRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result.copy();
    }

    @Override
    public boolean matches(SingleRecipeInput input, Level level) {
        return ingredient.test(input.item());
    }

    @Override
    public ItemStack assemble(SingleRecipeInput input, net.minecraft.core.HolderLookup.Provider registries) {
        return result.copy();
    }
    public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) { return result.copy(); }
    @Override public RecipeSerializer<CrushingRecipe> getSerializer() { return SERIALIZER.get(); }
    @Override public RecipeType<CrushingRecipe> getType() { return TYPE.get(); }
    public java.util.List<Ingredient> getIngredients() { return java.util.List.of(ingredient); }
    @Override public boolean isSpecial() { return true; }
    @Override public net.minecraft.world.item.crafting.PlacementInfo placementInfo() {
        return net.minecraft.world.item.crafting.PlacementInfo.NOT_PLACEABLE;
    }
    @Override public net.minecraft.world.item.crafting.RecipeBookCategory recipeBookCategory() {
        return net.minecraft.world.item.crafting.RecipeBookCategories.CRAFTING_MISC;
    }

    private static final class CrushingRecipeType implements RecipeType<CrushingRecipe> {
        @Override
        public String toString() {
            return BaseMetals.MOD_ID + ":crushing";
        }
    }

    public static final class Serializer implements RecipeSerializer<CrushingRecipe> {
        private static final MapCodec<CrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result))
                        .apply(instance, CrushingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.ingredient,
                        ItemStack.STREAM_CODEC, recipe -> recipe.result,
                        CrushingRecipe::new);

        @Override public MapCodec<CrushingRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, CrushingRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
