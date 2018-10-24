package com.mcmoddev.lib.crafttweaker;

import javax.annotation.Nullable;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;
import com.mcmoddev.lib.registry.recipe.ICrusherRecipe;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class CrusherRecipeRemoveAction implements IAction {
	private final ItemStack output;
	private final ItemStack input;

	public CrusherRecipeRemoveAction(ItemStack input, @Nullable ItemStack output) {
		this.input = input;
		this.output = output;
	}

	@Override
	public void apply() {
		ICrusherRecipe recipe = CrusherRecipeRegistry.getRecipeForInputItem(this.input);
		if (recipe != null) {
			if ((this.output != ItemStack.EMPTY && 
					OreDictionary.itemMatches(recipe.getOutput(), this.output, false) && 
					recipe.getOutput().getCount() == this.output.getCount())
					|| this.output == ItemStack.EMPTY) {
				CrusherRecipeRegistry.removeByName(recipe.getRegistryName());
			} else {
				String mess = String.format("Recipe with input %s(*%d)", this.input.getDisplayName(), this.input.getCount());
				if (this.output != ItemStack.EMPTY) {
					mess += String.format(" and output of %s(*%d)", this.output.getDisplayName(), this.output.getCount());
				}
				CraftTweakerAPI.logInfo(String.format( "%s not found, ignoring", mess));
			}
		} else {
			String mess = String.format("Recipe with input %s(*%d)", this.input.getDisplayName(), this.input.getCount());
			if (this.output != null) {
				mess += String.format(" and output of %s(*%d)", this.output.getDisplayName(), this.output.getCount());
			}
			CraftTweakerAPI.logInfo(String.format( "%s not found, ignoring", mess));
		}
	}

	@Override
	public String describe() {
		return String.format("Recipe for %s(*%d) has been removed", this.input.getDisplayName(), this.input.getCount());
	}

}
