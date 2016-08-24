package cyano.basemetals.jei;

import mezz.jei.api.recipe.*;

import javax.annotation.Nonnull;

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
		return id;
	}

	@Override
	public String getRecipeCategoryUid() {
		return id;
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
