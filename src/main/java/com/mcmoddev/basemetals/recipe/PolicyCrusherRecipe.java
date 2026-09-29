package com.mcmoddev.basemetals.recipe;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Retains a crusher recipe's persistent registry identity while making a
 * policy-restricted acquisition route non-matching and invisible.
 */
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
