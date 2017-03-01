package com.mcmoddev.lib.jei;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import mezz.jei.api.gui.ITooltipCallback;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.item.ItemStack;


@SuppressWarnings("rawtypes")
public class CrusherRecipeWrapper implements ITooltipCallback<ItemStack>, IRecipeWrapper {

	private ICrusherRecipe theRecipe;

	public CrusherRecipeWrapper(ICrusherRecipe recipe) {
		theRecipe = recipe;
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		// TODO Auto-generated method stub

	}

	@Override
	public List getInputs() {
		return new ArrayList<>(theRecipe.getValidInputs());
	}

	@Override
	public List getOutputs() {
		List<ItemStack> rv = new ArrayList<>();
		rv.add(theRecipe.getOutput());
		return rv;
	}

	@Override
	public List<FluidStack> getFluidInputs() {
		return new ArrayList<>();
	}

	@Override
	public List<FluidStack> getFluidOutputs() {
		return new ArrayList<>();
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		// TODO Auto-generated method stub

	}

	@Override
	public void drawAnimations(Minecraft minecraft, int recipeWidth, int recipeHeight) {
		// TODO Auto-generated method stub
	}

	@Override
	public List<String> getTooltipStrings(int mouseX, int mouseY) {
		return new ArrayList<>();
	}

	@Override
	public boolean handleClick(Minecraft minecraft, int mouseX, int mouseY, int mouseButton) {
		return false;
	}

	@Override
	public void onTooltip(int slotIndex, boolean input, @Nonnull ItemStack ingredient, @Nonnull List<String> tooltip) {
		// TODO Auto-generated method stub
	}
}
