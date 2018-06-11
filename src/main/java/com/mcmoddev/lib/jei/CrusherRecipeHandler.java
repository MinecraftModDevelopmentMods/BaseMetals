package com.mcmoddev.lib.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 *
 * @author DrCyano
 * @author Daniel Hazelton
 *
 */
public class CrusherRecipeHandler implements IRecipeHandler<ICrusherRecipeWrapper> {

	@Override
	public Class<ICrusherRecipeWrapper> getRecipeClass() {
		return ICrusherRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull final ICrusherRecipeWrapper recipe) {
		return BaseMetalsJEIPlugin.RECIPE_UID;
	}

	@Override
	public String getRecipeCategoryUid() {
		return BaseMetalsJEIPlugin.JEI_UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(final ICrusherRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(final ICrusherRecipeWrapper recipe) {
		return true;
	}
}
