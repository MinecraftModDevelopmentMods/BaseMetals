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

    @Override public ItemStack getCraftingResult(IInventory inventory) {
        return allowed() ? super.getCraftingResult(inventory) : ItemStack.EMPTY;
    }

    @Override public boolean isDynamic() { return !allowed(); }

    private boolean allowed() {
        return ContentPolicy.active().allows(getRecipeOutput().getItem().getRegistryName().toString());
    }

    public static final class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<LegacySmeltingRecipe> {
        public Serializer() { setRegistryName(BaseMetals.MOD_ID, "legacy_smelting"); }
        @Override public LegacySmeltingRecipe read(ResourceLocation id, JsonObject json) {
            String group = JSONUtils.getString(json, "group", "");
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);
            return new LegacySmeltingRecipe(id, group, ingredient, result,
                    JSONUtils.getFloat(json, "experience", 0.0F),
                    JSONUtils.getInt(json, "cookingtime", 200));
        }
        @Override public LegacySmeltingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            return new LegacySmeltingRecipe(id, buffer.readString(32767), Ingredient.read(buffer),
                    buffer.readItemStack(), buffer.readFloat(), buffer.readVarInt());
        }
        @Override public void write(PacketBuffer buffer, LegacySmeltingRecipe recipe) {
            buffer.writeString(recipe.getGroup());
            recipe.getIngredients().get(0).write(buffer);
            buffer.writeItemStack(recipe.getRecipeOutput());
            buffer.writeFloat(recipe.getExperience());
            buffer.writeVarInt(recipe.getCookTime());
        }
    }
}
