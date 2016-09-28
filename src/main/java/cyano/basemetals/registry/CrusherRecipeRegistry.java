package cyano.basemetals.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.registry.recipe.ArbitraryCrusherRecipe;
import cyano.basemetals.registry.recipe.ICrusherRecipe;
import cyano.basemetals.registry.recipe.OreDictionaryCrusherRecipe;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;

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

	private final Map<ItemLookupReference, ICrusherRecipe> recipeByInputCache = new HashMap<>();
	private final Map<ItemLookupReference, List<ICrusherRecipe>> recipeByOutputCache = new HashMap<>();

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
				if (instance == null)
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
	 * @param oreDictionaryName Name in the ore dictionary (e.g. "logWood")
	 * @param output The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final String oreDictionaryName, final ItemStack output) { // preferred method
		getInstance().addRecipe(new OreDictionaryCrusherRecipe(oreDictionaryName, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is specified by an ItemStack. This means that only
	 * the specified item will be converted into the specified output item.
	 * 
	 * @param input Item to be crushed
	 * @param output The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final ItemStack input, final ItemStack output) {
		if ((input == null) || (output == null))
			FMLLog.severe("%s: %s: Crusher recipe not registered because of null input or output. \n %s",
				BaseMetals.MODID, CrusherRecipeRegistry.class,
				Arrays.toString(Thread.currentThread().getStackTrace()).replace(", ", "\n").replace("[", "").replace("]", "")
				);
		getInstance().addRecipe(new ArbitraryCrusherRecipe(input, output));
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is specified by an Item instance. This means that
	 * only the specified item will be converted into the specified output item.
	 * Note that this will assume an item metadata value of 0.
	 * 
	 * @param input Item to be crushed
	 * @param output The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final Item input, final ItemStack output) {
		getInstance().addRecipe(new ICrusherRecipe() {

			@Override
			public ItemStack getOutput() {
				return output;
			}

			@Override
			public boolean isValidInput(ItemStack in) {
				return input.equals(in.getItem());
			}

			@Override
			public Collection<ItemStack> getValidInputs() {
				return Collections.singletonList(new ItemStack(input));
			}
		});
	}

	/**
	 * Adds a new crusher recipe (for the crack hammer and other rock crushers)
	 * where the input item is a block. This means that any block of this type
	 * will be converted into the specified output item. If you want to restrict
	 * the block inputs to certain metadata values, convert the block into an
	 * ItemStack instead of providing it as a block instance.
	 * 
	 * @param input Block to be crushed
	 * @param output The item to create as the result of this crusher recipe.
	 */
	public static void addNewCrusherRecipe(final Block input, final ItemStack output) {
		getInstance().addRecipe(new ICrusherRecipe() {

			@Override
			public ItemStack getOutput() {
				return output;
			}

			@Override
			public boolean isValidInput(ItemStack in) {
				return input.equals(Block.getBlockFromItem(in.getItem()));
			}

			@Override
			public Collection<ItemStack> getValidInputs() {
				return Collections.singletonList(new ItemStack(input));
			}
		});
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
	 * @param crusherRecipe An implementation of the ICrusherRecipe interface.
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
		final ItemLookupReference ref = new ItemLookupReference(output);
		if (this.recipeByOutputCache.containsKey(ref)) {
			final List<ICrusherRecipe> recipeCache = this.recipeByOutputCache.get(ref);
			if (recipeCache.isEmpty())
				return null;
			return recipeCache;
		} else {
			// add recipe cache
			final List<ICrusherRecipe> recipeCache = new ArrayList<>();
			for (final ICrusherRecipe r : this.recipes)
				if (ItemStack.areItemsEqual(r.getOutput(), output))
					recipeCache.add(r);
			this.recipeByOutputCache.put(ref, recipeCache);
			if (recipeCache.isEmpty())
				return null;
			return recipeCache;
		}
	}

	/**
	 * Gets a list of crusher recipes whose output is equal to the specified
	 * output item. If there are no such recipes, then null is returned (instead
	 * of an empty list).
	 * 
	 * @param output The block resulting from the crushing of another item or block
	 * @return A list of recipes producing the requested item, or null if no
	 * such recipes exist
	 */
	public List<ICrusherRecipe> getRecipesForOutputItem(IBlockState output) {
		final ItemLookupReference ref = new ItemLookupReference(output);
		if (this.recipeByOutputCache.containsKey(ref)) {
			final List<ICrusherRecipe> recipeCache = this.recipeByOutputCache.get(ref);
			if (recipeCache.isEmpty())
				return null;
			return recipeCache;
		} else {
			// add recipe cache
			final List<ICrusherRecipe> recipeCache = new ArrayList<>();
			for (final ICrusherRecipe r : this.recipes)
				if (ref.equals(r.getOutput()))
					recipeCache.add(r);
			this.recipeByOutputCache.put(ref, recipeCache);
			if (recipeCache.isEmpty())
				return null;
			return recipeCache;
		}
	}

	/**
	 * Gets the recipe for crushing the specified item, or null if ther is no
	 * recipe accepting the item.
	 * 
	 * @param input The item/block to crush
	 * @return The crusher recipe for crushing this item/block, or null if no
	 * such recipe exists
	 */
	public ICrusherRecipe getRecipeForInputItem(ItemStack input) {
		final ItemLookupReference ref = new ItemLookupReference(input);
		if (this.recipeByInputCache.containsKey(ref))
			return this.recipeByInputCache.get(ref);
		else {
			for (final ICrusherRecipe r : this.recipes)
				if (r.isValidInput(input)) {
					this.recipeByInputCache.put(ref, r);
					return r;
				}
			// no recipes, cache null result
			this.recipeByInputCache.put(ref, null);
			return null;
		}
	}

	/**
	 * Gets the recipe for crushing the specified item, or null if ther is no
	 * recipe accepting the item.
	 * 
	 * @param input The item/block to crush
	 * @return The crusher recipe for crushing this item/block, or null if no
	 * such recipe exists
	 */
	public ICrusherRecipe getRecipeForInputItem(IBlockState input) {
		final ItemLookupReference ref = new ItemLookupReference(input);
		final ItemStack stack = new ItemStack(input.getBlock(), 1, ref.metaData);
		if (this.recipeByInputCache.containsKey(ref))
			return this.recipeByInputCache.get(ref);
		else {
			for (final ICrusherRecipe r : this.recipes)
				if (r.isValidInput(stack)) {
					this.recipeByInputCache.put(ref, r);
					return r;
				}
			// no recipes, cache null result
			this.recipeByInputCache.put(ref, null);
			return null;
		}
	}

	/**
	 * Gets all registered crusher recipes
	 * 
	 * @return An unmodifiable list of all registered crusher recipes
	 */
	public Collection<ICrusherRecipe> getAllRecipes() {
		return Collections.unmodifiableList(this.recipes);
	}

	private static final class ItemLookupReference {
		final Item item;
		final int metaData;
		final int hashCache;

		public ItemLookupReference(ItemStack inputItem) {
			this.item = inputItem.getItem();
			this.metaData = inputItem.getMetadata();
			this.hashCache = this.item.getUnlocalizedName().hashCode() + (57 * this.metaData);
		}

		public ItemLookupReference(IBlockState inputBlock) {
			this.item = Item.getItemFromBlock(inputBlock.getBlock());
			this.metaData = inputBlock.getBlock().getMetaFromState(inputBlock);
			this.hashCache = inputBlock.getBlock().getUnlocalizedName().hashCode() + (57 * this.metaData);
		}

		@Override
		public boolean equals(Object other) {
			if (other instanceof ItemLookupReference) {
				final ItemLookupReference that = (ItemLookupReference) other;
				return (this.hashCache == that.hashCache) && (this.item == that.item)
						&& (this.metaData == that.metaData);
			} else if (other instanceof ItemStack) {
				final ItemStack that = (ItemStack) other;
				return (this.item == that.getItem()) && (this.metaData == that.getMetadata());
			} else
				return false;
		}

		@Override
		public int hashCode() {
			return this.hashCache;
		}
	}
}
