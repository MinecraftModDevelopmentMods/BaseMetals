package com.mcmoddev.lib.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.recipe.ArbitraryCrusherRecipe;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.registry.recipe.OreDictionaryCrusherRecipe;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

/**
 * This class handles all of the recipes for the crack hammer, collectively
 * referred to as crusher recipes (the crack hammer is meant to be equivalent to
 * the pulverizers and rock crushers from mods like Thermal Expansion). Adding a
 * new crusher recipe is as simple as calling one of the
 * addNewCrusherRecipe(...) static functions. To get the recipe(s) for a given
 * block or item, use
 * CrusherRecipeRegistry.getInstance().getRecipeForInputItem(...) or
 * CrusherRecipeRegistry.getInstance().getRecipesForOutputItem(...). Added
 * crusher recipes will automatically appear in the NEI crusher recipe section.
 * <p>
 * To see all of the default crusher recipes, look at the source code of the
 * class cyano.basemetals.init.Recipes
 * </p>
 * 
 * @author DrCyano
 *
 */
public class CrusherRecipeRegistry {

	private final List<ICrusherRecipe> recipes = new ArrayList<>();

	private final Map<Integer, ICrusherRecipe> recipeByInputCache = new HashMap<>();
	private final Map<Integer, List<ICrusherRecipe>> recipeByOutputCache = new HashMap<>();

	private static final Lock initLock = new ReentrantLock();
	private static CrusherRecipeRegistry instance = null;

