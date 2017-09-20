package com.mcmoddev.lib.registry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.annotation.Nonnull;

import com.google.common.collect.Lists;
import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.registry.recipe.ArbitraryCrusherRecipe;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.registry.recipe.OreDictionaryCrusherRecipe;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryModifiable;
import net.minecraftforge.registries.RegistryBuilder;

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
	private final IForgeRegistryModifiable<ICrusherRecipe> REGISTRY;
	private final static CrusherRecipeRegistry instance = new CrusherRecipeRegistry();
	
	private CrusherRecipeRegistry() {
		this.REGISTRY = (IForgeRegistryModifiable<ICrusherRecipe>)new RegistryBuilder<ICrusherRecipe>()
				.setName(new ResourceLocation("mmdlib","crusher_registry"))
			    .allowModification()
			    .setType(ICrusherRecipe.class)
			    .setMaxID(Integer.MAX_VALUE >> 4)
			    .create();
	}

	public static void addNewCrusherRecipe(@Nonnull final String oreDict, @Nonnull final ItemStack output) {
		instance.REGISTRY.register( new OreDictionaryCrusherRecipe(oreDict, output));
	}

	public static void addNewCrusherRecipe(@Nonnull final Block input, @Nonnull final ItemStack output) {
		instance.REGISTRY.register( new ArbitraryCrusherRecipe(input, output));
	}

	public static void addNewCrusherRecipe(@Nonnull final ItemStack input, @Nonnull final ItemStack output) {
		instance.REGISTRY.register( new ArbitraryCrusherRecipe(input, output));
	}

	public static void addNewCrusherRecipe(@Nonnull final Item input, @Nonnull final ItemStack output) {
		instance.REGISTRY.register( new ArbitraryCrusherRecipe(input, output));
	}

	public static void register(@Nonnull final ICrusherRecipe recipe) {
		instance.REGISTRY.register(recipe);
	}
	
	public static List<ICrusherRecipe> getAll() {
		return Lists.newArrayList(instance.REGISTRY.iterator());
	}
	
	public static CrusherRecipeRegistry getInstance() {
		return instance;
	}
	
	public static ICrusherRecipe getRecipeForInputItem(@Nonnull final ItemStack itemStack) {
		Iterator<ICrusherRecipe> iter = instance.REGISTRY.iterator();
		
		while( iter.hasNext() ) {
			ICrusherRecipe cur = iter.next();
			if( cur.isValidInput(itemStack) ) {
				return cur;
			}
		}
		
		return null;
	}
	
	public void clearCache() {
		// we do nothing - back-compat
	}
}
