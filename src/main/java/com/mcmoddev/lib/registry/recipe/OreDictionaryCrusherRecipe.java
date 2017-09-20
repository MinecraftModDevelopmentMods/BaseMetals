package com.mcmoddev.lib.registry.recipe;

import java.util.Collection;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This class is an implementation of the ICrusherRecipe superclass. It uses the
 * OreDictionary to check the input item.
 * 
 * @author DrCyano
 *
 */
public class OreDictionaryCrusherRecipe  extends IForgeRegistryEntry.Impl<ICrusherRecipe> implements ICrusherRecipe {

	private List<ItemStack> inputs;
	private final ItemStack output;
	private final String oreDictSource;

	/**
	 * Constructs a new instance of this ICrusherRecipe class representing a
	 * recipe with an input and an output.
	 * 
	 * @param oreDictionaryID
	 *            The input item(s), described by an OreDictionary name
	 * @param results
	 *            The result of applying this recipe to an input item
	 */
	public OreDictionaryCrusherRecipe(String oreDictionaryID, ItemStack results) {
		this.oreDictSource = oreDictionaryID;
		this.inputs = OreDictionary.getOres(this.oreDictSource);
		this.output = results;
		
		super.setRegistryName(oreDictionaryID + "_to_" + results.getItem().getRegistryName().getResourcePath());
	}

	/**
	 * Gets the output item from applying this recipe.
	 * 
	 * @return An ItemStack instance of the result of this recipe
	 */
	@Override
	public List<ItemStack> getInputs() {
		return this.inputs;
	}

	/**
	 * Gets the output item from applying this recipe.
	 * 
	 * @return An ItemStack instance of the result of this recipe
	 */
	@Override
	public ItemStack getOutput() {
		return this.output.copy();
	}

	/**
	 * Checks if the given ItemStack instance is the input for this recipe.
	 * 
	 * @param input
	 *            An ItemStack to test
	 * @return Returns true if and only if this recipe should produce an output
	 *         item from the given input.
	 */
	@Override
	public boolean isValidInput(ItemStack input) {
		for (int i = 0; i < inputs.size(); i++) {
			if (inputs.get(i).getMetadata() == OreDictionary.WILDCARD_VALUE) {
				// do not compare metadata values
				if (inputs.get(i).getItem() == input.getItem())
					return true;
			} else if (ItemStack.areItemsEqual(inputs.get(i), input)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a list of all registered blocks/items for which
	 * <code>isValidInput(...)</code> would return true. This method is only
	 * used for displaying recipes in NEI and does not need to be performance
	 * optimized.
	 * 
	 * @return A list of allowed inputs.
	 */
	@Override
	public Collection<ItemStack> getValidInputs() {
		return OreDictionary.getOres(this.oreDictSource);
	}
}
