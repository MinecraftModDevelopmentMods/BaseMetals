package zone.moddev.mc.basemetals.recipe;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import zone.moddev.mc.basemetals.BaseMetals;
import zone.moddev.mc.basemetals.config.ContentPolicy;

import net.minecraft.init.Items;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeHidden;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public final class PlateRepairRecipe extends IRecipeHidden {
    private final Item target;
    private final Ingredient plate;

    public PlateRepairRecipe(ResourceLocation id, Item target, Ingredient plate) {
        super(id);
        this.target = target;
        this.plate = plate;
    }

    @Override
    public boolean matches(IInventory inventory, World world) {
        if (!ContentPolicy.active().allows(target.getRegistryName().toString())) return false;
        ItemStack foundTarget = ItemStack.EMPTY;
        boolean foundPlate = false;

        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);

            if (stack.isEmpty()) {
                continue;
            }

            if (stack.getItem() == target && stack.isDamaged() && foundTarget.isEmpty()) {
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
    public ItemStack getCraftingResult(IInventory inventory) {
        if (!matches(inventory, null)) {
            return ItemStack.EMPTY;
        }

        for (int slot = 0; slot < inventory.getSizeInventory(); slot++) {
            ItemStack stack = inventory.getStackInSlot(slot);

            if (stack.getItem() == target && stack.isDamaged()) {
                ItemStack repaired = stack.copy();
                repaired.setCount(1);
                repaired.setDamage(0);

                return repaired;
            }
        }

        return ItemStack.EMPTY;
    }
    @Override
    public boolean canFit(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(target);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        ingredients.add(Ingredient.fromItems(target));
        ingredients.add(plate);

        return ingredients;
    }

    @Override
    public IRecipeSerializer<?> getSerializer() {
        return CrushingRecipe.PLATE_REPAIR_SERIALIZER;
    }

    public static final class Serializer implements IRecipeSerializer<PlateRepairRecipe> {
        private final ResourceLocation name = new ResourceLocation(BaseMetals.MOD_ID, "plate_repair");
        @Override public PlateRepairRecipe read(ResourceLocation id, JsonObject json) {
            ResourceLocation targetId = new ResourceLocation(JsonUtils.getString(json, "target"));
            Item target = ForgeRegistries.ITEMS.getValue(targetId);
            if (target == null || target == Items.AIR) throw new JsonSyntaxException("Unknown target " + targetId);
            return new PlateRepairRecipe(id, target, Ingredient.fromJson(JsonUtils.getJsonObject(json, "plate")));
        }
        @Override public PlateRepairRecipe read(ResourceLocation id, PacketBuffer buffer) {
            Item target = ForgeRegistries.ITEMS.getValue(buffer.readResourceLocation());
            if (target == null || target == Items.AIR) throw new IllegalStateException("Missing target");
            return new PlateRepairRecipe(id, target, Ingredient.fromBuffer(buffer));
        }
        @Override public void write(PacketBuffer buffer, PlateRepairRecipe recipe) {
            buffer.writeResourceLocation(recipe.target.getRegistryName());
            recipe.plate.writeToBuffer(buffer);
        }
        @Override public ResourceLocation getName() { return name; }
    }
}
