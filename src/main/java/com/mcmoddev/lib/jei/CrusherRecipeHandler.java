package com.mcmoddev.lib.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

/**
 *
 * @author DrCyano
 * @author Daniel Hazelton
 *
 */
public class CrusherRecipeHandler implements IRecipeHandler<ICrusherRecipe> {

	@Override
	public Class<ICrusherRecipe> getRecipeClass() {
		return ICrusherRecipe.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull ICrusherRecipe recipe) {
		return BaseMetalsJEIPlugin.JEIUID + ".crackhammer";
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(ICrusherRecipe recipe) {
		return new CrusherRecipeWrapper(recipe);
	}

	@Override
	public boolean isRecipeValid(ICrusherRecipe recipe) {
		return true;
	}
}
