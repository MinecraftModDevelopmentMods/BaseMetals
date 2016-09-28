package cyano.basemetals.init;

import org.apache.logging.log4j.Level;

import cyano.basemetals.BaseMetals;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import portablejim.veinminer.api.IMCMessage;

/**
 *
 * @author Jasmine Iwanek
 *
 */
public class VeinMinerPlugin {

	private static boolean initDone = false;

	/**
	 *
	 */
	public static void init() {
		if (initDone)
			return;

		if (Loader.isModLoaded("VeinMiner") || Loader.isModLoaded("veinminer")) {
			FMLLog.log(Level.ERROR, "%s: Activating VeinMiner Support", BaseMetals.MODID);

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
		}

		initDone = true;
	}

	/**
	 *
	 * @param metal
	 */
	public static void addToolsForMetal(String metal) {
		IMCMessage.addTool("axe", BaseMetals.MODID + ":" + metal + "_axe");
		IMCMessage.addTool("hoe", BaseMetals.MODID + ":" + metal + "_hoe");
		IMCMessage.addTool("pickaxe", BaseMetals.MODID + ":" + metal + "_pickaxe");
		IMCMessage.addTool("shears", BaseMetals.MODID + ":" + metal + "_shears");
		IMCMessage.addTool("shovel", BaseMetals.MODID + ":" + metal + "_shovel");
		// IMCMessage.addTool("crook", BaseMetals.MODID + ":" + metal + "_crook");
		IMCMessage.addTool("hammer", BaseMetals.MODID + ":" + metal + "_hammer");
	}
}
