package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.init.Materials;
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
public class EnderIOBase implements IIntegration {

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

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 */
	protected static void addAlloySmelterRecipe(String materialName) {
		addAlloySmelterRecipe(materialName, null, 2000);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(String materialName, int energy) {
		addAlloySmelterRecipe(materialName, null, energy);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(String materialName, String outputSecondary, int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final MetalMaterial material = Materials.getMaterialByName(materialName);
		final String capitalizedName = material.getCapitalizedName();

		final String input = Oredicts.ORE + capitalizedName;
		final String output = Oredicts.INGOT + capitalizedName;

		if (!(material.hasOre))
			return; // Only run for Ore types

		// @formatter:off
		final String messageAlloySmelter =
			"<recipeGroup name=\"" + ownerModID + "\" >" +
			 "<recipe name=\"" + material + "\" energyCost=\"" + energy + "\" >" +
			  "<input>" +
			   "<itemStack oreDictionary="+ input + "\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack oreDictionary=\"" + output + "\" />" +
			   // TODO: account for number="int", exp="float" and chance="float" too
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:alloysmelter", messageAlloySmelter);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 */
	protected static void addSagMillRecipe(String materialName) {
		addSagMillRecipe(materialName, null, 2400);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(String materialName, int energy) {
		addSagMillRecipe(materialName, null, energy);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(String materialName, String outputSecondary, int energy) {
		MetalMaterial material = Materials.getMaterialByName(materialName);

		// we need to be sure the material is enabled, a null check is the best way
		if( material == null ) {
			return;
		}
		
		int primaryQty = 2;
		int secondaryQty = 1;

		if (material.getType() == MaterialType.MINERAL) {
			primaryQty = 4;
		}

		addSagMillRecipe(materialName, primaryQty, outputSecondary, secondaryQty, energy);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param primaryQty
	 *            How much to make
	 * @param outputSecondary
	 *            The secondary output
	 * @param secondaryQty
	 *            How much to make
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(String materialName, int primaryQty, String outputSecondary, int secondaryQty, int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final MetalMaterial material = Materials.getMaterialByName(materialName);
		final String capitalizedName = material.getCapitalizedName();

		final String input = Oredicts.ORE + capitalizedName;

		String primaryOutput = Oredicts.DUST + capitalizedName;
		String secondaryOutput = Oredicts.DUST + outputSecondary;
		if (material.getType() == MaterialType.GEM) {
			primaryOutput = Oredicts.GEM + capitalizedName;
			secondaryOutput = Oredicts.GEM + outputSecondary;
		}

		final String primaryChance = "1.0";
		final String secondaryChance = "0.1";

		final String rockModID = "minecraft";
		final String rockName = "cobblestone";
		// final String rockName = "gravel";
		// final String rockName = "sandstone";
		// final String rockName = "netherrack";
		// final String rockName = "end_stone";
		final int rockQty = 1;
		final String rockChance = "0.15";
		String messageSecondary = "";

		if (!(material.hasOre))
			return; // Only run for Ore types

		if (outputSecondary != null)
			messageSecondary = "<itemStack oreDictionary=\"" + secondaryOutput + "\" number=\"" + secondaryQty + "\" chance=\"" + secondaryChance + "\" />";
			// messageSecondary = "<itemStack oreDictionary=\"" + rockOredict + "\" number=\"" + rockQty + "\" chance=\"" + rockChance + "\" />" +

		// @formatter:off
		String messageSAGMill =
			"<recipeGroup name=\"" + ownerModID + "\" >" +
			 "<recipe name=\"" + materialName + "\" energyCost=\"" + energy + "\" >" +
			  "<input>" +
			   "<itemStack oreDictionary="+ input + "\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack oreDictionary=\"" + primaryOutput + "\" number=\"" + primaryQty + "\" chance=\"" + primaryChance + "\" />" +
			   messageSecondary +
			   "<itemStack oreDictionary=\"" + secondaryOutput + "\" number=\"" + secondaryQty + "\" chance=\"" + secondaryChance + "\" />" +
			   "<itemStack modID=\"" + rockModID + "\" itemName=\"" + rockName + "\" number=\"" + rockQty + "\" chance=\"" + rockChance + "\" />" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:sagmill", messageSAGMill);
	}
}
