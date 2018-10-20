package com.mcmoddev.lib.registry.recipe;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

import com.mcmoddev.lib.registry.recipe.NBTCrusherRecipe.OutputMunger;

public class NBTOreDictRecipe extends IForgeRegistryEntry.Impl<ICrusherRecipe> implements ICrusherRecipe, ICrusherNBTRecipe {
	private final OutputMunger outputCallback;
	private final InputMunger inputCallback;
	private final Predicate<ItemStack> validItemCallback;
	private final String inputItem;
	
	public interface InputMunger {
		public List<ItemStack> run(final String input);
	}

	public NBTOreDictRecipe(final String inputOreDict, final OutputMunger nbtCallback, final InputMunger inputCallback, final Predicate<ItemStack> validItemCallback) {
		inputItem = inputOreDict;
		outputCallback = nbtCallback;
		this.inputCallback = inputCallback;
		this.validItemCallback = validItemCallback;
	}

	public NBTOreDictRecipe(final String inputOreDict, final OutputMunger nbtCallback, final InputMunger inputCallback) {
		this(inputOreDict, nbtCallback, inputCallback, (in) -> OreDictionary.containsMatch(false, OreDictionary.getOres(inputOreDict), in));
	}

	public NBTOreDictRecipe(final String inputOreDict, final OutputMunger nbtCallback, final Predicate<ItemStack> validItemCallback) {
		this(inputOreDict, nbtCallback, (in) -> OreDictionary.getOres(in), validItemCallback);
	}
	
	public NBTOreDictRecipe(final String inputOreDict, final OutputMunger nbtCallback) {
		this(inputOreDict, nbtCallback, (in) -> OreDictionary.getOres(in), (in) -> OreDictionary.containsMatch(false, OreDictionary.getOres(inputOreDict), in));
	}
	
	@Override
	public List<ItemStack> getInputs() {
		return this.inputCallback.run(inputItem);
	}

	@Override
	public ItemStack getOutput() {
		return getOutput(getInputs().get(0));
	}

	@Override
	public ItemStack getOutput(ItemStack input) {
		return this.isValidInput(input)?this.outputCallback.run(input):ItemStack.EMPTY;
	}
	
	@Override
	public boolean isValidInput(ItemStack input) {
		return this.validItemCallback.test(input);
	}

	@Override
	public Collection<ItemStack> getValidInputs() {
		return Collections.unmodifiableList(getInputs());
	}
}
