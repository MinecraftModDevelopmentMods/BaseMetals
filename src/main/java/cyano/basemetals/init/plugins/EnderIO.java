package cyano.basemetals.init.plugins;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class EnderIO {

	protected static final String PLUGIN_MODID = "EnderIO";

	// protected static final String MODID = BaseMetals.MODID;
	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone) {
			return;
		}

		// TODO: Verify blocks, Slabs, Ingots, Oreblocks
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

		initDone = true;
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param metal
	 * @param outputSecondary
	 * @param energy
	 */
	protected static void addAlloySmelterRecipe(String metal, String outputSecondary, int energy) {
		// @formatter:off
		final String ALLOY_SMELTER_MSG =
			"<recipeGroup name=\"" + OWNER_MODID + "\" >" +
			 "<recipe name=\"%s\" energyCost=\"%d\" >" +
			  "<input>" +
			   "<itemStack modID=\"" + OWNER_MODID + "\" itemName=\"%s\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + OWNER_MODID + "\" itemName=\"%s\" />" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		final String str = String.format(ALLOY_SMELTER_MSG, metal, energy, metal + "_ore", metal + "_ingot");
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:alloysmelter", str);
	}

	// TODO: Use Oredicts for inputs
	/**
	 *
	 * @param metal
	 * @param outputSecondary
	 * @param energy
	 */
	protected static void addSagMillRecipe(String metal, String outputSecondary, int energy) {
	// @formatter:off
		final String SAG_MILL_MSG =
			"<recipeGroup name=\"" + OWNER_MODID + "\">" +
			 "<recipe name=\"%s\" energyCost=\"%d\">" +
			  "<input>" +
			   "<itemStack modID=\"" + OWNER_MODID + "\" itemName=\"%s\" itemMeta=\"%d\" />" +
			  "</input>" +
			  "<output>" +
			   "<itemStack modID=\"" + OWNER_MODID + "\" itemName=\"%s\" itemMeta=\"%d\" number=\"2\" />" +
			   "<itemStack modID=\"" + OWNER_MODID + "\" itemName=\"%s\" itemMeta=\"%d\" number=\"1\" chance=\"0.1\" />" +
			   "<itemStack modID=\"minecraft\" itemName=\"%s\" chance=\"0.15\"/>" +
			  "</output>" +
			 "</recipe>" +
			"</recipeGroup>";
		// @formatter:on
		final String str = String.format(SAG_MILL_MSG, metal, energy, metal + "_ore", 0, metal + "_powder", 0, outputSecondary, 0, "cobblestone");
		FMLInterModComms.sendMessage(PLUGIN_MODID, "recipe:sagmill", str);
	}
}
