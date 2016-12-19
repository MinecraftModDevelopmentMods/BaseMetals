package cyano.basemetals.init.plugins;

import net.minecraftforge.fml.common.*;
import portablejim.veinminer.api.IMCMessage;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class VeinMiner {

	protected static final String PLUGIN_MODID = "veinminer";

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

		addToolsForMetal("aluminum");
		addToolsForMetal("aluminumbrass");
		addToolsForMetal("cadmium");
		addToolsForMetal("chromium");
		addToolsForMetal("galvanizedsteel");
		addToolsForMetal("iridium");
		addToolsForMetal("magnesium");
		addToolsForMetal("manganese");
		addToolsForMetal("nichrome");
		addToolsForMetal("osmium");
		addToolsForMetal("plutonium");
		addToolsForMetal("rutile");
		addToolsForMetal("stainlesssteel");
		addToolsForMetal("tantalum");
		addToolsForMetal("titanium");
		addToolsForMetal("tungsten");
		addToolsForMetal("uranium");
		addToolsForMetal("zirconium");

		initDone = true;
	}

	/**
	 *
	 * @param metal
	 */
	protected static void addToolsForMetal(String metal) {
		IMCMessage.addTool("axe", OWNER_MODID + ":" + metal + "_axe");
		IMCMessage.addTool("hoe", OWNER_MODID + ":" + metal + "_hoe");
		IMCMessage.addTool("pickaxe", OWNER_MODID + ":" + metal + "_pickaxe");
		IMCMessage.addTool("shears", OWNER_MODID + ":" + metal + "_shears");
		IMCMessage.addTool("shovel", OWNER_MODID + ":" + metal + "_shovel");
		// IMCMessage.addTool("crook", OWNER_MODID + ":" + metal + "_crook");
		IMCMessage.addTool("hammer", OWNER_MODID + ":" + metal + "_hammer");
	}
}
