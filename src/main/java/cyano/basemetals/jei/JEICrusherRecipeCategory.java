package cyano.basemetals.jei;

import cyano.basemetals.BaseMetals;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

public class JEICrusherRecipeCategory extends BlankRecipeCategory {

	private final ResourceLocation bgtex = new ResourceLocation(BaseMetals.MODID+":textures/gui/nei/nei_crusher.png");
	private final IDrawable background;
	private final String id = BaseMetalsJEIPlugin.JEIUID;

	private static final int inputSlot = 0;
	private static final int outputSlot = 1;

	public JEICrusherRecipeCategory(IGuiHelper guiHelper){
		background = guiHelper.createDrawable(bgtex, 24,26, 128,32);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public String getTitle() {
		String key = "nei."+BaseMetals.MODID+".recipehandler.crusher.name";
		if(StatCollector.canTranslate(key)){
			return StatCollector.translateToLocal(key); 
		} else {
			return "Crusher";
		}
	}

	@Override
	public String getUid() {
		return id;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		int x = 45, y = 7;
		guiItemStacks.init(inputSlot, true, x, y);
		guiItemStacks.init(outputSlot, false, x+58, y);

		guiItemStacks.setFromRecipe(inputSlot, recipeWrapper.getInputs());
		guiItemStacks.setFromRecipe(outputSlot, recipeWrapper.getOutputs());
	}
}
