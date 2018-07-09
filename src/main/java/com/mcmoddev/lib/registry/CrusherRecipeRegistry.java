package com.mcmoddev.lib.registry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.recipe.ArbitraryCrusherRecipe;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.registry.recipe.OreDictionaryCrusherRecipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.RegistrySimple;

/**
 * This class handles all of the recipes for the crack hammer, collectively referred to as crusher
 * recipes (the crack hammer is meant to be equivalent to the pulverizers and rock crushers from
 * mods like Thermal Expansion). Adding a new crusher recipe is as simple as calling one of the
 * addNewCrusherRecipe(...) static functions. To get the recipe(s) for a given block or item, use
 * CrusherRecipeRegistry.getInstance().getRecipeForInputItem(...) or
 * CrusherRecipeRegistry.getInstance().getRecipesForOutputItem(...). Added crusher recipes will
 * automatically appear in the NEI crusher recipe section.
 * <p>
 * To see all of the default crusher recipes, look at the source code of the class
 * cyano.basemetals.init.Recipes
 * </p>
 *
 * @author DrCyano
 *
 */
public class CrusherRecipeRegistry {
	private final RegistrySimple<ResourceLocation,ICrusherRecipe> recipes = new RegistrySimple<>();
	
	private static CrusherRecipeRegistry instance = null;

	/**
	 * Gets a singleton instance of CrusherRecipeRegistry
	 *
	 * @return A global instance of CrusherRecipeRegistry
	 */
	public static CrusherRecipeRegistry getInstance() {
		if (instance == null) {
			instance = new CrusherRecipeRegistry();
		}
		return instance;
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers) where the input item
	 * is an OreDictionary name. This means that any item registered with the OreDictionary will be
	 * converted into the specified output item.
	 *
	 * @param oreDictionaryName
	 *            Name in the ore dictionary (e.g. "logWood")
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final String oreDictionaryName, final ItemStack output) { // preferred
																										// method
		getInstance().addRecipe(new OreDictionaryCrusherRecipe(oreDictionaryName, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers) where the input item
	 * is specified by an ItemStack. This means that only the specified item will be converted into
	 * the specified output item.
	 *
	 * @param input
	 *            Item to be crushed
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final ItemStack input, final ItemStack output) {
		if ((input == null) || (output == null)) {
			BaseMetals.logger.error(
					"%s: Crusher recipe not registered because of null input or output. \n %s",
					CrusherRecipeRegistry.class,
					Arrays.toString(Thread.currentThread().getStackTrace()).replace(", ", "\n")
							.replace("[", "").replace("]", ""));
		}

		if ((input.isEmpty()) || (output.isEmpty())) {
			BaseMetals.logger.error(
					"%s: Crusher recipe not registered because of empty input or output. \n %s",
					CrusherRecipeRegistry.class,
					Arrays.toString(Thread.currentThread().getStackTrace()).replace(", ", "\n")
							.replace("[", "").replace("]", ""));
		}

		getInstance().addRecipe(new ArbitraryCrusherRecipe(input, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers) where the input item
	 * is specified by an Item instance. This means that only the specified item will be converted
	 * into the specified output item. Note that this will assume an item metadata value of 0.
	 *
	 * @param input
	 *            Item to be crushed
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final Item input, final ItemStack output) {
		getInstance().addRecipe(new ArbitraryCrusherRecipe(input, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers) where the input item
	 * is a block. This means that any block of this type will be converted into the specified
	 * output item. If you want to restrict the block inputs to certain metadata values, convert the
	 * block into an ItemStack instead of providing it as a block instance.
	 *
	 * @param input
	 *            Block to be crushed
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final Block input, final ItemStack output) {
		getInstance().addRecipe(new ArbitraryCrusherRecipe(input, output));
		/*
		 * {
		 * 
		 * @Override public boolean isValidInput(ItemStack in) { return
		 * input.equals(Block.getBlockFromItem(in.getItem())); } });
		 */
	}

	/**
	 * This is the universal method for adding new crusher recipes
	 *
	 * @param crusherRecipe
	 *            An implementation of the ICrusherRecipe interface.
	 */
	public void addRecipe(final ICrusherRecipe crusherRecipe) {
		this.recipes.putObject(crusherRecipe.getResourceLocation(), crusherRecipe);
	}

	/**
	 * Gets a list of crusher recipes whose output is equal to the specified output item. If there
	 * are no such recipes, then null is returned (instead of an empty list).
	 *
	 * @param output
	 *            The item resulting from the crushing of another item or block
	 * @return A list of recipes producing the requested item, or null if no such recipes exist
	 */
	public List<ICrusherRecipe> getRecipesForOutputItem(final ItemStack output) {
		return this.recipes.getKeys().stream()
				.filter(rl -> this.recipes.getObject(rl).getOutput().equals(output))
				.map(rl -> this.recipes.getObject(rl))
				.collect(Collectors.toList());
	}

	/**
	 * Gets a list of crusher recipes whose output is equal to the specified output item. If there
	 * are no such recipes, then null is returned (instead of an empty list).
	 *
	 * @param output
	 *            The block resulting from the crushing of another item or block
	 * @return A list of recipes producing the requested item, or null if no such recipes exist
	 */
	public List<ICrusherRecipe> getRecipesForOutputItem(final IBlockState output) {
		return this.getRecipesForOutputItem(
				new ItemStack(output.getBlock(), 1, output.getBlock().getMetaFromState(output)));
	}

	/**
	 * Gets the recipe for crushing the specified item, or null if ther is no recipe accepting the
	 * item.
	 *
	 * @param input
	 *            The item/block to crush
	 * @return The crusher recipe for crushing this item/block, or null if no such recipe exists
	 */
	public ICrusherRecipe getRecipeForInputItem(final ItemStack input) {
		return this.recipes.getKeys().stream()
				.filter(rl -> this.recipes.getObject(rl).getInputs().contains(input))
				.map(rl -> this.recipes.getObject(rl))
				.findFirst().orElse(null);
	}

	/**
	 * Gets the recipe for crushing the specified item, or null if there is no recipe accepting the
	 * item.
	 *
	 * @param input
	 *            The item/block to crush
	 * @return The crusher recipe for crushing this item/block, or null if no such recipe exists
	 */
	public ICrusherRecipe getRecipeForInputItem(final IBlockState input) {
		return this.getRecipeForInputItem(
				new ItemStack(input.getBlock(), 1, input.getBlock().getMetaFromState(input)));
	}

	/**
	 * Gets all registered crusher recipes
	 *
	 * @return An unmodifiable list of all registered crusher recipes
	 */
	public Collection<ICrusherRecipe> getAllRecipes() {
		return Collections.unmodifiableList(
				this.recipes.getKeys().stream()
				.map(rl -> this.recipes.getObject(rl))
				.collect(Collectors.toList()));
	}
}
