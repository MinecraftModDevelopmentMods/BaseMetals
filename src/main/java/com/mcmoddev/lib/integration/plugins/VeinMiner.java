package com.mcmoddev.lib.integration.plugins;

//import com.mcmoddev.basemetals.integration.BaseMetalsPlugin;
import com.mcmoddev.lib.integration.IIntegration;

import net.minecraftforge.fml.common.*;
import portablejim.veinminer.api.IMCMessage;

/**
 * VeinMiner Integration Plugin
 *
 * @author Jasmine Iwanek
 *
 */
//@BaseMetalsPlugin(VeinMiner.PLUGIN_MODID)
public class VeinMiner implements IIntegration {

	public static final String PLUGIN_MODID = "veinminer";

//	protected static final String OWNER_MODID = Loader.instance().activeModContainer().getModId();

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.ENABLE_VEINMINER) {
			return;
		}

		initDone = true;
	}

	/**
	 * Add tools to the VeinMiner list from a Material
	 *
	 * @param material Material to add
	 */
	protected static void addToolsForMaterial(String material) {
		final String OWNER_MODID = Loader.instance().activeModContainer().getModId();
		IMCMessage.addTool("axe", OWNER_MODID + ":" + material + "_axe");
		IMCMessage.addTool("hoe", OWNER_MODID + ":" + material + "_hoe");
		IMCMessage.addTool("pickaxe", OWNER_MODID + ":" + material + "_pickaxe");
		IMCMessage.addTool("shears", OWNER_MODID + ":" + material + "_shears");
		IMCMessage.addTool("shovel", OWNER_MODID + ":" + material + "_shovel");
		// IMCMessage.addTool("crook", OWNER_MODID + ":" + material + "_crook");
		IMCMessage.addTool("hammer", OWNER_MODID + ":" + material + "_hammer");
	}
}
