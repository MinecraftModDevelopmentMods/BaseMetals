package com.mcmoddev.lib.integration.plugins;

import com.mcmoddev.lib.integration.IIntegration;

import net.minecraftforge.fml.common.*;
import portablejim.veinminer.api.IMCMessage;

/**
 * VeinMiner Integration Plugin
 *
 * @author Jasmine Iwanek
 *
 */
public class VeinMinerBase implements IIntegration {

	public static final String PLUGIN_MODID = "veinminer";

	private static boolean initDone = false;

	@Override
	public void init() {
		if (initDone || !com.mcmoddev.basemetals.util.Config.Options.enableVeinminer) {
			return;
		}

		initDone = true;
	}

	/**
	 * Add tools to the VeinMiner list from a Material
	 *
	 * @param material
	 *            Material to add
	 */
	protected static void addToolsForMaterial(String material) {
		final String ownerModID = Loader.instance().activeModContainer().getModId();
		IMCMessage.addTool("axe", ownerModID + ":" + material + "_axe");
		IMCMessage.addTool("hoe", ownerModID + ":" + material + "_hoe");
		IMCMessage.addTool("pickaxe", ownerModID + ":" + material + "_pickaxe");
		IMCMessage.addTool("shears", ownerModID + ":" + material + "_shears");
		IMCMessage.addTool("shovel", ownerModID + ":" + material + "_shovel");
		// IMCMessage.addTool("crook", ownerModID + ":" + material + "_crook");
		IMCMessage.addTool("hammer", ownerModID + ":" + material + "_hammer");
	}
}
