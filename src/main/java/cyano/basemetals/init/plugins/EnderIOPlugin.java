package cyano.basemetals.init.plugins;

import cyano.basemetals.BaseMetals;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EnderIOPlugin {

	private static final String ENDER_IO_MODID = "EnderIO";

	private static boolean initDone = false;

	// // Loader.instance().activeModContainer().getModId()
	private static String MODID = BaseMetals.MODID;

	// @formatter:off
	private static final String ALLOY_SMELTER_MSG =
			"<recipeGroup name=\"" + MODID + "\" >" +
			 "<recipe name=\"%s\" energyCost=\"%d\" >" +
			  "<input>" +
			   "<itemStack modID=\"" + MODID + "\" itemName=\"%s\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + MODID + "\" itemName=\"%s\" />" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";

	private static final String SAG_MILL_MSG =
			"<recipeGroup name=\"" + MODID + "\">" +
			 "<recipe name=\"%s\" energyCost=\"%d\">" +
			  "<input>" +
			   "<itemStack modID=\"" + MODID + "\" itemName=\"%s\" itemMeta=\"%d\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + MODID + "\" itemName=\"%s\" itemMeta=\"%d\" number=\"2\" />" +
			   "<itemStack modID=\"" + MODID + "\" itemName=\"%s\" itemMeta=\"%d\" number=\"1\" chance=\"0.1\" />" +
			   "<itemStack modID=\"minecraft\" itemName=\"%s\" chance=\"0.15\"/>" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
	// @formatter:on

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		// TODO: Verify blocks, Slabs, Ingots, Oreblocks
		if (Loader.isModLoaded(ENDER_IO_MODID)) {
			addSagMillRecipe("aluminum", null, 3600);
			addSagMillRecipe("aluminumbrass", null, 3600);
			addSagMillRecipe("cadmium", null, 3600);
			addSagMillRecipe("chromium", null, 3600);
			addSagMillRecipe("galvanizedsteel", null, 3600);
			addSagMillRecipe("iridium", null, 3600);
			addSagMillRecipe("magnesium", null, 3600);
			addSagMillRecipe("manganese", null, 3600);
			addSagMillRecipe("nichrome", null, 3600);
			addSagMillRecipe("osmium", null, 3600);
			addSagMillRecipe("plutonium", null, 3600);
			addSagMillRecipe("rutile", null, 3600);
			addSagMillRecipe("stainlesssteel", null, 3600);
			addSagMillRecipe("tantalum", null, 3600);
			addSagMillRecipe("titanium", null, 3600);
			addSagMillRecipe("tungsten", null, 3600);
			addSagMillRecipe("uranium", null, 3600);
			addSagMillRecipe("zirconium", null, 3600);
		}

		initDone = true;
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param metal
	 * @param outputSecondary
	 * @param energy
	 */
	public static void addAlloySmelterRecipe(String metal, String outputSecondary, int energy) {
		final String str = String.format(ALLOY_SMELTER_MSG, metal, energy, metal + "_ore", metal + "_ingot");
		FMLInterModComms.sendMessage(ENDER_IO_MODID, "recipe:alloysmelter", str);
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param metal
	 * @param outputSecondary
	 * @param energy
	 */
	public static void addSagMillRecipe(String metal, String outputSecondary, int energy) {

		final String str = String.format(SAG_MILL_MSG, metal, energy, metal + "_ore", 0, metal + "_powder", 0, outputSecondary, 0, "cobblestone");
		FMLInterModComms.sendMessage(ENDER_IO_MODID, "recipe:sagmill", str);
	}
}
