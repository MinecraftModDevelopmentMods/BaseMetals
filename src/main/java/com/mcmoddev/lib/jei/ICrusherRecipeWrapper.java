package com.mcmoddev.lib.jei;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.registry.recipe.NBTCrusherRecipe;
import com.mcmoddev.lib.registry.recipe.NBTOreDictRecipe;

import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.item.ItemStack;

public class ICrusherRecipeWrapper implements IRecipeWrapper {

	private final ICrusherRecipe theRecipe;

	public ICrusherRecipeWrapper(final ICrusherRecipe recipe) {
		this.theRecipe = recipe;
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		if (this.theRecipe instanceof NBTCrusherRecipe || this.theRecipe instanceof NBTOreDictRecipe) {
			ingredients.setInputs(VanillaTypes.ITEM, (List<ItemStack>)this.theRecipe.getValidInputs());
		} else {
			ingredients.setInputs(VanillaTypes.ITEM, this.theRecipe.getInputs());
		}
		
		if (this.theRecipe instanceof NBTCrusherRecipe) {
			ingredients.setOutputs(VanillaTypes.ITEM,  this.theRecipe.getValidInputs().stream()
					.map( ing -> ((NBTCrusherRecipe)this.theRecipe).getOutput(ing)).collect(Collectors.toList()));
		} else if (this.theRecipe instanceof NBTOreDictRecipe) {
			ingredients.setOutputs(VanillaTypes.ITEM,  this.theRecipe.getValidInputs().stream()
					.map( ing -> ((NBTOreDictRecipe)this.theRecipe).getOutput(ing)).collect(Collectors.toList()));
		} else {
			ingredients.setOutput(VanillaTypes.ITEM, this.theRecipe.getOutput());
		}
	}

	@Override
	public List<String> getTooltipStrings(final int mouseX, final int mouseY) {
		return Collections.emptyList();
	}
}
