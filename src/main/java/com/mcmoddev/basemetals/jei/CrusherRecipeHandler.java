package com.mcmoddev.basemetals.jei;

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

	private static final String ID = BaseMetalsJEIPlugin.JEIUID;

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull CrusherRecipeWrapper recipe) {
		return ID+".crackhammer";
	}

	@Override
	public String getRecipeCategoryUid() {
		return ID;
	}

	@Override
	public Class<CrusherRecipeWrapper> getRecipeClass() {
		return CrusherRecipeWrapper.class;
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
