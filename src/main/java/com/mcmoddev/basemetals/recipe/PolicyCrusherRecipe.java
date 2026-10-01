package com.mcmoddev.basemetals.recipe;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/** Disables a crusher recipe without removing its saved registry name. */
final class PolicyCrusherRecipe extends IForgeRegistryEntry.Impl<ICrusherRecipe>
		implements ICrusherRecipe {

	PolicyCrusherRecipe(final ICrusherRecipe delegate) {
		setRegistryName(delegate.getRegistryName());
	}

	@Override
	public List<ItemStack> getInputs() {
		return Collections.emptyList();
	}

	@Override
	public ItemStack getOutput() {
		return ItemStack.EMPTY;
	}

	@Override
	public boolean isValidInput(final ItemStack input) {
		return false;
	}

	@Override
	public Collection<ItemStack> getValidInputs() {
		return Collections.emptyList();
	}
}
