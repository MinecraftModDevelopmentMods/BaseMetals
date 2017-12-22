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
public class CrusherRecipeHandler implements IRecipeHandler<CrusherRecipeWrapper> {

	@Override
	public Class<CrusherRecipeWrapper> getRecipeClass() {
		return CrusherRecipeWrapper.class;
	}

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull CrusherRecipeWrapper recipe) {
		return BaseMetalsJEIPlugin.RECIPE_UID;
	}

	@Override
	public String getRecipeCategoryUid() {
		return BaseMetalsJEIPlugin.JEI_UID;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CrusherRecipeWrapper recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(CrusherRecipeWrapper recipe) {
		return !(recipe.getInputs().isEmpty() || recipe.getOutputs().isEmpty());
	}
}
