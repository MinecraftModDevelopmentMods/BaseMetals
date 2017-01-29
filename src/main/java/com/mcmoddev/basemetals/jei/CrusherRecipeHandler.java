package com.mcmoddev.basemetals.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

/**
 *
 * @author DrCyano
 *
 */
public class CrusherRecipeHandler implements IRecipeHandler<CrusherRecipeJEI> {

	private final String id = BaseMetalsJEIPlugin.JEIUID;

	@Nonnull
	@Override
	public String getRecipeCategoryUid(@Nonnull CrusherRecipeJEI recipe) {
		return this.id;
	}

	@Override
	public String getRecipeCategoryUid() {
		return this.id;
	}

	@Override
	public Class<CrusherRecipeJEI> getRecipeClass() {
		return CrusherRecipeJEI.class;
	}

	@Override
	public IRecipeWrapper getRecipeWrapper(CrusherRecipeJEI recipe) {
		return recipe;
	}

	@Override
	public boolean isRecipeValid(CrusherRecipeJEI recipe) {
		return !(recipe.getInputs().isEmpty() || recipe.getOutputs().isEmpty());
	}
}
