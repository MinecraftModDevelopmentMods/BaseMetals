package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
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

    private final Ingredient ingredient;
    private final ItemStack result;

    public CrushingRecipe(Ingredient ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result.copy();
    }

    @Override public boolean matches(Container container, Level level) { return ingredient.test(container.getItem(0)); }
    @Override public ItemStack assemble(Container container, net.minecraft.core.HolderLookup.Provider registries) { return result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    @Override public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) { return result.copy(); }
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

    public static final class Serializer implements RecipeSerializer<CrushingRecipe> {
        private static final MapCodec<CrushingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.ingredient),
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
