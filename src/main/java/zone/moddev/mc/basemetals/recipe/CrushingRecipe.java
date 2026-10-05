package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID,
        bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
public final class CrushingRecipe implements IRecipe<IInventory> {
    public static final IRecipeType<CrushingRecipe> TYPE = IRecipeType.register("basemetals:crushing");
    public static final IRecipeSerializer<CrushingRecipe> SERIALIZER = new Serializer("crushing");
    public static final IRecipeSerializer<LegacySmeltingRecipe> LEGACY_SMELTING_SERIALIZER =
            new LegacySmeltingRecipe.Serializer();
    public static final IRecipeSerializer<PlateRepairRecipe> PLATE_REPAIR_SERIALIZER =
            new PlateRepairRecipe.Serializer();

    private final ResourceLocation id;
    private final Ingredient ingredient;
    private final ItemStack result;

    public CrushingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result) {
        this.id = id;
        this.ingredient = ingredient;
        this.result = result.copy();
    }

    @net.minecraftforge.eventbus.api.SubscribeEvent
    public static void register(net.minecraftforge.event.RegistryEvent.Register<IRecipeSerializer<?>> event) {
        event.getRegistry().registerAll(SERIALIZER, LEGACY_SMELTING_SERIALIZER,
                PLATE_REPAIR_SERIALIZER, ContentCraftingRecipe.SERIALIZER);
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
    @Override public IRecipeType<CrushingRecipe> getType() { return TYPE; }
    @Override public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(ingredient);
        return ingredients;
    }
    @Override public boolean isDynamic() { return true; }

    public static final class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<IRecipeSerializer<?>>
            implements IRecipeSerializer<CrushingRecipe> {
        private Serializer(String name) { setRegistryName(BaseMetals.MOD_ID, name); }
        @Override public CrushingRecipe read(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.deserialize(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return new CrushingRecipe(id, ingredient, result);
        }
        @Override public CrushingRecipe read(ResourceLocation id, PacketBuffer buffer) {
            return new CrushingRecipe(id, Ingredient.read(buffer), buffer.readItemStack());
        }
        @Override public void write(PacketBuffer buffer, CrushingRecipe recipe) {
            recipe.ingredient.write(buffer);
            buffer.writeItemStack(recipe.result);
        }
    }
}
