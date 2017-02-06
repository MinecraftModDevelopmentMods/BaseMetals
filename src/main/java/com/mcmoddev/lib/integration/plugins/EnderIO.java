package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 *
 * @author Jasmine Iwanek
 *
 */
//@BaseMetalsPlugin(EnderIO.PLUGIN_MODID)
public class EnderIO implements IIntegration {

	public static final String PLUGIN_MODID = "EnderIO";

	// protected static final String MODID = BaseMetals.MODID;
//	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

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
	 * @param material The Material
	 * @param outputSecondary The secondary output
	 * @param energy How much energy it costs to perform
	 */
	protected static void addAlloySmelterRecipe(String material, String outputSecondary, int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();
		// @formatter:off
		final String AlloySmelterMessage =
			"<recipeGroup name=\"" + ownerModID + "\" >" +
			 "<recipe name=\"%s\" energyCost=\"%d\" >" +
			  "<input>" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"%s\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"%s\" />" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		final String str = String.format(AlloySmelterMessage, material, energy, material + "_ore", material + "_ingot");
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:alloysmelter", str);
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param material The Material
	 * @param outputSecondary The secondary output
	 * @param energy How much energy it costs to perform
	 */
	protected static void addSagMillRecipe(String material, String outputSecondary, int energy) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();
		// @formatter:off
		final String SAGMillMessage =
			"<recipeGroup name=\"" + ownerModID + "\">" +
			 "<recipe name=\"%s\" energyCost=\"%d\">" +
			  "<input>" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"%s\" itemMeta=\"%d\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"%s\" itemMeta=\"%d\" number=\"2\" />" +
			   "<itemStack modID=\"" + ownerModID + "\" itemName=\"%s\" itemMeta=\"%d\" number=\"1\" chance=\"0.1\" />" +
			   "<itemStack modID=\"minecraft\" itemName=\"%s\" chance=\"0.15\"/>" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		final String str = String.format(SAGMillMessage, material, energy, material + "_ore", 0, material + "_powder", 0, outputSecondary, 0, "cobblestone");
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:sagmill", str);
	}
}
