package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
//import com.mcmoddev.lib.util.Oredicts;
import com.mcmoddev.lib.util.ConfigBase.Options;

import net.minecraft.item.Item;
//import ic2.api.recipe.RecipeInputOreDict;
//import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class IC2Base implements IIntegration {

	public static final String PLUGIN_MODID = "ic2";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
	}

	protected void registerVanillaRecipes(@Nonnull final String materialName) {
		registerVanillaRecipes(Materials.getMaterialByName(materialName));
	}

	protected void registerVanillaRecipes(@Nonnull final MMDMaterial material) {
//		final Item forgeHammer = new ItemStack(Item.getByNameOrId("ic2:forge_hammer"), 1).getItem();
//		final Item forgeHammer = IC2Items.getItem("forge_hammer").getItem(); // crashes
		
		final Item ingot = material.getItem(Names.INGOT);

//		if (forgeHammer != null) {
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.casing, 2), material.plate, forgeHammer));
//		}

		// Move these to main recipe loop?
		GameRegistry.addSmelting(material.getItem(Names.CRUSHED), new ItemStack(ingot, 1), 0);
		GameRegistry.addSmelting(material.getItem(Names.CRUSHED_PURIFIED), new ItemStack(ingot, 1), 0);

		// TODO: Figure out Dense Plate & Casing
	}

	protected void addMaceratorRecipes(@Nonnull final String materialName) {
		addMaceratorRecipes(Materials.getMaterialByName(materialName));
	}

	protected void addMaceratorRecipes(@Nonnull final MMDMaterial material) {
//		String oreDictName = material.getCapitalizedName();
//		Recipes.macerator.addRecipe(new RecipeInputOreDict(Oredicts.ORE + oreDictName, 0), null, false, new ItemStack(material.getItem(Names.CRUSHED), 2));
//		Recipes.macerator.addRecipe(new RecipeInputOreDict(Oredicts.PLATE_DENSE + oreDictName, 0), null, false, new ItemStack(material.getItem(Names.POWDER), 8));
	}
}
