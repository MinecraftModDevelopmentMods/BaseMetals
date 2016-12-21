package cyano.basemetals.integration.plugins;

import cyano.basemetals.integration.BaseMetalsPlugin;
import cyano.basemetals.integration.IIntegration;
import cyano.basemetals.util.Config.Options;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.event.FMLInterModComms;

/**
 *
 * @author Jasmine Iwanek
 *
 */
@BaseMetalsPlugin(EnderIO.PLUGIN_MODID)
public class EnderIO implements IIntegration {

	public static final String PLUGIN_MODID = "EnderIO";

	// protected static final String MODID = BaseMetals.MODID;
	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	/**
	 *
	 */
	@Override
	public void init() {
		if (initDone || !cyano.basemetals.util.Config.Options.ENABLE_ENDER_IO) {
			return;
		}

		// TODO: Verify blocks, Slabs, Ingots, Oreblocks
		addSagMillRecipe("adamantine", null, 3600);
		addSagMillRecipe("antimony", null, 3600);
		addSagMillRecipe("aquarium", null, 3600);
		addSagMillRecipe("bismuth", null, 3600);
		addSagMillRecipe("brass", null, 3600);
		addSagMillRecipe("bronze", null, 3600);
		addSagMillRecipe("coldiron", null, 3600);
		addSagMillRecipe("copper", null, 3600);
		addSagMillRecipe("cupronickel", null, 3600);
		addSagMillRecipe("electrum", null, 3600);
		addSagMillRecipe("invar", null, 3600);
		addSagMillRecipe("lead", null, 3600);
		addSagMillRecipe("mithril", null, 3600);
		addSagMillRecipe("nickel", null, 3600);
		addSagMillRecipe("pewter", null, 3600);
		addSagMillRecipe("platinum", null, 3600);
		addSagMillRecipe("silver", null, 3600);
		addSagMillRecipe("starsteel", null, 3600);
		addSagMillRecipe("steel", null, 3600);
		addSagMillRecipe("tin", null, 3600);
		addSagMillRecipe("zinc", null, 3600);

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
