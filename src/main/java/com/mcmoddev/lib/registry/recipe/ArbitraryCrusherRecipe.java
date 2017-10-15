package com.mcmoddev.lib.registry.recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

/**
 * This class is an implementation of the ICrusherRecipe superclass. It uses the
 * <code>ItemStack.areItemsEqual(...)</code> method to check the input item.
 * 
 * @author DrCyano
 *
 */
public class ArbitraryCrusherRecipe extends IForgeRegistryEntry.Impl<ICrusherRecipe> implements ICrusherRecipe {
	
	private final ItemStack input;
	private final ItemStack output;
	
	/**
	 * Constructs a new instance of this ICrusherRecipe class representing a
	 * recipe with an input and an output. If the input ItemStack has
	 * OreDictionary.WILDCARD_VALUE as its damage value, then metadata values
	 * will be ignored when testing an item for being a valid input for this
	 * recipe.
	 * 
	 * @param input
	 *            The input item as a direct ItemStack reference.
	 * @param output
	 *            The result of applying this recipe to an input item
	 */
	public ArbitraryCrusherRecipe(@Nonnull ItemStack input, @Nonnull ItemStack output) {
		this.input = input;
		this.output = output;

		super.setRegistryName(input.getItem().getRegistryName().getResourcePath() + "_to_" + output.getItem().getRegistryName().getResourcePath());
	}

	/**
	 * Constructs a new instance of this ICrusherRecipe class representing a
	 * recipe with an input and an output. If the input ItemStack has
	 * OreDictionary.WILDCARD_VALUE as its damage value, then metadata values
	 * will be ignored when testing an item for being a valid input for this
	 * recipe.
	 * 
	 * @param input
	 *            The input item as an Item reference.
	 * @param output
	 *            The result of applying this recipe to an input item
	 */
	public ArbitraryCrusherRecipe(@Nonnull Item input, @Nonnull ItemStack output) {
		this.input = new ItemStack(input);
		this.output = output;
		
		super.setRegistryName(input.getRegistryName().getResourcePath() + "_to_" + output.getItem().getRegistryName().getResourcePath());
	}

	/**
	 * Constructs a new instance of this ICrusherRecipe class representing a
	 * recipe with an input and an output. If the input ItemStack has
	 * OreDictionary.WILDCARD_VALUE as its damage value, then metadata values
	 * will be ignored when testing an item for being a valid input for this
	 * recipe.
	 * 
	 * @param input
	 *            The input item as an Item reference.
	 * @param output
	 *            The result of applying this recipe to an input item
	 */
	public ArbitraryCrusherRecipe(@Nonnull Block input, @Nonnull ItemStack output) {
		this.input = new ItemStack(input);
		this.output = output;
		
		super.setRegistryName(input.getRegistryName().getResourcePath() + "_to_" + output.getItem().getRegistryName().getResourcePath());
	}

	/**
	 * Gets the output item from applying this recipe.
	 * 
	 * @return An ItemStack instance of the result of this recipe
	 */
	@Override
	public List<ItemStack> getInputs() {
		return Collections.singletonList(this.input.copy());
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
		if ((input != null) && (this.input.getItemDamage() == OreDictionary.WILDCARD_VALUE))
			return this.input.getItem() == input.getItem();
		return ItemStack.areItemsEqual(this.input, input);
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
		return Collections.singletonList(this.input);
	}
}
