package com.mcmoddev.lib.crafttweaker;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CrusherRecipeRemoveAction implements IAction {
	private final ItemStack output;
	private final IIngredient input;

	private List<ICrusherRecipe> toRemove;
	public CrusherRecipeRemoveAction(IIngredient input, ItemStack output) {
		this.input = input;
		this.output = output;
		this.toRemove = new ArrayList<>();
	}

	@Override
	public void apply() {
		getAllMatching(input,output);
		this.toRemove.stream().forEach( icr -> CrusherRecipeRegistry.removeByName(icr.getRegistryName()));
	}

	private void getAllMatching(IIngredient ingIn, ItemStack itemOut) {
		this.toRemove.addAll(ingIn.getItems().stream().map(CraftTweakerMC::getItemStack)
				.filter( ing -> CrusherRecipeRegistry.getRecipeForInputItem(ing) != null)
				.map( ing -> CrusherRecipeRegistry.getRecipeForInputItem(ing) )
				.filter( ing -> (itemOut != null && OreDictionary.itemMatches(ing.getOutput(), itemOut, false)) || (itemOut == null) )
				.collect(Collectors.toList()));
	}

	@Override
	public String describe() {
		return String.format("Recipe for %s has been removed", this.output.getDisplayName());
	}

}
