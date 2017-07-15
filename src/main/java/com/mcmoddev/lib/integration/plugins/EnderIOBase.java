package com.mcmoddev.lib.integration.plugins;

import javax.annotation.Nonnull;

import com.mcmoddev.lib.init.Materials;
import com.mcmoddev.lib.integration.IIntegration;
import com.mcmoddev.lib.material.MMDMaterial;
import com.mcmoddev.lib.material.MMDMaterial.MaterialType;
import com.mcmoddev.lib.util.ConfigBase.Options;
import com.mcmoddev.lib.util.Oredicts;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EnderIOBase implements IIntegration {

	public static final String PLUGIN_MODID = "enderio";

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !Options.isModEnabled(PLUGIN_MODID)) {
			return;
		}

		initDone = true;
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final String materialName) {
		addAlloySmelterRecipe(Materials.getMaterialByName(materialName), null, 2000);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final MMDMaterial material) {
		addAlloySmelterRecipe(material, null, 2000);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final String materialName, @Nonnull final int energy) {
		addAlloySmelterRecipe(Materials.getMaterialByName(materialName), null, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final MMDMaterial material, @Nonnull final int energy) {
		addAlloySmelterRecipe(material, null, energy);
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
	protected static void addAlloySmelterRecipe(@Nonnull final String materialName, final String outputSecondary, @Nonnull final int energy) {
			addAlloySmelterRecipe(Materials.getMaterialByName(materialName), outputSecondary, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(@Nonnull final MMDMaterial material, final String outputSecondary, @Nonnull final int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final String capitalizedName = material.getCapitalizedName();

		final String input = Oredicts.ORE + capitalizedName;
		final String output = Oredicts.INGOT + capitalizedName;

		if (!(material.hasOre()))
			return; // Only run for Ore types

		// @formatter:off
		// TODO: account for number="int", exp="float" and chance="float" too
		final String messageAlloySmelter = String.format( "<recipeGroup name=\"%s\">\n\t"
															+ "<recipe name=\"%s\" energyCost=\"%d\">\n\t\t"
																+ "<input>\n\t\t\t<itemStack oreDictionary=\"%s\"/>\n\t\t</input>\n\t\t"
																+ "<output>\n\t\t\t<itemStack oreDictionary=\"%s\" />\n\t\t</output>\n\t"
															+ "</recipe>\n</recipeGroup>",
															ownerModID, material, energy, input, output);
		// @formatter:on
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:alloysmelter", messageAlloySmelter);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 */
	protected static void addSagMillRecipe(@Nonnull final String materialName) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), null, 2400);
	}

	/**
	 *
	 * @param materialName
	 *            The Material's name
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final String materialName, @Nonnull final int energy) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), null, energy);
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
	protected static void addSagMillRecipe(@Nonnull final String materialName, final String outputSecondary, @Nonnull final int energy) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), outputSecondary, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param outputSecondary
	 *            The secondary output
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final MMDMaterial material, final String outputSecondary, @Nonnull final int energy) {
		int primaryQty = 2;
		int secondaryQty = 1;

		if (material.getType() == MaterialType.MINERAL) {
			primaryQty = 4;
		}

		addSagMillRecipe(material, primaryQty, outputSecondary, secondaryQty, energy);
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
	protected static void addSagMillRecipe(@Nonnull final String materialName, @Nonnull final int primaryQty, @Nonnull final String outputSecondary, @Nonnull final int secondaryQty, @Nonnull final int energy) {
		addSagMillRecipe(Materials.getMaterialByName(materialName), primaryQty, outputSecondary, secondaryQty, energy);
	}

	/**
	 *
	 * @param material
	 *            The Material
	 * @param primaryQty
	 *            How much to make
	 * @param outputSecondary
	 *            The secondary output
	 * @param secondaryQty
	 *            How much to make
	 * @param energy
	 *            How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(@Nonnull final MMDMaterial material, @Nonnull final int primaryQty, final String outputSecondary, @Nonnull final int secondaryQty, @Nonnull final int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();

		final String materialName = material.getName();
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

		if (!(material.hasOre()))
			return; // Only run for Ore types

		if (outputSecondary != null)
			messageSecondary = String.format("<itemStack oreDictionary=\"%s\" number=\"%d\" chance=\"%s\" />", secondaryOutput, secondaryQty, secondaryChance);
			// messageSecondary = "<itemStack oreDictionary=\"" + rockOredict + "\" number=\"" + rockQty + "\" chance=\"" + rockChance + "\" />" +

		// @formatter:off
		String messageSAGMill = String.format( "<recipeGroup name=\"%s\">\n\t"
											     +"<recipe name=\"%s\" energyCost=\"%d\">\n\t\t"
											     	+"<input>\n\t\t\t<itemStack oreDictionary=\"%s\" />\n\t\t</input>\n\t\t"
											     	+"<output>\n\t\t\t<itemStack oreDictionary=\"%s\" number=\"%d\" chance=\"%s\"/>\n\t\t\t"
											     		+"%s\n\t\t\t<itemStack oreDictionary=\"%s\" number=\"%d\" chance=\"%s\"/>\n\t\t\t"
											     		+"<itemStack modId=\"%s\" itemName=\"%s\" number=\"%d\" chance=\"%s\"/>\n\t\t</output>"
											     +"</recipe>"
											    +"</recipeGroup>",
											    ownerModID, materialName, energy, input, primaryOutput, primaryQty, primaryChance,
											    messageSecondary, secondaryOutput, secondaryQty, secondaryChance, rockModID, 
											    rockName, rockQty, rockChance );
											       
		// @formatter:on
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:sagmill", messageSAGMill);
	}
}
