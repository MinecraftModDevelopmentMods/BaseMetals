package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.data.Names;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.util.Oredicts;

//import ic2.api.recipe.RecipeInputOreDict;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;

@SuppressWarnings("deprecation")
public class IC2Base implements IIntegration {

	public static final String PLUGIN_MODID = "IC2";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.modEnabled("ic2")) {
			return;
		}

		initDone = true;
	}

	protected void registerVanillaRecipes(MMDMaterial material) {
//		final Item forgeHammer = new ItemStack(Item.getByNameOrId("ic2:forge_hammer"), 1).getItem();
//		final Item forgeHammer = IC2Items.getItem("forge_hammer").getItem(); // crashes

//		if (forgeHammer != null) {
//			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(material.casing, 2), material.plate, forgeHammer));
//		}

		// Move these to main recipe loop?
		GameRegistry.addSmelting(material.getItem(Names.CRUSHED), new ItemStack(material.getItem(Names.INGOT), 1), 0);
		GameRegistry.addSmelting(material.getItem(Names.CRUSHEDPURIFIED), new ItemStack(material.getItem(Names.INGOT), 1), 0);

		// TODO: Figure out Dense Plate & Casing
	}

	@SuppressWarnings("deprecation")
	protected void addMaceratorRecipes(MMDMaterial material) {
		String oreDictName = material.getCapitalizedName();
//		Recipes.macerator.addRecipe(new RecipeInputOreDict(Oredicts.ORE + oreDictName, 0), null, false, new ItemStack(material.getItem(Names.CRUSHED), 2));
//		Recipes.macerator.addRecipe(new RecipeInputOreDict(Oredicts.PLATE_DENSE + oreDictName, 0), null, false, new ItemStack(material.getItem(Names.POWDER), 8));
	}
}
