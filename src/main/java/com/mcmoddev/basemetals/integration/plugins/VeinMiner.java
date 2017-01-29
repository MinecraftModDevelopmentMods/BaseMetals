package com.mcmoddev.basemetals.integration.plugins;

import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.basemetals.integration.IIntegration;

import net.minecraftforge.fml.common.*;
import portablejim.veinminer.api.IMCMessage;

/**
 * VeinMiner Integration Plugin
 *
 * @author Jasmine Iwanek
 */
@BaseMetalsPlugin(VeinMiner.PLUGIN_MODID)
public class VeinMiner implements IIntegration {

	public static final String PLUGIN_MODID = "veinminer";

	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_VEINMINER) {
			return;
		}

		addToolsForMaterial("aluminum");
		addToolsForMaterial("aluminumbrass");
		addToolsForMaterial("cadmium");
		addToolsForMaterial("chromium");
		addToolsForMaterial("galvanizedsteel");
		addToolsForMaterial("iridium");
		addToolsForMaterial("magnesium");
		addToolsForMaterial("manganese");
		addToolsForMaterial("nichrome");
		addToolsForMaterial("osmium");
		addToolsForMaterial("plutonium");
		addToolsForMaterial("rutile");
		addToolsForMaterial("stainlesssteel");
		addToolsForMaterial("tantalum");
		addToolsForMaterial("titanium");
		addToolsForMaterial("tungsten");
		addToolsForMaterial("uranium");
		addToolsForMaterial("zirconium");

		initDone = true;
	}

	/**
	 * Add tools to the VeinMiner list from a Material
	 *
	 * @param material Material to add
	 */
	protected static void addToolsForMaterial(String material) {
		IMCMessage.addTool("axe", OWNER_MODID + ":" + material + "_axe");
		IMCMessage.addTool("hoe", OWNER_MODID + ":" + material + "_hoe");
		IMCMessage.addTool("pickaxe", OWNER_MODID + ":" + material + "_pickaxe");
		IMCMessage.addTool("shears", OWNER_MODID + ":" + material + "_shears");
		IMCMessage.addTool("shovel", OWNER_MODID + ":" + material + "_shovel");
		// IMCMessage.addTool("crook", OWNER_MODID + ":" + material + "_crook");
		IMCMessage.addTool("hammer", OWNER_MODID + ":" + material + "_hammer");
	}
}
