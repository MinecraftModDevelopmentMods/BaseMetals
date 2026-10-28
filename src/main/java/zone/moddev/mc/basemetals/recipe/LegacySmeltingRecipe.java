package zone.moddev.mc.basemetals.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.AbstractCookingRecipe.CookingBookInfo;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe.CommonInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Keeps the existing recycling recipe IDs and counted furnace outputs. */
public final class LegacySmeltingRecipe extends SmeltingRecipe {
    public LegacySmeltingRecipe(CommonInfo commonInfo, CookingBookInfo bookInfo,
            Ingredient ingredient, ItemStackTemplate result, float experience, int cookingTime) {
        super(commonInfo, bookInfo, ingredient, result, experience, cookingTime);
    }

    private boolean allowed() {
        return ContentPolicy.active().allows(ForgeRegistries.ITEMS.getKey(result().item().value()).toString());
    }

    @Override
    public boolean matches(SingleRecipeInput inventory, Level world) {
        return allowed() && super.matches(inventory, world);
    }

    @Override
    public ItemStack assemble(SingleRecipeInput inventory) {
        return allowed() ? super.assemble(inventory) : ItemStack.EMPTY;
    }

    @Override
    @SuppressWarnings("unchecked")
    public RecipeSerializer<SmeltingRecipe> getSerializer() {
        return (RecipeSerializer<SmeltingRecipe>) (RecipeSerializer<?>)
                CrushingRecipe.LEGACY_SMELTING_SERIALIZER.get();
    }

    public static final class Serializer {
        private Serializer() {}

        public static RecipeSerializer<LegacySmeltingRecipe> create() {
            return new RecipeSerializer<>(cookingMapCodec(LegacySmeltingRecipe::new, 200),
                    cookingStreamCodec(LegacySmeltingRecipe::new));
        }
    }
}
