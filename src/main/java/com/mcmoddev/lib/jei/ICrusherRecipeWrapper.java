package com.mcmoddev.lib.jei;

import java.util.Collections;
import java.util.List;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;

public class ICrusherRecipeWrapper extends BlankRecipeWrapper {

	private ICrusherRecipe theRecipe;
	
	public ICrusherRecipeWrapper(ICrusherRecipe recipe) {
		this.theRecipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputs(ItemStack.class, theRecipe.getInputs());
		ingredients.setOutput(ItemStack.class, theRecipe.getOutput());
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		// this used to add
		// add the recipes type at the top of the screen
		// minecraft.fontRenderer.drawString(I18n.format("basemetals.description.crackhammer"),43, 0, 0);
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return Collections.emptyList();
	}
}
