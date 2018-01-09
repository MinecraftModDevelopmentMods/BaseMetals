package cyano.basemetals.jei;

import java.util.Arrays;
import java.util.stream.Collectors;

import cyano.basemetals.BaseMetals;
import cyano.basemetals.registry.CrusherRecipeRegistry;
import cyano.basemetals.registry.recipe.ICrusherRecipe;
import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class BaseMetalsJEIPlugin extends BlankModPlugin{

	public static final String JEI_UID = BaseMetals.MODID;
	public static final String RECIPE_UID = JEI_UID + ".crackhammer";

	@Override
	public void register(IModRegistry registry) {

		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();
		
		registry.addRecipeCategories(new JEICrusherRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new CrusherRecipeHandler());
		
		registry.addRecipes(CrusherRecipeRegistry.getInstance().getAllRecipes().stream()
				.map((ICrusherRecipe in)->new CrusherRecipeJEI(Arrays.asList(in.getValidInputs().toArray(new ItemStack[0])), in.getOutput()))
				.collect(Collectors.toList()));
	}
}
