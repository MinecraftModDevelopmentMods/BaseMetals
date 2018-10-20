package com.mcmoddev.lib.registry.recipe;

import net.minecraft.item.ItemStack;

public interface ICrusherNBTRecipe {
	public ItemStack getOutput(ItemStack input);
}
