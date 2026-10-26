package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;

/**
 * Keeps the existing recycling recipe IDs and counted furnace outputs.
 */
public final class LegacySmeltingRecipe extends SmeltingRecipe {
    public LegacySmeltingRecipe(String group, Ingredient ingredient,
            ItemStack result, float experience, int cookingTime) {
        super(group, net.minecraft.world.item.crafting.CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean matches(net.minecraft.world.item.crafting.SingleRecipeInput inventory, net.minecraft.world.level.Level world) {
        return zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(result().getItem()).toString())
                && super.matches(inventory, world);
    }

    @Override
    public ItemStack assemble(net.minecraft.world.item.crafting.SingleRecipeInput inventory, net.minecraft.core.HolderLookup.Provider registries) {
        return zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(result().getItem()).toString())
                ? super.assemble(inventory, registries) : ItemStack.EMPTY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<SmeltingRecipe> getSerializer() {
        return (RecipeSerializer<SmeltingRecipe>) (RecipeSerializer<?>)
                CrushingRecipe.LEGACY_SMELTING_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<LegacySmeltingRecipe> {
        private static final MapCodec<LegacySmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(LegacySmeltingRecipe::group),
                        Ingredient.CODEC.fieldOf("ingredient").forGetter(recipe -> recipe.input()),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.result()),
                        Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(LegacySmeltingRecipe::experience),
                        Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(LegacySmeltingRecipe::cookingTime))
                        .apply(instance, LegacySmeltingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, LegacySmeltingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, LegacySmeltingRecipe::group,
                        Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.input(),
                        ItemStack.STREAM_CODEC, recipe -> recipe.result(),
                        ByteBufCodecs.FLOAT, LegacySmeltingRecipe::experience,
                        ByteBufCodecs.VAR_INT, LegacySmeltingRecipe::cookingTime,
                        LegacySmeltingRecipe::new);

        @Override public MapCodec<LegacySmeltingRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, LegacySmeltingRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
