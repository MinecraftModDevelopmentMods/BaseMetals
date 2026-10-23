package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;

/**
 * Furnace recipe serializer which retains the counted outputs supported by the
 * 1.12 furnace registry. Vanilla's 1.18 JSON serializer only accepts a result
 * item ID and therefore cannot express the old multi-ingot recycling recipes.
 */
public final class LegacySmeltingRecipe extends SmeltingRecipe {
    public LegacySmeltingRecipe(String group, Ingredient ingredient,
            ItemStack result, float experience, int cookingTime) {
        super(group, net.minecraft.world.item.crafting.CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean matches(net.minecraft.world.Container inventory, net.minecraft.world.level.Level world) {
        return zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(getResultItem(world.registryAccess()).getItem()).toString())
                && super.matches(inventory, world);
    }

    @Override
    public ItemStack assemble(net.minecraft.world.Container inventory, net.minecraft.core.HolderLookup.Provider registries) {
        return zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(getResultItem(registries).getItem()).toString())
                ? super.assemble(inventory, registries) : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrushingRecipe.LEGACY_SMELTING_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<LegacySmeltingRecipe> {
        private static final MapCodec<LegacySmeltingRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        Codec.STRING.optionalFieldOf("group", "").forGetter(LegacySmeltingRecipe::getGroup),
                        Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(recipe -> recipe.getIngredients().getFirst()),
                        ItemStack.CODEC.fieldOf("result").forGetter(recipe -> recipe.getResultItem(null)),
                        Codec.FLOAT.optionalFieldOf("experience", 0.0F).forGetter(LegacySmeltingRecipe::getExperience),
                        Codec.INT.optionalFieldOf("cookingtime", 200).forGetter(LegacySmeltingRecipe::getCookingTime))
                        .apply(instance, LegacySmeltingRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, LegacySmeltingRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.STRING_UTF8, LegacySmeltingRecipe::getGroup,
                        Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.getIngredients().getFirst(),
                        ItemStack.STREAM_CODEC, recipe -> recipe.getResultItem(null),
                        ByteBufCodecs.FLOAT, LegacySmeltingRecipe::getExperience,
                        ByteBufCodecs.VAR_INT, LegacySmeltingRecipe::getCookingTime,
                        LegacySmeltingRecipe::new);

        @Override public MapCodec<LegacySmeltingRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, LegacySmeltingRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
