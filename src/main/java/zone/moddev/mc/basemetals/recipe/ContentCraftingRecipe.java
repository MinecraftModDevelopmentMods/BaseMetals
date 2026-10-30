package zone.moddev.mc.basemetals.recipe;

import java.util.List;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import zone.moddev.mc.basemetals.config.ContentPolicy;

/** Keeps recipe IDs stable while hiding recipes that the selected mode does not allow. */
public final class ContentCraftingRecipe implements CraftingRecipe {
    public static final RecipeSerializer<ContentCraftingRecipe> SERIALIZER = Serializer.create();
    private final CraftingRecipe plain;
    private final String resultItem;

    private ContentCraftingRecipe(CraftingRecipe plain) {
        this.plain = plain;
        List<RecipeDisplay> displays = plain.display();

        if (displays.size() != 1
                || !(displays.getFirst().result() instanceof SlotDisplay.ItemStackSlotDisplay result)) {
            throw new IllegalArgumentException("Content-mode recipes need a fixed crafting output");
        }

        // Recipe loading happens before item components are ready to make stacks.
        this.resultItem = ForgeRegistries.ITEMS.getKey(result.stack().item().value()).toString();
    }

    private boolean allowed() {
        return ContentPolicy.active().allows(resultItem);
    }

    private static ContentCraftingRecipe wrap(Recipe<?> recipe) {
        if (!(recipe instanceof CraftingRecipe crafting)) {
            throw new IllegalArgumentException("Expected a crafting recipe, got " + recipe.getSerializer());
        }

        return new ContentCraftingRecipe(crafting);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        return allowed() && plain.matches(input, level);
    }

    @Override
    public ItemStack assemble(CraftingInput input) {
        return allowed() ? plain.assemble(input) : ItemStack.EMPTY;
    }

    @Override
    public boolean isSpecial() {
        return !allowed() || plain.isSpecial();
    }

    @Override
    public String group() {
        return plain.group();
    }

    @Override
    public CraftingBookCategory category() {
        return plain.category();
    }

    @Override
    public PlacementInfo placementInfo() {
        return allowed() ? plain.placementInfo() : PlacementInfo.NOT_PLACEABLE;
    }

    @Override
    public List<RecipeDisplay> display() {
        return allowed() ? plain.display() : List.of();
    }

    @Override
    public boolean showNotification() {
        return plain.showNotification();
    }

    @Override
    public RecipeSerializer<ContentCraftingRecipe> getSerializer() {
        return SERIALIZER;
    }

    private static final class Serializer {
        private static final MapCodec<ContentCraftingRecipe> CODEC = Recipe.DIRECT_CODEC.fieldOf("recipe")
                .xmap(ContentCraftingRecipe::wrap, recipe -> recipe.plain);
        // Preserve empty pattern slots and tag names instead of flattening the ingredients.
        private static final StreamCodec<RegistryFriendlyByteBuf, ContentCraftingRecipe> STREAM_CODEC =
                ByteBufCodecs.fromCodecWithRegistries(Recipe.DIRECT_CODEC)
                        .map(ContentCraftingRecipe::wrap, recipe -> recipe.plain);

        private static RecipeSerializer<ContentCraftingRecipe> create() {
            return new RecipeSerializer<>(CODEC, STREAM_CODEC);
        }
    }
}
