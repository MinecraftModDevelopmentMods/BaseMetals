package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * A matching plate fully repairs one damaged armor piece or shield without changing its item data.
 */
public final class PlateRepairRecipe extends CustomRecipe {
    private final Item target;
    private final Ingredient plate;

    public PlateRepairRecipe(ResourceLocation id, Item target, Ingredient plate) {
        super(id);
        this.target = target;
        this.plate = plate;
    }

    @Override
    public boolean matches(CraftingContainer container, Level level) {
        if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(target.getRegistryName().toString())) return false;
        ItemStack foundTarget = ItemStack.EMPTY;
        boolean foundPlate = false;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) continue;
            if (stack.is(target) && stack.isDamaged() && foundTarget.isEmpty()) {
                foundTarget = stack;
            } else if (plate.test(stack) && !foundPlate) {
                foundPlate = true;
            } else {
                return false;
            }
        }
        return !foundTarget.isEmpty() && foundPlate;
    }

    @Override
    public ItemStack assemble(CraftingContainer container) {
        if (!matches(container, null)) return ItemStack.EMPTY;
        for (int slot = 0; slot < container.getContainerSize(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.is(target) && stack.isDamaged()) {
                ItemStack repaired = stack.copy();
                repaired.setCount(1);
                repaired.setDamageValue(0);
                return repaired;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getResultItem() {
        return target.getDefaultInstance();
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return NonNullList.of(Ingredient.EMPTY, Ingredient.of(target), plate);
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrushingRecipe.PLATE_REPAIR_SERIALIZER.get();
    }

    public static final class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>>
            implements RecipeSerializer<PlateRepairRecipe> {
        @Override
        public PlateRepairRecipe fromJson(ResourceLocation id, JsonObject json) {
            ResourceLocation targetId = new ResourceLocation(GsonHelper.getAsString(json, "target"));
            Item target = ForgeRegistries.ITEMS.getValue(targetId);
            if (target == null || target == Items.AIR) {
                throw new com.google.gson.JsonSyntaxException("Unknown plate-repair target " + targetId);
            }
            return new PlateRepairRecipe(id, target,
                    Ingredient.fromJson(GsonHelper.getAsJsonObject(json, "plate")));
        }

        @Override
        public PlateRepairRecipe fromNetwork(ResourceLocation id, FriendlyByteBuf buffer) {
            Item target = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
            if (target == null || target == Items.AIR) {
                throw new IllegalStateException("Missing plate-repair target");
            }
            return new PlateRepairRecipe(id, target, Ingredient.fromNetwork(buffer));
        }

        @Override
        public void toNetwork(FriendlyByteBuf buffer, PlateRepairRecipe recipe) {
            buffer.writeResourceLocation(recipe.target.getRegistryName());
            recipe.plate.toNetwork(buffer);
        }
    }
}
