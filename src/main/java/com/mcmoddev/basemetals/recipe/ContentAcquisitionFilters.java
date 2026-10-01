package com.mcmoddev.basemetals.recipe;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.basemetals.content.ContentMode;
import com.mcmoddev.basemetals.content.ContentPolicy;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/** Filters crafting, smelting, and crushing recipes for the selected content mode. */
public final class ContentAcquisitionFilters {
	private ContentAcquisitionFilters() {
	}

	public static void apply(final RegistryEvent.Register<IRecipe> event) {
		final ContentPolicy policy = ContentPolicy.active();

		if (policy.mode() == ContentMode.HIGH_FANTASY) {
			return;
		}

		wrapCraftingRecipes(event, policy);
		filterFurnaceRecipes(policy);
		filterCrusherRecipes(policy);
	}

	private static void wrapCraftingRecipes(final RegistryEvent.Register<IRecipe> event,
			final ContentPolicy policy) {
		final List<IRecipe> recipes = new ArrayList<>();

		for (final IRecipe recipe : event.getRegistry()) {
			if (recipe.getRegistryName() != null
					&& BaseMetals.MODID.equals(recipe.getRegistryName().getNamespace())) {
				recipes.add(recipe);
			}
		}

		for (final IRecipe recipe : recipes) {
			final PolicyRecipe wrapped = new PolicyRecipe(recipe, policy);

			if (!wrapped.isAllowed()) {
				event.getRegistry().register(wrapped);
			}
		}
	}

	private static void filterFurnaceRecipes(final ContentPolicy policy) {
		final Map<ItemStack, ItemStack> recipes = FurnaceRecipes.instance().getSmeltingList();
		final List<Map.Entry<ItemStack, ItemStack>> snapshot = new ArrayList<>(recipes.entrySet());

		for (final Map.Entry<ItemStack, ItemStack> recipe : snapshot) {
			if (!policy.allows(recipe.getValue())) {
				recipes.remove(recipe.getKey());
			}
		}
	}

	private static void filterCrusherRecipes(final ContentPolicy policy) {
		final List<ICrusherRecipe> recipes = CrusherRecipeRegistry.getAll();

		for (final ICrusherRecipe recipe : recipes) {
			if (!policy.allows(recipe.getOutput())) {
				final ResourceLocation id = recipe.getRegistryName();

				if (id != null) {
					CrusherRecipeRegistry.removeByName(id);
					CrusherRecipeRegistry.getInstance().register(
							new PolicyCrusherRecipe(recipe));
				}
			}
		}
	}
}
