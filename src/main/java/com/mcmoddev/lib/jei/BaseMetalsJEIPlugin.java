package com.mcmoddev.lib.jei;

import java.util.stream.Collectors;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;

/**
 *
 * @author Jasmine Iwanek
 * @author Daniel Hazelton
 * 
 */
@JEIPlugin
public class BaseMetalsJEIPlugin extends BlankModPlugin {

	public static final String JEI_UID = BaseMetals.MODID;
	public static final String RECIPE_UID = JEI_UID + ".crackhammer";

	@Override
	public void register(IModRegistry registry) {
		final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

		registry.addRecipeCategories(new ICrusherRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new CrusherRecipeHandler());

		registry.addRecipes(CrusherRecipeRegistry.getInstance().getAllRecipes().stream().map((ICrusherRecipe in) -> new ICrusherRecipeWrapper(in)).collect(Collectors.toList()));

	}
}
