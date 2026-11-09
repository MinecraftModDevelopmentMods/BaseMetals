package com.mcmoddev.lib.jei;

import zone.moddev.mc.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;

/**
 *
 * @author Jasmine Iwanek
 * @author Daniel Hazelton
 *
 */
@JEIPlugin
public final class BaseMetalsJEIPlugin extends BlankModPlugin {

	public static final String JEI_UID = BaseMetals.MODID;
	public static final String RECIPE_UID = JEI_UID + ".crackhammer";

	@Override
	public void registerCategories(final IRecipeCategoryRegistration registry) {
		final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

		registry.addRecipeCategories(new ICrusherRecipeCategory(guiHelper));
	}

	@Override
	public void register(final IModRegistry registry) {
		registry.handleRecipes(ICrusherRecipe.class, ICrusherRecipeWrapper::new, RECIPE_UID);
		registry.addRecipes(CrusherRecipeRegistry.getInstance().getAllRecipes(), RECIPE_UID);
	}
}
