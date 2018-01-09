package cyano.basemetals.jei;

import javax.annotation.Nonnull;

import mezz.jei.api.recipe.IRecipeHandler;
import mezz.jei.api.recipe.IRecipeWrapper;

public class CrusherRecipeHandler implements IRecipeHandler<CrusherRecipeJEI>{

	private final String id = BaseMetalsJEIPlugin.JEI_UID;
	
	@Override
	public Class<CrusherRecipeJEI> getRecipeClass() {
		return CrusherRecipeJEI.class;
	}

	@Override
	public String getRecipeCategoryUid() {
		return BaseMetalsJEIPlugin.JEI_UID;
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
