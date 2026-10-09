package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.common.crafting.CraftingHelper;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(modid = BaseMetals.MOD_ID,
        bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
public final class CrushingRecipe implements Recipe<Container> {
    public static final RecipeType<CrushingRecipe> TYPE = RecipeType.register("basemetals:crushing");
    public static final RecipeSerializer<CrushingRecipe> SERIALIZER = new Serializer("crushing");
    public static final RecipeSerializer<LegacySmeltingRecipe> LEGACY_SMELTING_SERIALIZER =
            new LegacySmeltingRecipe.Serializer();
    public static final RecipeSerializer<PlateRepairRecipe> PLATE_REPAIR_SERIALIZER =
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
    public static void register(net.minecraftforge.event.RegistryEvent.Register<RecipeSerializer<?>> event) {
        event.getRegistry().registerAll(SERIALIZER, LEGACY_SMELTING_SERIALIZER,
                PLATE_REPAIR_SERIALIZER, ContentCraftingRecipe.SERIALIZER);
    }

    @Override public boolean matches(Container inventory, Level world) {
        return ContentPolicy.active().allows(result.getItem().getRegistryName().toString())
                && inventory.getContainerSize() > 0 && ingredient.test(inventory.getItem(0));
    }
    @Override public ItemStack assemble(Container inventory) { return result.copy(); }
    @Override public boolean canCraftInDimensions(int width, int height) { return width * height >= 1; }
    @Override public ItemStack getResultItem() { return result.copy(); }
    @Override public ResourceLocation getId() { return id; }
    @Override public RecipeSerializer<?> getSerializer() { return SERIALIZER; }
    @Override public RecipeType<CrushingRecipe> getType() { return TYPE; }
    @Override public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(ingredient);
        return ingredients;
    }
    @Override public boolean isSpecial() { return true; }

    public static final class Serializer extends net.minecraftforge.registries.ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<CrushingRecipe> {
        private Serializer(String name) { setRegistryName(BaseMetals.MOD_ID, name); }
        @Override public CrushingRecipe fromJson(ResourceLocation id, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("ingredient"));
            ItemStack result = CraftingHelper.getItemStack(json.getAsJsonObject("result"), true);
            return new CrushingRecipe(id, ingredient, result);
        }
        @Override public CrushingRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            return new CrushingRecipe(id, Ingredient.fromNetwork(buffer), buffer.readItem());
        }
        @Override public void toNetwork(FriendlyByteBuf buffer, CrushingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeItem(recipe.result);
        }
    }
}
