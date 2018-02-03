package com.mcmoddev.lib.crafttweaker;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

@ZenClass("mods.mmdlib.CrusherRecipes")
@ZenRegister
public class CrusherRecipes {
	private static void removeMatchFull(ItemStack input, ItemStack output) {
		CrusherRecipeRegistry.getAll().stream()
		.filter(rec -> rec.getInputs().contains(input))
		.filter(rec -> rec.getOutput().equals(output))
		.forEach(rec -> CrusherRecipeRegistry.removeByName(rec.getRegistryName()));
	}
	
	private static void removeMatch(ItemStack input) {
		CrusherRecipeRegistry.removeByInput(input);
	}
	
	@ZenMethod
	public static void remove(IItemStack input, @Optional IItemStack output) {
		if (output != null) {
			removeMatchFull(toStack(input), toStack(output));
		} else {
			removeMatch(toStack(input));
		}
	}
	
	@ZenMethod
	public static void add(IItemStack output, IItemStack input) {
		CrusherRecipeRegistry.addNewCrusherRecipe(toStack(input), toStack(output));
	}
}
