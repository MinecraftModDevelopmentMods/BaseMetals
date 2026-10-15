package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;

/**
 * Furnace recipe serializer which retains the counted outputs supported by the
 * 1.12 furnace registry. Vanilla's 1.18 JSON serializer only accepts a result
 * item ID and therefore cannot express the old multi-ingot recycling recipes.
 */
public final class LegacySmeltingRecipe extends SmeltingRecipe {
    public LegacySmeltingRecipe(ResourceLocation id, String group, Ingredient ingredient,
            ItemStack result, float experience, int cookingTime) {
        super(id, group, net.minecraft.world.item.crafting.CookingBookCategory.MISC, ingredient, result, experience, cookingTime);
    }

    @Override
    public boolean matches(net.minecraft.world.Container inventory, net.minecraft.world.level.Level world) {
        return zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(getResultItem(world.registryAccess()).getItem()).toString())
                && super.matches(inventory, world);
    }

    @Override
    public ItemStack assemble(net.minecraft.world.Container inventory, net.minecraft.core.RegistryAccess registries) {
        return zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(net.minecraftforge.registries.ForgeRegistries.ITEMS.getKey(getResultItem(registries).getItem()).toString())
                ? super.assemble(inventory, registries) : ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrushingRecipe.LEGACY_SMELTING_SERIALIZER.get();
    }

    public static final class Serializer
            implements RecipeSerializer<LegacySmeltingRecipe> {
        @Override
        public LegacySmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(
                    GsonHelper.getAsJsonObject(json, "result"), true);
            float experience = GsonHelper.getAsFloat(json, "experience", 0.0F);
            int cookingTime = GsonHelper.getAsInt(json, "cookingtime", 200);
            return new LegacySmeltingRecipe(id, group, ingredient, result, experience, cookingTime);
        }

        @Override
        public LegacySmeltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            String group = buffer.readUtf();
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float experience = buffer.readFloat();
            int cookingTime = buffer.readVarInt();
            return new LegacySmeltingRecipe(id, group, ingredient, result, experience, cookingTime);
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, LegacySmeltingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getIngredients().get(0).toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem(net.minecraft.core.RegistryAccess.EMPTY));
            buffer.writeFloat(recipe.getExperience());
            buffer.writeVarInt(recipe.getCookingTime());
        }
    }
}
