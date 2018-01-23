package com.mcmoddev.lib.jei;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ICrusherRecipeWrapper extends BlankRecipeWrapper {

	private ICrusherRecipe theRecipe;

	public ICrusherRecipeWrapper(ICrusherRecipe recipe) {
		this.theRecipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, theRecipe.getInputs());
		ingredients.setOutput(ItemStack.class, theRecipe.getOutput());
	}
}
