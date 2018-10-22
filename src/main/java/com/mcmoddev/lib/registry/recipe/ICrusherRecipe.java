package com.mcmoddev.lib.registry.recipe;

import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * Superclass for all crushed-item recipes. For recipes that use the OreDictionary to describe the
 * input item, use the OreDictionaryCrusherRecipe class. If not using the OreDictionary, use the
 * ArbitraryCrusherRecipe class. Note that you should use the OreDictionary for most recipes.
 *
 * @author DrCyano
 *
 */
public interface ICrusherRecipe extends IForgeRegistryEntry<ICrusherRecipe> {

	/**
	 * Gets the input item(s) from applying this recipe.
	 *
	 * @return An ItemStack instance of the result of this recipe
	 */
	public List<ItemStack> getInputs();

	/**
	 * Gets the output item from applying this recipe.
	 *
	 * @return An ItemStack instance of the result of this recipe
	 */
	public ItemStack getOutput();

	/**
	 * Checks if the given ItemStack instance is the input for this recipe.
	 *
	 * @param input
	 *            An ItemStack to test
	 * @return Returns true if and only if this recipe should produce an output item from the given
	 *         input.
	 */
	public boolean isValidInput(ItemStack input);

	/**
	 * Returns a list of all registered blocks/items for which <code>isValidInput(...)</code> would
	 * return true. This method is only used for displaying recipes in NEI and does not need to be
	 * performance optimized.
	 *
	 * @return A list of allowed inputs.
	 */
	public Collection<ItemStack> getValidInputs();
}
