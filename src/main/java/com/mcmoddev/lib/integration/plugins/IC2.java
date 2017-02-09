package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.util.Oredicts;

import ic2.api.item.IC2Items;
import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class IC2 implements IIntegration {

	public static final String PLUGIN_MODID = "IC2";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableIC2) {
			return;
		}

		initDone = true;
	}

	protected void registerVanillaRecipes(MetalMaterial material) {
//		final Item forgeHammer = new ItemStack(Item.getByNameOrId("ic2:forge_hammer"), 1).getItem();
//		final Item forgeHammer = IC2Items.getItem("forge_hammer").getItem(); // crashes

//		if (forgeHammer != null) {
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.casing, 2), material.plate, forgeHammer));
//		}

		// Move these to main recipe loop?
		GameRegistry.addSmelting(material.crushed, new ItemStack(material.ingot, 1), 0);
		GameRegistry.addSmelting(material.crushed_purified, new ItemStack(material.ingot, 1), 0);
		
		// TODO: Figure out Dense Plate & Casing
	}

	protected void addMaceratorRecipes(MetalMaterial material) {
		String oreDictName = material.getCapitalizedName();
		Recipes.macerator.addRecipe(new RecipeInputOreDict(Oredicts.ORE + oreDictName, 0), null, false, new ItemStack(material.crushed, 2));
		Recipes.macerator.addRecipe(new RecipeInputOreDict(Oredicts.PLATEDENSE + oreDictName, 0), null, false, new ItemStack(material.powder, 8));
	}
}
