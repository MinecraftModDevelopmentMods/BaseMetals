package com.mcmoddev.lib.crafttweaker;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mmdlib.CrusherRecipes")
@ZenRegister
public class CrusherRecipes {

	private CrusherRecipes() {
		// private constructor
	}

	private static void removeMatchFull(final ItemStack input, final ItemStack output) {
		CrusherRecipeRegistry.getAll().stream().filter(rec -> rec.getInputs().contains(input))
				.filter(rec -> rec.getOutput().equals(output))
				.forEach(rec -> CrusherRecipeRegistry.removeByName(rec.getRegistryName()));
	}

	private static void removeMatch(final ItemStack input) {
		CrusherRecipeRegistry.removeByInput(input);
	}

	/**
	 *
	 * @param input
	 * @param output
	 */
	@ZenMethod
	public static void remove(final IItemStack input, @Optional final IItemStack output) {
		if (output != null) {
			removeMatchFull(toStack(input), toStack(output));
		} else {
			removeMatch(toStack(input));
		}
	}

	@ZenMethod
	public static void add(final IItemStack output, final IItemStack input) {
		CrusherRecipeRegistry.addNewCrusherRecipe(toStack(input), toStack(output));
	}
}
