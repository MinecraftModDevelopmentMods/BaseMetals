package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Retains counted furnace outputs and content-mode restrictions. */
public final class LegacySmeltingRecipe extends SmeltingRecipe {
    private final Ingredient input;
    private final ItemStack output;

    public LegacySmeltingRecipe(String group, Ingredient ingredient, ItemStack result,
            float experience, int cookingTime) {
        super(group, CookingBookCategory.MISC, ingredient, result.copy(), experience, cookingTime);
        this.input = ingredient;
        this.output = result.copy();
    }

    private boolean allowed() {
        return ContentPolicy.active().allows(BuiltInRegistries.ITEM.getKey(output.getItem()).toString());
    }

    @Override public boolean matches(SingleRecipeInput inventory, Level world) {
        return allowed() && super.matches(inventory, world);
    }

    @Override public ItemStack assemble(SingleRecipeInput inventory, HolderLookup.Provider registries) {
        return allowed() ? output.copy() : ItemStack.EMPTY;
    }

    @Override public RecipeSerializer<?> getSerializer() {
        return CrushingRecipe.LEGACY_SMELTING_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<LegacySmeltingRecipe> {
        private static final MapCodec<LegacySmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(LegacySmeltingRecipe::getGroup),
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.input),
                        CrushingRecipe.Serializer.RESULT_CODEC.fieldOf("result").forGetter(recipe -> recipe.output),
                        Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(LegacySmeltingRecipe::getExperience),
                        Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("cookingtime", 200)
                                .forGetter(LegacySmeltingRecipe::getCookingTime)
                ).apply(instance, LegacySmeltingRecipe::new));
        private static final StreamCodec<RegistryFriendlyByteBuf, LegacySmeltingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, LegacySmeltingRecipe::getGroup,
                        Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input,
                        ItemStack.STREAM_CODEC, recipe -> recipe.output,
                        ByteBufCodecs.FLOAT, LegacySmeltingRecipe::getExperience,
                        ByteBufCodecs.VAR_INT, LegacySmeltingRecipe::getCookingTime,
                        LegacySmeltingRecipe::new);

        @Override public MapCodec<LegacySmeltingRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, LegacySmeltingRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
