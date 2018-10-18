package com.mcmoddev.lib.registry.recipe;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IForgeRegistryEntry;

public class NBTCrusherRecipe extends IForgeRegistryEntry.Impl<ICrusherRecipe> implements ICrusherRecipe {
	private final OutputMunger outputCallback;
	private final InputMunger inputCallback;
	private final Predicate<ItemStack> validItemCallback;
	private final ItemStack inputItem;
	private final ItemStack outputItem;
	
	public interface OutputMunger {
		public ItemStack run(final ItemStack input);
	}
	
	public interface InputMunger {
		public List<ItemStack> run(final ItemStack input);
	}

	public NBTCrusherRecipe(final ItemStack input, final OutputMunger nbtCallback, final InputMunger inputCallback, final Predicate<ItemStack> validItemCallback) {
		inputItem = input;
		outputCallback = nbtCallback;
		outputItem = outputCallback.run(inputItem);
		this.inputCallback = inputCallback;
		this.validItemCallback = validItemCallback;
	}

	public NBTCrusherRecipe(final ItemStack input, final OutputMunger nbtCallback, final InputMunger inputCallback) {
		this(input, nbtCallback, inputCallback, (in) -> OreDictionary.itemMatches(input, in, false));
	}

	public NBTCrusherRecipe(final ItemStack input, final OutputMunger nbtCallback, final Predicate<ItemStack> validItemCallback) {
		this(input, nbtCallback, (in) -> Arrays.asList(in), validItemCallback);
	}
	
	public NBTCrusherRecipe(final ItemStack input, final OutputMunger nbtCallback) {
		this(input, nbtCallback, (in) -> Arrays.asList(in), (in) -> OreDictionary.itemMatches(input, in, false));
	}
	
	@Override
	public List<ItemStack> getInputs() {
		return this.inputCallback.run(inputItem);
	}

	@Override
	public ItemStack getOutput() {
		return outputItem;
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
