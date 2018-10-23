package com.mcmoddev.lib.crafttweaker;

import javax.annotation.Nullable;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import crafttweaker.IAction;

import net.minecraft.item.ItemStack;

public class CrusherRecipeAddAction implements IAction {
	private final ItemStack input;
	private final ItemStack output;
	
	public CrusherRecipeAddAction(ItemStack input, @Nullable ItemStack output) {
		this.input = input;
		this.output = output;
	}
	
	@Override
	public void apply() {
   		CrusherRecipeRegistry.addNewCrusherRecipe(this.input, this.output);
	}

	@Override
	public String describe() {
		return String.format("Recipe for %s(*%d) from %s(*%d) has been added", this.output.getDisplayName(), this.output.getCount(),
				this.input.getDisplayName(), this.input.getCount());
	}
}
