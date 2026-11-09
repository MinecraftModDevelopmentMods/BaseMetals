package zone.moddev.mc.basemetals.content;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.ModContainer;
import zone.moddev.mc.basemetals.BaseMetals;

/** Filters only the recipes added by Base Metals, leaving earlier mod and pack recipes alone. */
public final class ContentAcquisition {
	private ContentAcquisition() {
	}

	/** Shared integration helpers must not apply Base Metals' mode to another mod's recipes. */
	public static boolean restrictsIntegration() {
		if (ContentPolicy.active().mode() == ContentMode.HIGH_FANTASY) {
			return false;
		}

		final ModContainer owner = Loader.instance().activeModContainer();
		return owner != null && BaseMetals.MODID.equals(owner.getModId());
	}

	public static boolean allowsIntegrationOutput(final ItemStack output) {
		return !restrictsIntegration() || ContentPolicy.active().allows(output);
	}

	public static void registerRecipes(final Runnable register) {
		final ContentPolicy policy = ContentPolicy.active();
		if (policy.mode() == ContentMode.HIGH_FANTASY) {
			register.run();
			return;
		}

		final List<IRecipe> crafting = CraftingManager.getInstance().getRecipeList();
		final Map<ItemStack, ItemStack> smelting = FurnaceRecipes.instance().getSmeltingList();
		final Set<IRecipe> previousCrafting = Collections.newSetFromMap(new IdentityHashMap<>());
		final Set<ItemStack> previousSmelting = Collections.newSetFromMap(new IdentityHashMap<>());
		previousCrafting.addAll(crafting);
		previousSmelting.addAll(smelting.keySet());

		CrusherRecipeRegistry.getInstance().registerWithOutputFilter(register, policy::allows);
		crafting.removeIf(recipe -> !previousCrafting.contains(recipe)
				&& !policy.allows(recipe.getRecipeOutput()));
		smelting.entrySet().removeIf(recipe -> !previousSmelting.contains(recipe.getKey())
				&& !policy.allows(recipe.getValue()));
	}
}
