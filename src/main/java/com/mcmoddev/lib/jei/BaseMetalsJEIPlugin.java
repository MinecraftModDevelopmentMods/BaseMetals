package com.mcmoddev.lib.jei;

import java.util.stream.Collectors;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.recipe.IRecipeCategoryRegistration;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IRecipeWrapperFactory;

/**
 *
 * @author Jasmine Iwanek
 * @author Daniel Hazelton
 * 
 */
@JEIPlugin
public class BaseMetalsJEIPlugin implements IModPlugin {

	public static final String JEI_UID = BaseMetals.MODID;
	public static final String RECIPE_UID = JEI_UID + ".crackhammer";

	@Override
	public void registerCategories(IRecipeCategoryRegistration registry) {
		final IGuiHelper guiHelper = registry.getJeiHelpers().getGuiHelper();

		registry.addRecipeCategories(new ICrusherRecipeCategory(guiHelper));
	}


	@Override
	public void register(IModRegistry registry) {
		registry.addRecipes(CrusherRecipeRegistry.getAll().stream().map((ICrusherRecipe in) -> new ICrusherRecipeWrapper(in)).collect(Collectors.toList()), RECIPE_UID);

		registry.handleRecipes(ICrusherRecipe.class, new IRecipeWrapperFactory<ICrusherRecipe>() {
			@Override
			public IRecipeWrapper getRecipeWrapper(ICrusherRecipe recipe) {
				return new ICrusherRecipeWrapper(recipe);
			}
		}, RECIPE_UID);
	}
}
