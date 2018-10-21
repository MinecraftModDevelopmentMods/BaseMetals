package com.mcmoddev.lib.crafttweaker;

import java.util.List;
import java.util.stream.Collectors;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.minecraft.CraftTweakerMC;

import net.minecraft.item.ItemStack;

public class CrusherRecipeAddAction implements IAction {
	private final IIngredient input;
	private final ItemStack output;
	
	public CrusherRecipeAddAction(IIngredient input, ItemStack output) {
		this.input = input;
		this.output = output;
	}
	
	@Override
	public void apply() {
        List<ItemStack> validIngredients = this.input.getItems().stream().map(CraftTweakerMC::getItemStack).collect(Collectors.toList());
        if(validIngredients.isEmpty()) {
            CraftTweakerAPI.logInfo("Could not find matching items for " + this.input.toString() + ". Ignoring recipe for " + output.getDisplayName());
        } else {
        	validIngredients.stream().forEach(itemIn -> CrusherRecipeRegistry.addNewCrusherRecipe(itemIn, output));
        }
	}

	@Override
	public String describe() {
		return String.format("Recipe for %s has been added", this.output.getDisplayName());
	}
}