	/**
	 * Gets a singleton instance of CrusherRecipeRegistry
	 * 
	 * @return A global instance of CrusherRecipeRegistry
	 */
	public static CrusherRecipeRegistry getInstance() {
		if (instance == null) {
			initLock.lock();
			try {
				// thread-safe singleton instantiation
				instance = new CrusherRecipeRegistry();
			} finally {
				initLock.unlock();
			}
		}
		return instance;
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is an OreDictionary name. This means that any item
	 * registered with the OreDictionary will be converted into the specified
	 * output item.
	 * 
	 * @param oreDictionaryName
	 *            Name in the ore dictionary (e.g. "logWood")
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final String oreDictionaryName, final ItemStack output) { // preferred method
		getInstance().addRecipe(new OreDictionaryCrusherRecipe(oreDictionaryName, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is specified by an ItemStack. This means that only
	 * the specified item will be converted into the specified output item.
	 * 
	 * @param input
	 *            Item to be crushed
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final ItemStack input, final ItemStack output) {
		if ((input == null) || (output == null)) {
			BaseMetals.logger.error("%s: Crusher recipe not registered because of null input or output. \n %s", CrusherRecipeRegistry.class, Arrays.toString(Thread.currentThread().getStackTrace()).replace(", ", "\n").replace("[", "").replace("]", ""));
		}

		getInstance().addRecipe(new ArbitraryCrusherRecipe(input, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is specified by an Item instance. This means that
	 * only the specified item will be converted into the specified output item.
	 * Note that this will assume an item metadata value of 0.
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
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is a block. This means that any block of this type
	 * will be converted into the specified output item. If you want to restrict
	 * the block inputs to certain metadata values, convert the block into an
	 * ItemStack instead of providing it as a block instance.
	 * 
	 * @param input
	 *            Block to be crushed
	 * @param output
	 *            The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final Block input, final ItemStack output) {
		getInstance().addRecipe(new ArbitraryCrusherRecipe(input, output));
/*
{
			@Override
			public boolean isValidInput(ItemStack in) {
				return input.equals(Block.getBlockFromItem(in.getItem()));
			}
		});
*/
	}

	/**
	 * Clears the fast-look-up cache. If recipes are added after the game
	 * starts, call this method to ensure that the new recipes will be used.
	 */
	public void clearCache() {
		this.recipeByInputCache.clear();
		this.recipeByOutputCache.clear();
	}

	/**
	 * This is the universal method for adding new crusher recipes
	 * 
	 * @param crusherRecipe
	 *            An implementation of the ICrusherRecipe interface.
	 */
	public void addRecipe(ICrusherRecipe crusherRecipe) {
		this.recipes.add(crusherRecipe);
	}

	/**
	 * Gets a list of crusher recipes whose output is equal to the specified
	 * output item. If there are no such recipes, then null is returned (instead
	 * of an empty list).
	 * 
	 * @param output
	 *            The item resulting from the crushing of another item or block
	 * @return A list of recipes producing the requested item, or null if no
	 *         such recipes exist
	 */
	public List<ICrusherRecipe> getRecipesForOutputItem(ItemStack output) {
		Integer hashKey = output.getItem().getUnlocalizedName().hashCode() + (56 * output.getMetadata());
		if (this.recipeByOutputCache.containsKey(hashKey)) {
			final List<ICrusherRecipe> recipeCache = this.recipeByOutputCache.get(hashKey);
			if (recipeCache.isEmpty()) {
				return Collections.emptyList();
			}
			return recipeCache;
		} else {
			final List<ICrusherRecipe> recipeCache = new ArrayList<>();
			for (final ICrusherRecipe r : this.recipes) {
				if (ItemStack.areItemsEqual(r.getOutput(), output)) {
					for (final String s : Options.disabledRecipes()) {
						List<ItemStack> ores = OreDictionary.getOres(s);
						for (ItemStack input : r.getValidInputs()) {
							if (OreDictionary.containsMatch(false, ores, input)) {
								this.recipeByInputCache.put(hashKey, null);
								return new ArrayList<>();
							}
						}
					}
					recipeCache.add(r);
				}
			}

			if (recipeCache.isEmpty()) {
				this.recipeByOutputCache.put(hashKey, Collections.emptyList());
				return Collections.emptyList();
			}
			this.recipeByOutputCache.put(hashKey, recipeCache);
			return recipeCache;
		}
	}

	/**
	 * Gets a list of crusher recipes whose output is equal to the specified
	 * output item. If there are no such recipes, then null is returned (instead
	 * of an empty list).
	 * 
	 * @param output
	 *            The block resulting from the crushing of another item or block
	 * @return A list of recipes producing the requested item, or null if no
	 *         such recipes exist
	 */
	public List<ICrusherRecipe> getRecipesForOutputItem(IBlockState output) {
		return getRecipesForOutputItem(new ItemStack(output.getBlock(), 1, output.getBlock().getMetaFromState(output)));
	}

	/**
	 * Gets the recipe for crushing the specified item, or null if ther is no
	 * recipe accepting the item.
	 * 
	 * @param input
	 *            The item/block to crush
	 * @return The crusher recipe for crushing this item/block, or null if no
	 *         such recipe exists
	 */
	public ICrusherRecipe getRecipeForInputItem(ItemStack input) {
		Integer hashKey = input.getItem().getUnlocalizedName().hashCode() + (56 * input.getMetadata());
		if (this.recipeByInputCache.containsKey(hashKey)) {
			return this.recipeByInputCache.get(hashKey);
		} else {
			for (final ICrusherRecipe r : this.recipes) {
				if (r.isValidInput(input)) {
					for (final String s : Options.disabledRecipes()) {
						List<ItemStack> ores = OreDictionary.getOres(s);
						if (OreDictionary.containsMatch(false, ores, input)) {
							this.recipeByInputCache.put(hashKey, null);
							return null;
						}
					}
					this.recipeByInputCache.put(hashKey, r);
					return r;
				}
			}
			this.recipeByInputCache.put(hashKey, null);
			return null;
		}
	}

	/**
	 * Gets the recipe for crushing the specified item, or null if there is no
	 * recipe accepting the item.
	 * 
	 * @param input
	 *            The item/block to crush
	 * @return The crusher recipe for crushing this item/block, or null if no
	 *         such recipe exists
	 */
	public ICrusherRecipe getRecipeForInputItem(IBlockState input) {
		return getRecipeForInputItem(new ItemStack(input.getBlock(), 1, input.getBlock().getMetaFromState(input)));
	}

	/**
	 * Gets all registered crusher recipes
	 * 
	 * @return An unmodifiable list of all registered crusher recipes
	 */
	public Collection<ICrusherRecipe> getAllRecipes() {
		List<ICrusherRecipe> filtered = new ArrayList<>();
		for (final ICrusherRecipe r : this.recipes) {
			boolean matched = false;
			for (final String s : Options.disabledRecipes()) {
				List<ItemStack> ores = OreDictionary.getOres(s);
				for (ItemStack ore : ores) {
					if (r.isValidInput(ore)) {
						matched = true;
						continue;
					}
				}
			}
			if (!matched) {
				filtered.add(r);
			}
		}
		return Collections.unmodifiableList(filtered);
	}
}
