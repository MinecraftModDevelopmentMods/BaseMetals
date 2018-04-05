package com.mcmoddev.lib.jei;

import java.util.Collections;
import java.util.List;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ICrusherRecipeWrapper implements IRecipeWrapper {

	private final ICrusherRecipe theRecipe;

	public ICrusherRecipeWrapper(final ICrusherRecipe recipe) {
		this.theRecipe = recipe;
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, this.theRecipe.getInputs());
		ingredients.setOutput(ItemStack.class, this.theRecipe.getOutput());
	}

	@Override
	public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
		return Collections.emptyList();
	}
}
