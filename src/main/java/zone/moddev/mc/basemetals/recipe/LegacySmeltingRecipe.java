package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;
import net.minecraft.inventory.IInventory;
import net.minecraft.world.World;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.CraftingHelper;

/** Smelting recipes that can produce more than one item. */
public final class LegacySmeltingRecipe extends FurnaceRecipe {
    public LegacySmeltingRecipe(ResourceLocation id, String group, Ingredient ingredient,
            ItemStack result, float experience, int cookingTime) {
        super(id, group, ingredient, result, experience, cookingTime);
    }

    @Override public IRecipeSerializer<?> getSerializer() {
        return CrushingRecipe.LEGACY_SMELTING_SERIALIZER;
    }

    @Override public boolean matches(IInventory inventory, World world) {
        return allowed() && super.matches(inventory, world);
    }

    @Override public ItemStack assemble(IInventory inventory) {
        return allowed() ? super.assemble(inventory) : ItemStack.EMPTY;
    }

    @Override public boolean isSpecial() { return !allowed(); }

    private boolean allowed() {
        return ContentPolicy.active().allows(getResultItem().getItem().getRegistryName().toString());
    }

    public static final class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<LegacySmeltingRecipe> {
        public Serializer() { setRegistryName(BaseMetals.MOD_ID, "legacy_smelting"); }
        @Override public LegacySmeltingRecipe fromJson(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getAsString(json, "group", "");
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);
            return new LegacySmeltingRecipe(id, group, ingredient, result,
                    JSONUtils.getAsFloat(json, "experience", 0.0F),
                    JSONUtils.getAsInt(json, "cookingtime", 200));
        }
        @Override public LegacySmeltingRecipe fromNetwork(ResourceLocation id, PacketBuffer buffer) {
            return new LegacySmeltingRecipe(id, buffer.readUtf(32767), Ingredient.fromNetwork(buffer),
                    buffer.readItem(), buffer.readFloat(), buffer.readVarInt());
        }
        @Override public void toNetwork(PacketBuffer buffer, LegacySmeltingRecipe recipe) {
            buffer.writeUtf(recipe.getGroup());
            recipe.getIngredients().get(0).toNetwork(buffer);
            buffer.writeItem(recipe.getResultItem());
            buffer.writeFloat(recipe.getExperience());
            buffer.writeVarInt(recipe.getCookingTime());
        }
    }
}
