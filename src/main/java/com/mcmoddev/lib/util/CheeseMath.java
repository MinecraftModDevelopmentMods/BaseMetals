package com.mcmoddev.lib.util;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import com.google.common.collect.ImmutableList;
import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.data.SharedStrings;
import com.mcmoddev.lib.material.MMDMaterial;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;


public class CheeseMath {

	private CheeseMath() {
		throw new IllegalAccessError(SharedStrings.NOT_INSTANTIABLE);
	}

	private static IRecipe getRecipeFor(@Nonnull ItemStack outputItem) {
		final IRecipe target = GameRegistry.findRegistry(IRecipe.class).getValuesCollection().stream()
		.filter(rec ->	rec.getRecipeOutput().getItem().equals(outputItem.getItem())
				&& (rec.getRecipeOutput().getMetadata() == OreDictionary.WILDCARD_VALUE
				|| rec.getRecipeOutput().getMetadata() == outputItem.getMetadata()))
		.findFirst().orElse(null);
		return target;
	}
	
	private static NonNullList<Ingredient> getRecipeIngredients(@Nonnull ItemStack outputItem) {
		IRecipe target = getRecipeFor(outputItem);
		
		return target == null ? NonNullList.<Ingredient>create() : target.getIngredients();
	}
	
	private static ItemStack getItemStackFromList(List<ItemStack> isl, ItemStack target) {
		return isl.stream()
		.filter(is -> is.getItem().equals(target.getItem()) && is.getMetadata() == target.getMetadata())
		.findFirst().orElse(ItemStack.EMPTY);
	}
	
	private static int getOutputCount(@Nonnull ItemStack outputItem) {
		IRecipe t = getRecipeFor(outputItem);
		if (t == null) {
			return 1;
		} else {
			return t.getRecipeOutput().getCount();
		}
	}
	
	protected static List<ItemStack> getIngredients(@Nonnull ItemStack outputItem) {
		List<ItemStack> ingredients = new ArrayList<>();
		
		NonNullList<Ingredient> ing = getRecipeIngredients(outputItem);
		if (!ing.isEmpty()) {
			ing.stream()
			.map(i -> i.getMatchingStacks())
			.forEach(isa -> {
				for (ItemStack is : isa) {
					ItemStack t = getItemStackFromList(ingredients, is);
					if (t.isEmpty()) {
						t = is.copy();
						t.setCount(1);
						ingredients.add(t);
					} else {
						ingredients.get(ingredients.indexOf(t)).setCount(t.getCount() + 1);
					}
				}
			});
		}
		
		return ImmutableList.copyOf(ingredients);
	}
	
	private static boolean itemStackMatches(ItemStack left, ItemStack right) {
		return left.getItem().equals(right.getItem())
				&& ((left.getMetadata() == OreDictionary.WILDCARD_VALUE
				|| right.getMetadata() == OreDictionary.WILDCARD_VALUE)
				|| (left.getMetadata() == right.getMetadata()));
	}
	
	private static boolean materialHasItem(MMDMaterial material, ItemStack match) {
		return material.getItems().stream()
		.map(x -> itemStackMatches(x, match))
		.distinct()
		.collect(Collectors.toList())
		.contains(true);
	}
	
	private static int rawNuggetCount(@Nonnull MMDMaterial material, @Nonnull ItemStack outputItem) {
		return getIngredients(outputItem).stream()
		.mapToInt(itemStack -> {
			if (itemStackMatches(itemStack, material.getItemStack(Names.INGOT))) {
				return itemStack.getCount() * 9;
			} else if (itemStackMatches(itemStack, material.getItemStack(Names.NUGGET))) {
				return itemStack.getCount();
			} else if (materialHasItem(material, itemStack)) {
				return getNuggetCount(material,itemStack) * itemStack.getCount();
			}
			return 0;
		})
		.sum();
	}

	/**
	 * 
	 * @param material
	 * @param outputItem
	 * @return
	 */
	public static int getIngotCount(@Nonnull MMDMaterial material, @Nonnull ItemStack outputItem) {
		if (outputItem.isEmpty()) {
			return 0;
		}
		return (rawNuggetCount(material, outputItem) / 9) / getOutputCount(outputItem);
	}

	/**
	 * 
	 * @param material
	 * @param outputItem
	 * @return
	 */
	public static int getNuggetCount(@Nonnull MMDMaterial material, @Nonnull ItemStack outputItem) {
		if (outputItem.isEmpty()) {
			return 0;
		}
		return rawNuggetCount(material,outputItem) / getOutputCount(outputItem);
	}
}
