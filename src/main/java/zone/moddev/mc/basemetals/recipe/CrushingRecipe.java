package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.RecipeSerializers;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;

public final class CrushingRecipe implements IRecipe {
    public static final RecipeType<CrushingRecipe> TYPE = RecipeType.get(
            new ResourceLocation(BaseMetals.MOD_ID, "crushing"), CrushingRecipe.class);
    public static final IRecipeSerializer<CrushingRecipe> SERIALIZER = new Serializer("crushing");
    public static final IRecipeSerializer<LegacySmeltingRecipe> LEGACY_SMELTING_SERIALIZER =
            new LegacySmeltingRecipe.Serializer();
    public static final IRecipeSerializer<PlateRepairRecipe> PLATE_REPAIR_SERIALIZER =
            new PlateRepairRecipe.Serializer();
    private static boolean registered;

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;

    public CrushingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result.copy();
    }

    public static synchronized void register() {
        if (registered) return;
        RecipeSerializers.register(SERIALIZER);
        RecipeSerializers.register(LEGACY_SMELTING_SERIALIZER);
        RecipeSerializers.register(PLATE_REPAIR_SERIALIZER);
        RecipeSerializers.register(ContentCraftingRecipe.SERIALIZER);
        registered = true;
    }

    @Override public boolean matches(IInventory inventory, World world) {
        return ContentPolicy.active().allows(result.getItem().getRegistryName().toString())
                && inventory.getSizeInventory() > 0 && ingredient.test(inventory.getStackInSlot(0));
    }
    @Override public ItemStack getCraftingResult(IInventory inventory) { return result.copy(); }
    @Override public boolean canFit(int width, int height) { return width * height >= 1; }
    @Override public ItemStack getRecipeOutput() { return result.copy(); }
    @Override public ResourceLocation getId() { return id; }
    @Override public IRecipeSerializer<?> getSerializer() { return SERIALIZER; }
    @Override public RecipeType<CrushingRecipe> getType() { return TYPE; }
    @Override public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(ingredient);
        return ingredients;
    }
    @Override public boolean isDynamic() { return true; }

    public static final class Serializer implements IRecipeSerializer<CrushingRecipe> {
        private final ResourceLocation name;
        private Serializer(String name) { this.name = new ResourceLocation(BaseMetals.MOD_ID, name); }
        @Override public CrushingRecipe read(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return new CrushingRecipe(id, ingredient, result);
        }
        @Override public CrushingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            return new CrushingRecipe(id, Ingredient.fromBuffer(buffer), buffer.readItemStack());
        }
        @Override public void write(PacketBuffer buffer, CrushingRecipe recipe) {
            recipe.ingredient.writeToBuffer(buffer);
            buffer.writeItemStack(recipe.result);
        }
        @Override public ResourceLocation getName() { return name; }
    }
}
