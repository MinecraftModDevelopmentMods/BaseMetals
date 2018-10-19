package com.mcmoddev.lib.jei;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;
import com.mcmoddev.lib.registry.recipe.NBTCrusherRecipe;
import com.mcmoddev.lib.registry.recipe.NBTOreDictRecipe;

import mezz.jei.api.IJeiHelpers;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.ingredients.VanillaTypes;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.IStackHelper;
import net.minecraft.item.ItemStack;

public class ICrusherRecipeWrapper<T extends ICrusherRecipe> implements IRecipeWrapper {

	private final ICrusherRecipe theRecipe;
	private final IJeiHelpers jeiHelpers;
	
	public ICrusherRecipeWrapper(IJeiHelpers jeiHelpers, final T recipe) {
		this.theRecipe = recipe;
		this.jeiHelpers = jeiHelpers;
	}

	@Override
	public void getIngredients(final IIngredients ingredients) {
		IStackHelper stackHelper = this.jeiHelpers.getStackHelper();
		
		List<List<ItemStack>> inputLists = stackHelper.expandRecipeItemStackInputs(this.theRecipe.getInputs());
		ingredients.setInputLists(VanillaTypes.ITEM, inputLists);
		
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
