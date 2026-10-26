package zone.moddev.mc.basemetals.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.BuiltInRegistries;

import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * A matching plate fully repairs one damaged armor piece or shield without changing its item data.
 */
public final class PlateRepairRecipe extends CustomRecipe {
    private final Item target;
    private final Ingredient plate;

    public PlateRepairRecipe(Item target, Ingredient plate) {
        super(net.minecraft.world.item.crafting.CraftingBookCategory.MISC);
        this.target = target;
        this.plate = plate;
    }

    @Override
    public boolean matches(CraftingInput container, Level level) {
        if (!zone.moddev.mc.basemetals.config.ContentPolicy.active().allows(ForgeRegistries.ITEMS.getKey(target).toString())) return false;
        ItemStack foundTarget = ItemStack.EMPTY;
        boolean foundPlate = false;
        for (int slot = 0; slot < container.size(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (stack.isEmpty()) continue;
            if (isTarget(stack) && stack.isDamaged() && foundTarget.isEmpty()) {
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
    public ItemStack assemble(CraftingInput container, net.minecraft.core.HolderLookup.Provider registries) {
        if (!matches(container, null)) return ItemStack.EMPTY;
        for (int slot = 0; slot < container.size(); slot++) {
            ItemStack stack = container.getItem(slot);
            if (isTarget(stack) && stack.isDamaged()) {
                ItemStack repaired = stack.copy();
                repaired.setCount(1);
                repaired.setDamageValue(0);
                return repaired;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean isTarget(ItemStack stack) {
        if (stack.is(target)) return true;

        String oldId = ForgeRegistries.ITEMS.getKey(target).toString();
        String equivalent = zone.moddev.mc.basemetals.compat.VanillaCopper.output(oldId);
        return !equivalent.equals(oldId)
                && equivalent.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()).toString());
    }

    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 2;
    }

    public ItemStack getResultItem(net.minecraft.core.HolderLookup.Provider registries) {
        return target.getDefaultInstance();
    }

    public java.util.List<Ingredient> getIngredients() {
        return java.util.List.of(Ingredient.of(target), plate);
    }

    @Override
    public RecipeSerializer<PlateRepairRecipe> getSerializer() {
        return CrushingRecipe.PLATE_REPAIR_SERIALIZER.get();
    }

    public static final class Serializer implements RecipeSerializer<PlateRepairRecipe> {
        private static final MapCodec<PlateRepairRecipe> CODEC = RecordCodecBuilder.mapCodec(instance ->
                instance.group(
                        BuiltInRegistries.ITEM.byNameCodec().fieldOf("target").forGetter(recipe -> recipe.target),
                        Ingredient.CODEC.fieldOf("plate").forGetter(recipe -> recipe.plate))
                        .apply(instance, PlateRepairRecipe::new));

        private static final StreamCodec<RegistryFriendlyByteBuf, PlateRepairRecipe> STREAM_CODEC =
                StreamCodec.composite(
                        ByteBufCodecs.registry(Registries.ITEM), recipe -> recipe.target,
                        Ingredient.CONTENTS_STREAM_CODEC, recipe -> recipe.plate,
                        PlateRepairRecipe::new);

        @Override public MapCodec<PlateRepairRecipe> codec() { return CODEC; }
        @Override public StreamCodec<RegistryFriendlyByteBuf, PlateRepairRecipe> streamCodec() { return STREAM_CODEC; }
    }
}
