package com.mcmoddev.lib.jei;

import java.util.stream.Collectors;

import com.mcmoddev.basemetals.BaseMetals;
import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.BlankModPlugin;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.IJeiHelpers;
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

	public static final String JEIUID = BaseMetals.MODID;

	@Override
	public void register(IModRegistry registry) {

		IJeiHelpers jeiHelpers = registry.getJeiHelpers();
		IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

		registry.addRecipeCategories(new CrusherRecipeCategory(guiHelper));
		registry.addRecipeHandlers(new CrusherRecipeHandler());

		registry.addRecipes(CrusherRecipeRegistry.getInstance().getAllRecipes().stream().map((ICrusherRecipe in) -> new CrusherRecipeWrapper(in)).collect(Collectors.toList()));

	}
}
