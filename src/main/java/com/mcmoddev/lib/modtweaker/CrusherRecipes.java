package com.mcmoddev.lib.modtweaker;

import com.mcmoddev.lib.registry.CrusherRecipeRegistry;

import crafttweaker.CraftTweakerAPI;
import crafttweaker.annotations.ModOnly;
import crafttweaker.annotations.ZenRegister;
import crafttweaker.api.item.IIngredient;
import crafttweaker.api.item.IItemStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import stanhebben.zenscript.annotations.Optional;
import stanhebben.zenscript.annotations.ZenClass;
import stanhebben.zenscript.annotations.ZenMethod;

import static com.blamejared.mtlib.helpers.InputHelper.toStack;

import java.util.List;
import java.util.stream.Collectors;

@ZenClass("mods.mmdlib.CrusherRecipes")
@ModOnly("basemetals")
@ZenRegister
public class CrusherRecipes {
	
	@ZenMethod
	public static void announce() {
		CraftTweakerAPI.logInfo("BaseMetals CraftTweaker Integration Loaded");
	}
	
	@ZenMethod
	public static void remove(IItemStack output, @Optional IIngredient input ) {
		CraftTweakerAPI.logInfo("In CrusherRecipes.remove");
		IIngredient in = input;
		if( in != null ) {
			in.getItems().stream().forEach( thisItem -> {
				ItemStack it = toStack(thisItem);
				CraftTweakerAPI.logInfo( String.format( "found item %s (stack %s), removing", thisItem, it));
				CrusherRecipeRegistry.removeByInput(it);
			});
		} else {
			ItemStack res = toStack(output);
			List<ResourceLocation> collated = CrusherRecipeRegistry.getAll().stream()
			.filter( recipe -> recipe.getOutput().equals(res) || recipe.getOutput().isItemEqual(res) )
			.map(recipe -> recipe.getRegistryName() )
			.collect(Collectors.toList());
			
			collated.forEach(CrusherRecipeRegistry::removeByName);
		}
	}
	
	@ZenMethod
	public static void add(IItemStack output, IItemStack input ) {
		CraftTweakerAPI.logInfo("In CrusherRecipes.add");
		CraftTweakerAPI.logInfo( String.format( "adding for %s out from %s ( %s/%s )", output, input, toStack(output), toStack(input)));
		CrusherRecipeRegistry.addNewCrusherRecipe(toStack(input), toStack(output));
	}
}
