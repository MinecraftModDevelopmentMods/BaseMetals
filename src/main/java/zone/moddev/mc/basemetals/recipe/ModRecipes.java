package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import zone.moddev.mc.basemetals.BaseMetals;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Base Metals recipe serializers. Crushing uses MMDLib's {@code mmdlib:crushing}. */
public final class ModRecipes {
    public static final DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(
            Registries.RECIPE_SERIALIZER, BaseMetals.MOD_ID);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<LegacySmeltingRecipe>> LEGACY_SMELTING_SERIALIZER =
            SERIALIZERS.register("legacy_smelting", LegacySmeltingRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<PlateRepairRecipe>> PLATE_REPAIR_SERIALIZER =
            SERIALIZERS.register("plate_repair", PlateRepairRecipe.Serializer::new);
    public static final DeferredHolder<RecipeSerializer<?>, RecipeSerializer<Recipe<?>>> CONTENT_CRAFTING_SERIALIZER =
            SERIALIZERS.register("content_crafting", () -> ContentCraftingRecipe.SERIALIZER);

    // Accept the existing generated results using "item", as well as the
    // 1.21 stack format using "id" and optional data components.
    private static final Codec<ItemStack> LEGACY_RESULT_CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemStack::getItem),
                    Codec.intRange(1, 99).optionalFieldOf("count", 1).forGetter(ItemStack::getCount)
            ).apply(instance, ItemStack::new));

    static final Codec<ItemStack> RESULT_CODEC = Codec.withAlternative(ItemStack.CODEC, LEGACY_RESULT_CODEC);

    private ModRecipes() {}
}
