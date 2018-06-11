package com.mcmoddev.lib.jei;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ICrusherRecipeWrapper extends BlankRecipeWrapper {

	private final ICrusherRecipe theRecipe;

	public ICrusherRecipeWrapper(final ICrusherRecipe recipe) {
		this.theRecipe = recipe;
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		// TODO: Verify Inputs
		ingredients.setInputs(ItemStack.class, this.theRecipe.getInputs());
		ingredients.setOutput(ItemStack.class, this.theRecipe.getOutput());
	}
}
