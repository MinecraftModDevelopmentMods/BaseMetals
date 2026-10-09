package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import net.minecraft.world.Container;
import net.minecraft.world.level.Level;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

/** Smelting recipes that can produce more than one item. */
public final class LegacySmeltingRecipe extends SmeltingRecipe {
    public LegacySmeltingRecipe(ResourceLocation id, String group, Ingredient ingredient,
            ItemStack result, float experience, int cookingTime) {
        super(id, group, ingredient, result, experience, cookingTime);
    }

    @Override public RecipeSerializer<?> getSerializer() {
        return CrushingRecipe.LEGACY_SMELTING_SERIALIZER;
    }

    @Override public boolean matches(Container inventory, Level world) {
        return allowed() && super.matches(inventory, world);
    }

    @Override public ItemStack assemble(Container inventory) {
        return allowed() ? super.assemble(inventory) : ItemStack.EMPTY;
    }

    @Override public boolean isSpecial() { return !allowed(); }

    private boolean allowed() {
        return ContentPolicy.active().allows(getResultItem().getItem().getRegistryName().toString());
    }

    public static final class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<LegacySmeltingRecipe> {
        public Serializer() { setRegistryName(BaseMetals.MOD_ID, "legacy_smelting"); }
        @Override public LegacySmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = GsonHelper.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);
            return new LegacySmeltingRecipe(id, group, ingredient, result,
                    GsonHelper.getAsFloat(json, "experience", 0.0F),
                    GsonHelper.getAsInt(json, "cookingtime", 200));
        }
        @Override public LegacySmeltingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new LegacySmeltingRecipe(id, buffer.readUtf(32767), Ingredient.fromNetwork(buffer),
                    buffer.readItem(), buffer.readFloat(), buffer.readVarInt());
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, LegacySmeltingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getIngredients().get(0).toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeFloat(recipe.getExperience());
            buffer.writeVarInt(recipe.getCookingTime());
        }
    }
}
