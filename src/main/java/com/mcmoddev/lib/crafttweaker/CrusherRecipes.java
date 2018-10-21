package com.mcmoddev.lib.crafttweaker;

import java.util.LinkedList;
import java.util.List;

import javax.annotation.Nullable;

import com.mcmoddev.basemetals.BaseMetals;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.IAction;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IItemStack;
import crafttweaker.api.minecraft.CraftTweakerMC;
import crafttweaker.api.oredict.IOreDictEntry;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

@ZenClass("mods.mmdlib.CrusherRecipes")
@ZenRegister
public class CrusherRecipes {
	private static final List<IAction> actions = new LinkedList<>();
	
	private CrusherRecipes() {
		// private constructor
	}

	@ZenMethod
	public static void remove(final IOreDictEntry input, @Optional @Nullable final IItemStack output) {
		input.getItems().stream().forEach( it -> remove(it, output) );
	}
	
	@ZenMethod
	public static void remove(final IItemStack input, @Optional @Nullable final IItemStack output) {
		if (output != null) {
			actions.add(new CrusherRecipeRemoveAction(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output)));
		} else {
			actions.add(new CrusherRecipeRemoveAction(CraftTweakerMC.getItemStack(input), null));
		}
	}

	@ZenMethod
	public static void add(final IItemStack output, final IOreDictEntry input) {
		input.getItems().stream().forEach( it -> add(it, output) );
	}
	
	@ZenMethod
	public static void add(final IItemStack output, final IItemStack input) {
		CraftTweakerAPI.logDefault(String.format("Adding recipe with input %s and output %s", input.getDisplayName(), output.getDisplayName()));
		actions.add(new CrusherRecipeAddAction(CraftTweakerMC.getItemStack(input), CraftTweakerMC.getItemStack(output)));
	}
	
	public static void loadComplete() {
		BaseMetals.logger.fatal("BLARGH!!! loadComplete!");
		actions.stream().forEach( act -> {
			BaseMetals.logger.fatal("ACTION: %s (%s)", act.describe(), act.toString());
			CraftTweakerAPI.apply(act);
		});
	}
}
