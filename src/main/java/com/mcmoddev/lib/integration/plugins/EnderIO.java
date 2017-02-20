package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.basemetals.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MetalMaterial;
import com.mcmoddev.lib.material.MetalMaterial.MaterialType;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EnderIO implements IIntegration {

	public static final String PLUGIN_MODID = "EnderIO";

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableEnderIO) {
			return;
		}

		initDone = true;
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(String materialName, String outputSecondary, int energy) {
		addAlloySmelterRecipe(materialName, 2, outputSecondary, energy);
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(String materialName, int primaryQty, String outputSecondary, int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		MetalMaterial material = Materials.getMaterialByName(materialName);

		// final String inputName = materialName + "_ore";
		final String inputOredict = Oredicts.ORE + material.getCapitalizedName();
		final String outputName = materialName + "_ingot";
		// final String outputOredict = Oredicts.INGOT + material.getCapitalizedName();

		if (material.hasOre == false)
			return; // Only run for Ore types

		// @formatter:off
		final String AlloySmelterMessage =
			"<recipeGroup name=\"" + ownerModID + "\" >" +
			 "<recipe name=\"" + material + "\" energyCost=\"" + energy + "\" >" +
			  "<input>" +
//			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + inputName + "\" />" +
//			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + inputName + "\" itemMeta=\"" + inputMeta + "\" />" +
			   "<itemStack oreDictionary="+ inputOredict + "\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + outputName + "\" />" +
//			   "<itemStack oreDictionary=\"" + outputOredict + "\" />" +
			   // account for number="int", exp="float" and chance="float" too
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:alloysmelter", AlloySmelterMessage);
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(String materialName, String outputSecondary, int energy) {
		MetalMaterial material = Materials.getMaterialByName(materialName);
		int primaryQty = 2;

		if (material.getType() == MaterialType.MINERAL) {
			primaryQty = 4;
		}

		addSagMillRecipe(materialName, primaryQty, outputSecondary, energy);
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param material
	 *            The Material
	 * @param primaryQty
	 *            How much to make
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(String materialName, int primaryQty, String outputSecondary, int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		MetalMaterial material = Materials.getMaterialByName(materialName);

		// final String inputName = materialName + "_ore";
		final String inputOredict = Oredicts.ORE + material.getCapitalizedName();
		// final int inputMeta = 0;

		String outputPrimary = materialName + "_powder";
		if (material.getType() == MaterialType.GEM) {
			outputPrimary = materialName + "_ingot";
		}

		// final String outputPrimaryOredict = Oredicts.DUST +
		// material.getCapitalizedName();
		// final int primaryMeta = 0;

		final String primaryChance = "1.0";

		// final String outputSecondaryOredict = Oredicts.DUST +
		// outputSecondary;
		// final int secondaryMeta = 0;
		final int secondaryQty = 1;
		final String secondaryChance = "0.1";

		final String rockModID = "minecraft";
		final String rockName = "cobblestone";
		// final int rockMeta = 0;
		final int rockQty = 1;
		final String rockChance = "0.15";

		if (material.hasOre == false)
			return; // Only run for Ore types

		// @formatter:off
		final String SAGMillMessage =
			"<recipeGroup name=\"" + ownerModID + "\" >" +
			 "<recipe name=\"" + materialName + "\" energyCost=\"" + energy + "\" >" +
			  "<input>" +
//			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + inputName + "\" />" +
//			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + inputName + "\" itemMeta=\"" + inputMeta + "\" />" +
			   "<itemStack oreDictionary="+ inputOredict + "\" />" +
			  "</input>" +
			  "<output>" +
//			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + outputPrimary + "\" itemMeta=\"" + primaryMeta + "\" number=\"" + primaryQty + "\" chance=\"" + primaryChance + "\" />" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + outputPrimary + "\" number=\"" + primaryQty + "\" chance=\"" + primaryChance + "\" />" +
//			   "<itemStack oreDictionary=\"" + primaryOreDict + "\" number=\"" + primaryQty   + "\" chance=\"" + primaryChance + "\" />" +
//			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + outputSecondary + "\" itemMeta=\"" + secondaryMeta + "\" number=\"" + secondaryQty + "\" chance=\"" + secondaryChance + "\" />" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"" + outputSecondary + "\" number=\"" + secondaryQty + "\" chance=\"" + secondaryChance + "\" />" +
//			   "<itemStack oreDictionary=\"" + secondaryOreDict + "\" number=\"" + secondaryQty + "\" chance=\"" + secondaryChance + "\" />" +
//			   "<itemStack modID=\"" + rockModID + "\" itemName=\"" + rockName + "\" itemMeta=\"" + rockMeta + "\" number=\"" + rockQty + "\" chance=\"" + rockChance + "\" />" +
			   "<itemStack modID=\"" + rockModID + "\" itemName=\"" + rockName + "\" number=\"" + rockQty + "\" chance=\"" + rockChance + "\" />" +
//			   "<itemStack oreDictionary=\"" + rockOredict + "\" number=\"" + rockQty + "\" chance=\"" + rockChance + "\" />" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:sagmill", SAGMillMessage);
	}
}
