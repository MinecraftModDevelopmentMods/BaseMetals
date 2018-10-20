package com.mcmoddev.lib.jei;

import java.util.Collections;
import java.util.List;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;

public class ICrusherRecipeWrapper implements IRecipeWrapper {

	private final ICrusherRecipe theRecipe;
	private final IJeiHelpers jeiHelpers;
	
	public ICrusherRecipeWrapper(IJeiHelpers jeiHelpers, final ICrusherRecipe recipe) {
		this.theRecipe = recipe;
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		ingredients.setInputLists(VanillaTypes.ITEM, jeiHelpers.getStackHelper().expandRecipeItemStackInputs(theRecipe.getInputs()));
		
		ingredients.setOutput(VanillaTypes.ITEM, theRecipe.getOutput());
	}

	@Override
	public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
		return Collections.emptyList();
	}
}
